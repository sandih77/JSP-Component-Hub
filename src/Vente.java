package gui;

public class Vente extends Composant {

    String article;
    int quantite;
    int prix;

    public String getArticle() {
        return this.article;
    }

    public int getQuantite() {
        return this.quantite;
    }

    public int getPrix() {
        return this.prix;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }
}