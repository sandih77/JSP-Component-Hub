package data;

import gui.Composant;
import gui.Deroulante;

import java.io.*;
import java.lang.reflect.Field;

public class SaveData {

    public void save(String filename, Object object) {
        if (object == null) return;

        File file = new File(filename);
        boolean fileExists = file.exists();
        boolean writeHeader = !fileExists || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) { // append = true
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();

            if (writeHeader) {
                writeHeaderLine(writer, fields, "");
                writer.newLine();
            }

            writeDataLine(writer, fields, object, "");
            writer.newLine();

        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void writeHeaderLine(BufferedWriter writer, Field[] fields, String prefix) throws IOException {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Class<?> type = field.getType();

            if (isPrimitiveOrWrapperOrString(type) || Deroulante.class.isAssignableFrom(type)) {
                writer.write(prefix + field.getName());
            } else {
                // Objet imbriqué, appel récursif
                writeHeaderLine(writer, type.getDeclaredFields(), prefix + field.getName() + ".");
            }

            if (i < fields.length - 1) {
                writer.write(";");
            }
        }
    }

    private void writeDataLine(BufferedWriter writer, Field[] fields, Object obj, String prefix) throws IOException, IllegalAccessException {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Object value = field.get(obj);
            Class<?> type = field.getType();

            if (value == null) {
                writer.write("");
            } else if (Deroulante.class.isAssignableFrom(type)) {
                Deroulante der = (Deroulante) value;
                String selected = der.getValeurSelectionnee();
                writer.write(selected != null ? selected : "");
            } else if (isPrimitiveOrWrapperOrString(type)) {
                writer.write(value.toString());
            } else {
                writeDataLine(writer, type.getDeclaredFields(), value, prefix + field.getName() + ".");
            }

            if (i < fields.length - 1) {
                writer.write(";");
            }
        }
    }

    private boolean isPrimitiveOrWrapperOrString(Class<?> type) {
        return type.isPrimitive() ||
                type == String.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Double.class ||
                type == Float.class ||
                type == Boolean.class ||
                type == Byte.class ||
                type == Short.class ||
                type == Character.class;
    }
}
