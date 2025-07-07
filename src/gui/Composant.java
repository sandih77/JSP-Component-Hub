package gui;

import data.SaveData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class Composant {

    List<Object> data;

    public List<Object> getData() {
        return this.data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    // === FORMULAIRE COMPLET : gère données + sauvegarde + affichage ===
    public String construireHtmlFormulaire(Map<String, String[]> paramMap, String cheminFichier, String action) {
        // Si paramètres reçus, on hydrate l'objet et sauvegarde
        if (paramMap != null && !paramMap.isEmpty()) {
            try {
                hydraterAvecParametres(paramMap, "");
                new SaveData().save(cheminFichier, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        StringBuilder html = new StringBuilder();

        html.append("<div class=\"container mt-4\">\n");
        html.append("<h2 class=\"mb-4\">Formulaire d'enregistrement</h2>\n");
        html.append("<form action=\"").append(action).append("\" method=\"post\" class=\"row g-3\">\n");

        // Champ caché classe
        html.append("  <input type=\"hidden\" name=\"class\" value=\"").append(this.getClass().getName()).append("\" />\n");

        // Champs du formulaire
        html.append(construireChampsFormulaire("", this));

        html.append("</form>\n");

        // Bouton submit en dehors du formulaire
        html.append("<div class=\"mt-3\">\n");
        html.append("  <button form=\"\" type=\"submit\" class=\"btn btn-success\" onclick=\"document.forms[0].submit();\">Envoyer</button>\n");
        html.append("</div>\n");

        return html.toString();
    }

    // Méthode récursive pour hydrater objet avec paramètres (nomChamps imbriqués avec '.'), à compléter si nécessaire
    private void hydraterAvecParametres(Map<String, String[]> paramMap, String prefix) throws Exception {
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String nomChamp = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();
            Class<?> type = field.getType();

            if (type.getName().startsWith("gui.") && !Deroulante.class.isAssignableFrom(type)) {
                // Objet imbriqué
                Object instance = field.get(this);
                if (instance == null) {
                    instance = type.getDeclaredConstructor().newInstance();
                    field.set(this, instance);
                }

                // Appel récursif sur l'objet imbriqué
                if (instance instanceof Composant composant) {
                    composant.hydraterAvecParametres(paramMap, nomChamp);
                }
            } else {
                // Champ simple ou Deroulante
                String[] valeurs = paramMap.get(nomChamp);
                if (valeurs != null && valeurs.length > 0) {
                    String valeur = valeurs[0];

                    if (Deroulante.class.isAssignableFrom(type)) {
                        Deroulante deroulante = (Deroulante) field.get(this);
                        if (deroulante == null) {
                            deroulante = (Deroulante) type.getDeclaredConstructor().newInstance();
                            field.set(this, deroulante);
                        }
                        deroulante.setValeurSelectionnee(valeur);
                    } else if (type == String.class) {
                        field.set(this, valeur);
                    } else if (type == int.class || type == Integer.class) {
                        field.set(this, Integer.parseInt(valeur));
                    } else if (type == boolean.class || type == Boolean.class) {
                        field.set(this, Boolean.parseBoolean(valeur));
                    } else if (type == long.class || type == Long.class) {
                        field.set(this, Long.parseLong(valeur));
                    } else if (type == double.class || type == Double.class) {
                        field.set(this, Double.parseDouble(valeur));
                    } else if (type == float.class || type == Float.class) {
                        field.set(this, Float.parseFloat(valeur));
                    } else {
                        // Pour autres types, on peut tenter toString ou ignorer
                        // Ignoré ici
                    }
                }
            }
        }
    }

    // === Construction HTML du formulaire avec bouton externe ===
    public String construireFormulaireHtml(String action) {
        StringBuilder html = new StringBuilder();

        html.append("<div class=\"container mt-4\">\n");
        html.append("<h2 class=\"mb-4\">Formulaire d'enregistrement</h2>\n");

        html.append("<form id=\"mainForm\" action=\"").append(action).append("\" method=\"post\" class=\"row g-3\">\n");
        html.append("  <input type=\"hidden\" name=\"class\" value=\"").append(this.getClass().getName()).append("\" />\n");

        html.append(construireChampsFormulaire("", this));
        html.append("</form>\n");

        html.append("<div class=\"mt-3\">\n");
        html.append("  <button type=\"submit\" form=\"mainForm\" class=\"btn btn-success\">Envoyer</button>\n");
        html.append("</div>\n");

        html.append("</div>\n");

        return html.toString();
    }

    // === Remplissage de l'objet via paramètres du formulaire ===
    public void remplirDepuisParametres(Map<String, String[]> paramMap) {
        try {
            Field[] fields = this.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String nomChamp = field.getName();
                Class<?> type = field.getType();

                String[] valeurs = paramMap.get(nomChamp);
                if (valeurs != null && valeurs.length > 0) {
                    String valeur = valeurs[0];

                    if (Deroulante.class.isAssignableFrom(type)) {
                        Deroulante deroulante = (Deroulante) field.get(this);
                        if (deroulante == null) {
                            deroulante = (Deroulante) type.getDeclaredConstructor().newInstance();
                        }
                        deroulante.setValeurSelectionnee(valeur);
                        field.set(this, deroulante);

                    } else if (type == String.class) {
                        field.set(this, valeur);

                    } else if (type == int.class || type == Integer.class) {
                        field.set(this, Integer.parseInt(valeur));

                    } else if (type == boolean.class || type == Boolean.class) {
                        field.set(this, valeur.equals("on"));

                    } else if (Composant.class.isAssignableFrom(type)) {
                        Object sousObjet = field.get(this);
                        if (sousObjet == null) {
                            sousObjet = type.getDeclaredConstructor().newInstance();
                        }
                        ((Composant) sousObjet).remplirDepuisParametres(paramMap);
                        field.set(this, sousObjet);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === Construction récursive des champs ===
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
                    if (valeurChamp == null) {
                        valeurChamp = type.getDeclaredConstructor().newInstance();
                    }

                    html.append("<div class=\"col-12 border rounded p-3 mb-3\">\n");
                    html.append("<h5>").append(capitalize(field.getName())).append("</h5>\n");
                    html.append(construireChampsFormulaire(nomChamp, valeurChamp));
                    html.append("</div>\n");

                } else if (Deroulante.class.isAssignableFrom(type)) {
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

    // === Affichage HTML de la table ===
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

        // Collecte tous les noms de colonnes à plat
        Object premierObjet = data.get(0);
        List<String> colonnes = new java.util.ArrayList<>();
        collectFieldNames(premierObjet, "", colonnes);

        // En-tête du tableau
        html.append("  <thead class=\"table-dark\">\n  <tr>\n");
        for (String nomColonne : colonnes) {
            html.append("    <th scope=\"col\">").append(capitalize(nomColonne)).append("</th>\n");
        }
        html.append("  </tr>\n</thead>\n");

        // Données du tableau
        html.append("  <tbody>\n");
        for (Object obj : data) {
            html.append("  <tr>\n");
            List<String> valeurs = new java.util.ArrayList<>();
            collectFieldValues(obj, valeurs);
            for (String val : valeurs) {
                html.append("    <td>").append(val == null ? "" : val).append("</td>\n");
            }
            html.append("  </tr>\n");
        }
        html.append("  </tbody>\n");
        html.append("</table>\n</div>\n</div>\n");

        return html.toString();
    }

    private void collectFieldNames(Object obj, String prefix, List<String> noms) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String nomChamp = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();
            try {
                Object valeur = field.get(obj);
                if (valeur != null && valeur.getClass().getName().startsWith("gui.") && !(valeur instanceof Deroulante)) {
                    collectFieldNames(valeur, nomChamp, noms);
                } else {
                    noms.add(nomChamp);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void collectFieldValues(Object obj, List<String> valeurs) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object val = field.get(obj);
                if (val != null && val.getClass().getName().startsWith("gui.") && !(val instanceof Deroulante)) {
                    collectFieldValues(val, valeurs);
                } else if (val instanceof Deroulante deroulante) {
                    String selected = deroulante.getValeurSelectionnee();
                    String[] cles = deroulante.getCle();
                    String[] valeursDer = deroulante.getValeur();
                    String libelle = selected;

                    if (cles != null && valeursDer != null) {
                        for (int i = 0; i < cles.length; i++) {
                            if (cles[i].equals(selected)) {
                                libelle = valeursDer[i];
                                break;
                            }
                        }
                    }
                    valeurs.add(libelle);
                } else {
                    valeurs.add(val == null ? "" : val.toString());
                }
            } catch (Exception e) {
                valeurs.add("Erreur");
            }
        }
    }

// === Helper : type input HTML selon type Java ===
    private String getInputType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" ->
                "text";
            case "int", "integer" ->
                "number";
            case "boolean" ->
                "checkbox";
            default ->
                "text";
        };
    }

// === Helper : capitaliser la 1ère lettre ===
    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
