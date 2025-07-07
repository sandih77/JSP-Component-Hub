package tools;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {

    public static Object appelerGetter(Object objet, String attr) {
        String nomMethod = "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1);

        Object valeur = null;

        try {
            Class classe = objet.getClass();
            Method method = classe.getMethod(nomMethod);
            valeur = method.invoke(objet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return valeur;
    }

    public static void appelerSetter(Object objet, String attr, String valeur) {
        String nomMethod = "set" + attr.substring(0, 1).toUpperCase() + attr.substring(1);

        try {
            Class classe = objet.getClass();
            Class classeAttr = classe.getDeclaredField(attr).getType();
            Method method = classe.getMethod(nomMethod, classeAttr);
            Object valeurObj = convertirStringEnObjet(classeAttr, valeur);
            method.invoke(objet, valeurObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object convertirStringEnObjet(Class classe, String str) {
        Object obj = null;

        if (classe.equals(String.class)) {
            obj = str;
        } else if (classe.equals(Date.class)) {
            // SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                obj = formatter.parse(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (classe.equals(Double.TYPE)) {
            obj = Double.valueOf(str);
        }

        return obj;
    }
}
