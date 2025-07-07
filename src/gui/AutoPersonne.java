package gui;

public class AutoPersonne extends Composant {

    Auto auto;
    Personne personne;
    Vente vente;

    public Vente getVente() {
        return this.vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public Auto getAuto() {
        return this.auto;
    }

    public Personne getPersonne() {
        return this.personne;
    }

    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }
}
