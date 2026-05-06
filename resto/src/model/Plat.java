package model;

public class Plat {
    private int idplat;
    private String nom;
    private double prix;
    private boolean disponible;
    private int idmenu;
    private String image;

    public Plat() {}

    public Plat(int idplat, String nom, double prix, boolean disponible, int idmenu,String image) {
        this.idplat = idplat;
        this.nom = nom;
        this.prix = prix;
        this.disponible = disponible;
        this.idmenu = idmenu;
        this.image=image;
    }

    public int getIdplat() { return idplat; }
    public void setIdplat(int idplat) { this.idplat = idplat; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public int getIdmenu() { return idmenu; }
    public void setIdmenu(int idmenu) { this.idmenu = idmenu; }

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}