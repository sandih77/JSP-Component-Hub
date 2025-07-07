package gui;

public class Personne extends Composant {

    String nom;
    String adress;
    String prenom;
    Sexe sexe;
    Mariage mariage;
    int age;

    public Mariage getMariage() {
        return this.mariage;
    }

    public void setMariage(Mariage mariage) {
        this.mariage = mariage;
    }

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

    public Sexe getSexe() {
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

    public void setSexe(Sexe Sexe) {
        this.sexe = Sexe;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Nom: " + nom + ", Adresse: " + adress + ", Prénom: " + prenom +
               ", Sexe: " + sexe + ", Age: " + age;
    }
}
