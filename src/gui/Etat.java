package gui;

public class Etat extends Deroulante {

    public Etat() {
        String[] cle = {"0", "1", "2"};
        String[] val = {"Vendu", "Non Vendu", "Stock epuise"};
        super.setCle(cle);
        super.setValeur(val);
    }
}
