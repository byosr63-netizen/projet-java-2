package model;

public class Menu {
    private int idmenu;
    private String nom;
    private String description;

    public Menu() {}

    public Menu(int idmenu, String nom, String description) {
        this.idmenu = idmenu;
        this.nom = nom;
        this.description = description;
    }

    public int getIdmenu() { return idmenu; }
    public void setIdmenu(int idmenu) { this.idmenu = idmenu; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    @Override
    public String toString() {
        return idmenu + " - " + nom + " - " + description;
    }
}