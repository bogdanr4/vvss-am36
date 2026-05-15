package drinkshop.domain;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {

    private int id;
    private String nume;
    private double pret;
    private String categorie;
    private String tip;

    public Product(int id, String nume, double pret,
                  String categorie,
                  String tip) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.categorie = categorie;
        this.tip = tip;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public double getPret() { return pret; }
    public String getCategorie() { return categorie; }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getTip() { return tip; }

    public void setTip(String tip) {
        this.tip = tip;
    }
    public void setNume(String nume) { this.nume = nume; }
    public void setPret(double pret) { this.pret = pret; }

    @Override
    public String toString() {
        return nume + " (" + categorie + ", " + tip + ") - " + pret + " lei";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Double.compare(pret, product.pret) == 0 && Objects.equals(nume, product.nume) && Objects.equals(categorie, product.categorie) && Objects.equals(tip, product.tip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nume, pret, categorie, tip);
    }
}