package gui;

public class Deroulante extends Composant {

    String[] cle;
    String[] valeur;

    String valeurSelectionnee;

    public String getValeurSelectionnee() {
        return valeurSelectionnee;
    }

    public void setValeurSelectionnee(String valeurSelectionnee) {
        this.valeurSelectionnee = valeurSelectionnee;
    }

    public String[] getCle() {
        return cle;
    }

    public void setCle(String[] cle) {
        this.cle = cle;
    }

    public String[] getValeur() {
        return valeur;
    }

    public void setValeur(String[] valeur) {
        this.valeur = valeur;
    }

    public String construireHtmlFormulaire(String action) {
        StringBuilder html = new StringBuilder();
        if (cle != null && valeur != null && cle.length == valeur.length) {
            String name = this.getClass().getSimpleName().toLowerCase(); 
            html.append("<label for=\"").append(name).append("\">Sexe :</label>\n");
            html.append("<select name=\"").append(name).append("\" id=\"").append(name).append("\">\n");
            for (int i = 0; i < cle.length; i++) {
                html.append("  <option value=\"").append(cle[i]).append("\"");
                if (cle[i].equals(valeurSelectionnee)) {
                    html.append(" selected");
                }
                html.append(">").append(valeur[i]).append("</option>\n");
            }
            html.append("</select>\n");
        }
        return html.toString();
    }

    @Override
    public String toString() {
        String selectedKey = getValeurSelectionnee();
        String display = selectedKey;

        String[] keys = getCle();
        String[] values = getValeur();

        if (keys != null && values != null) {
            for (int i = 0; i < keys.length; i++) {
                if (keys[i].equals(selectedKey)) {
                    display = values[i];
                    break;
                }
            }
        }
        return display;
    }
}
