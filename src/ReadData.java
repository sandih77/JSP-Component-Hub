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
                return liste; // Fichier vide
            }

            String[] headers = headerLine.split(";");
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] values = line.split(";", -1);
                Object instance = clazz.getDeclaredConstructor().newInstance();

                for (int i = 0; i < headers.length; i++) {
                    String fieldName = headers[i];
                    String valueStr = i < values.length ? values[i] : "";

                    try {
                        Field field = clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Class<?> type = field.getType();

                        // Si le champ est un composant déroulant comme Sexe
                        if (Deroulante.class.isAssignableFrom(type)) {
                            Deroulante deroulante = (Deroulante) type.getDeclaredConstructor().newInstance();
                            deroulante.setValeurSelectionnee(valueStr);
                            field.set(instance, deroulante);
                        } else {
                            Object convertedValue = convert(valueStr, type);
                            field.set(instance, convertedValue);
                        }
                    } catch (NoSuchFieldException e) {
                        // champ non trouvé, ignorer
                    }
                }

                liste.add(instance);
            }
        }

        return liste;
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
