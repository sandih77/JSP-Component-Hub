package gui;

public class Auto extends Composant {

    String nom;
    int prix;
    Etat etat;

    public String getNom() {
        return this.nom;
    }

    public int getPrix() {
        return this.prix;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void setPrix(int prix) {
        this.prix = prix;
    }

    public Etat getEtat() {
        return this.etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }
}
