package gui;

import java.lang.reflect.Field;
import java.util.List;

public class Composant {

    List<Object> data;

    public List<Object> getData() {
        return this.data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    // Méthode publique pour construire le formulaire HTML complet
    public String construireHtmlFormulaire(String action) {
        StringBuilder html = new StringBuilder();

        html.append("<div class=\"container mt-4\">\n");
        html.append("<h2 class=\"mb-4\">Formulaire d'enregistrement</h2>\n");
        html.append("<form action=\"").append(action).append("\" method=\"post\" class=\"row g-3\">\n");

        // Champ caché pour la classe
        html.append("  <input type=\"hidden\" name=\"class\" value=\"")
                .append(this.getClass().getName())
                .append("\" />\n");

        // Construction des champs du formulaire à partir de l'instance courante
        html.append(construireChampsFormulaire("", this));

        // Bouton submit unique
        html.append("  <div class=\"col-12\">\n");
        html.append("    <button type=\"submit\" class=\"btn btn-success\">Envoyer</button>\n");
        html.append("  </div>\n");

        html.append("</form>\n</div>\n");

        return html.toString();
    }

    // Méthode privée récursive pour construire les champs, avec instance pour récupérer valeurs
    private String construireChampsFormulaire(String prefix, Object instance) {
        StringBuilder html = new StringBuilder();

        Class<?> clazz = instance.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String nomChamp = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();
            Class<?> type = field.getType();

            try {
                Object valeurChamp = field.get(instance);

                if (type.getName().startsWith("gui.") && !Deroulante.class.isAssignableFrom(type)) {
                    // Champ imbriqué classique
                    if (valeurChamp == null) {
                        Class<?> classModel = Class.forName(type.getName());
                        valeurChamp = classModel.getDeclaredConstructor().newInstance();
                    }

                    html.append("<div class=\"col-12 border rounded p-3 mb-3\">\n");
                    html.append("<h5>").append(capitalize(field.getName())).append("</h5>\n");
                    html.append(construireChampsFormulaire(nomChamp, valeurChamp));
                    html.append("</div>\n");

                } else if (Deroulante.class.isAssignableFrom(type)) {
                    // Champ de type Deroulante ou ses sous-classes (ex : Sexe, Etat)
                    Deroulante deroulante = (Deroulante) valeurChamp;
                    if (deroulante == null) {
                        deroulante = (Deroulante) type.getDeclaredConstructor().newInstance();
                    }

                    html.append("  <div class=\"col-md-6\">\n");
                    html.append("    <label for=\"").append(nomChamp).append("\" class=\"form-label\">")
                            .append(capitalize(field.getName())).append("</label>\n");
                    html.append("    <select class=\"form-select\" name=\"").append(nomChamp).append("\" id=\"").append(nomChamp).append("\">\n");

                    String[] cles = deroulante.getCle();
                    String[] valeurs = deroulante.getValeur();
                    String selected = deroulante.getValeurSelectionnee();

                    if (cles != null && valeurs != null && cles.length == valeurs.length) {
                        for (int i = 0; i < cles.length; i++) {
                            html.append("      <option value=\"").append(cles[i]).append("\"");
                            if (cles[i].equals(selected)) {
                                html.append(" selected");
                            }
                            html.append(">").append(valeurs[i]).append("</option>\n");
                        }
                    }

                    html.append("    </select>\n");
                    html.append("  </div>\n");

                } else {
                    // Champ simple (input classique)
                    String inputType = getInputType(type.getSimpleName());

                    html.append("  <div class=\"col-md-6\">\n");
                    html.append("    <label for=\"").append(nomChamp).append("\" class=\"form-label\">")
                            .append(capitalize(field.getName())).append("</label>\n");
                    html.append("    <input type=\"").append(inputType)
                            .append("\" class=\"form-control\" name=\"").append(nomChamp)
                            .append("\" id=\"").append(nomChamp).append("\"");

                    if (valeurChamp != null) {
                        if ("checkbox".equals(inputType)) {
                            if (valeurChamp instanceof Boolean && (Boolean) valeurChamp) {
                                html.append(" checked");
                            }
                        } else {
                            html.append(" value=\"").append(valeurChamp.toString()).append("\"");
                        }
                    }

                    html.append(" />\n");
                    html.append("  </div>\n");
                }
            } catch (Exception e) {
                html.append("<!-- Erreur dans champ ").append(nomChamp).append(": ").append(e.getMessage()).append(" -->\n");
            }
        }

        return html.toString();
    }

    // Méthode pour construire un tableau HTML à partir de la liste data
    public String construireHtmlTable() {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"container mt-5\">\n");
        html.append("<h2 class=\"mb-4\">Liste des données enregistrées</h2>\n");

        html.append("<div class=\"table-responsive\">\n");
        html.append("<table class=\"table table-hover table-bordered align-middle\">\n");

        if (data == null || data.isEmpty()) {
            html.append("  <tr><td colspan=\"100%\">Aucune donnée</td></tr>\n");
            html.append("</table>\n</div>\n</div>");
            return html.toString();
        }

        Object premierObjet = data.get(0);
        Class<?> clazz = premierObjet.getClass();
        Field[] fields = clazz.getDeclaredFields();

        // En-tête du tableau
        html.append("  <thead class=\"table-dark\">\n  <tr>\n");
        for (Field field : fields) {
            html.append("    <th scope=\"col\">").append(capitalize(field.getName())).append("</th>\n");
        }
        html.append("  </tr>\n</thead>\n");

        html.append("  <tbody>\n");

        for (Object obj : data) {
            html.append("  <tr>\n");
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);

                    if (value instanceof Deroulante deroulante) {
                        String selectedKey = deroulante.getValeurSelectionnee();
                        String display = selectedKey;

                        String[] cles = deroulante.getCle();
                        String[] valeurs = deroulante.getValeur();
                        if (cles != null && valeurs != null) {
                            for (int i = 0; i < cles.length; i++) {
                                if (cles[i].equals(selectedKey)) {
                                    display = valeurs[i];
                                    break;
                                }
                            }
                        }

                        html.append("    <td>").append(display != null ? display : "").append("</td>\n");

                    } else if (value instanceof Composant composant) {
                        html.append("    <td>").append(composant.construireHtmlTable()).append("</td>\n");

                    } else {
                        html.append("    <td>").append(value != null ? value.toString() : "").append("</td>\n");
                    }

                } catch (IllegalAccessException e) {
                    html.append("    <td class=\"text-danger\">Erreur</td>\n");
                }
            }
            html.append("  </tr>\n");
        }

        html.append("  </tbody>\n</table>\n</div>\n</div>");

        return html.toString();
    }

    // Retourne le type d'input HTML en fonction du type Java
    public String getInputType(String javaType) {
        return switch (javaType) {
            case "int", "Integer" ->
                "number";
            case "boolean", "Boolean" ->
                "checkbox";
            case "String" ->
                "text";
            default ->
                "text";
        };
    }

    // Capitalize la première lettre d'une chaîne
    public String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
