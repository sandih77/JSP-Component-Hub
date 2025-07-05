package gui;

import java.lang.reflect.Field;

public class Composant {

    public String construireHtmlFormulaire() {
        String html = "";

        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String nomChamp = field.getName();
            String typeChamp = field.getType().getSimpleName();

            html += "<label for=\"" + nomChamp + "\">" + capitalize(nomChamp) + ":</label>\n";

            String inputType = getInputType(typeChamp);

            html += "<input type=\"" + inputType + "\" name=\"" + nomChamp + "\" id=\"" + nomChamp + "\" />\n\n";
        }

        html += "<input type=\"submit\" value=\"Envoyer\" />\n";
        html += "</form>";

        return html;
    }

    public String getInputType(String javaType) {
        return switch (javaType) {
            case "int", "Integer" -> "number";
            case "String" -> "text";
            case "boolean", "Boolean" -> "checkbox";
            default -> "text";
        };
    }

    public String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
