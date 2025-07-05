package gui;

import java.lang.reflect.Field;

public class Composant {

    public String construireHtmlFormulaire() {
        StringBuilder html = new StringBuilder();

        html.append("<form action=\"traitementData.jsp\" method=\"post\">\n\n");

        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String nomChamp = field.getName();
            String typeChamp = field.getType().getSimpleName();
            String inputType = getInputType(typeChamp);

            html.append("  <label for=\"")
                .append(nomChamp)
                .append("\">")
                .append(capitalize(nomChamp))
                .append(":</label>\n");

            html.append("  <input type=\"")
                .append(inputType)
                .append("\" name=\"")
                .append(nomChamp)
                .append("\" id=\"")
                .append(nomChamp)
                .append("\" />\n\n");
        }

        html.append("  <input type=\"submit\" value=\"Envoyer\" />\n");
        html.append("</form>");

        return html.toString();
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
