package data;

import tools.Tools;
import gui.Deroulante;

import java.io.*;
import java.lang.reflect.Field;

public class SaveData {

    public void save(String filename, Object object) {
        if (object == null) {
            return;
        }

        File file = new File(filename);
        boolean fileExists = file.exists();
        boolean writeHeader = true;

        if (fileExists && file.length() > 0) {
            writeHeader = false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) { // append = true

            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();

            if (writeHeader) {
                for (int i = 0; i < fields.length; i++) {
                    writer.write(fields[i].getName());
                    if (i < fields.length - 1) {
                        writer.write(";");
                    }
                }
                writer.newLine();
            }

            for (int i = 0; i < fields.length; i++) {
                String attr = fields[i].getName();
                Object value = Tools.appelerGetter(object, attr);
                if (value instanceof Deroulante deroulante) {
                    String selected = deroulante.getValeurSelectionnee();
                    String[] cles = deroulante.getCle();
                    String[] valeurs = deroulante.getValeur();

                    String affichage = selected;
                    if (cles != null && valeurs != null && cles.length == valeurs.length) {
                        for (int j = 0; j < cles.length; j++) {
                            if (cles[j].equals(selected)) {
                                affichage = valeurs[j];
                                break;
                            }
                        }
                    }

                    writer.write(affichage != null ? affichage : "");
                } else {
                    writer.write(value != null ? value.toString() : "");
                }

                if (i < fields.length - 1) {
                    writer.write(";");
                }
            }
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
