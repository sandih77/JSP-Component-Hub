package data;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import gui.Composant;
import gui.Deroulante;

public class ReadData {

    public List<Object> getAllData(String filename, Class<?> clazz) throws Exception {
        List<Object> liste = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return liste; // fichier vide
            }

            String[] headers = headerLine.split(";");
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(";", -1);
                Object instance = clazz.getDeclaredConstructor().newInstance();

                for (int i = 0; i < headers.length; i++) {
                    if (i >= values.length) {
                        break;
                    }
                    String header = headers[i];
                    String valueStr = values[i];

                    // Gestion des champs imbriqués "objet.champ"
                    String[] parts = header.split("\\.");
                    Object targetObject = instance;
                    Class<?> targetClass = clazz;

                    if (parts.length == 1) {
                        // Champ simple dans la classe principale
                        try {
                            Field field = targetClass.getDeclaredField(parts[0]);
                            field.setAccessible(true);
                            setFieldValue(field, targetObject, valueStr);
                        } catch (NoSuchFieldException e) {
                            // Ignorer champ non trouvé
                        }
                    } else if (parts.length == 2) {
                        // Champ dans un sous-objet
                        String objName = parts[0];
                        String subFieldName = parts[1];

                        try {
                            // Récupérer ou créer l'objet imbriqué
                            Field objField = targetClass.getDeclaredField(objName);
                            objField.setAccessible(true);
                            Object subObject = objField.get(targetObject);
                            if (subObject == null) {
                                subObject = objField.getType().getDeclaredConstructor().newInstance();
                                objField.set(targetObject, subObject);
                            }

                            // Récupérer le champ du sous-objet et le remplir
                            Field subField = subObject.getClass().getDeclaredField(subFieldName);
                            subField.setAccessible(true);
                            setFieldValue(subField, subObject, valueStr);
                        } catch (NoSuchFieldException e) {
                            // Ignorer champ non trouvé
                        }
                    }
                    // Tu peux étendre pour plus de profondeur si besoin
                }

                liste.add(instance);
            }
        }

        return liste;
    }

    /**
     * Méthode utilitaire pour convertir la String et setter le champ
     */
    private void setFieldValue(Field field, Object target, String valueStr) throws Exception {
        Class<?> type = field.getType();

        if (Deroulante.class.isAssignableFrom(type)) {
            Deroulante deroulante = (Deroulante) type.getDeclaredConstructor().newInstance();
            deroulante.setValeurSelectionnee(valueStr);
            field.set(target, deroulante);
        } else if (type == int.class || type == Integer.class) {
            int val = valueStr.isEmpty() ? 0 : Integer.parseInt(valueStr);
            field.set(target, val);
        } else if (type == boolean.class || type == Boolean.class) {
            boolean val = valueStr.equalsIgnoreCase("true") || valueStr.equalsIgnoreCase("on");
            field.set(target, val);
        } else if (type == String.class) {
            field.set(target, valueStr);
        } else {
            // gérer d'autres types si besoin
            field.set(target, null);
        }
    }

    private Object convert(String value, Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return value.isEmpty() ? 0 : Integer.parseInt(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on");
        } else if (type == String.class) {
            return value;
        } else {
            return null;
        }
    }
}
