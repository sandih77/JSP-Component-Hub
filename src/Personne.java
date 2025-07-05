package gui;

public class Personne extends Composant {

    String nom;
    String adress;
    String prenom;
    String sexe;
    int age;

    public String getNom() {
        return this.nom;
    }

    public String getAdress() {
        return this.adress;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public int getAge() {
        return this.age;
    }

    public String getSexe() {
        return this.sexe;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String Prenom) {
        this.prenom = Prenom;
    }

    public void setAdress(String Adress) {
        this.adress = Adress;
    }

    public void setSexe(String Sexe) {
        this.sexe = Sexe;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
