package model;

public class LigneCommande {
    private int idcommande;
    private int idplat;
    private int quantite;
    private double prixUnitaire;

    public LigneCommande() {}

    public LigneCommande(int idcommande, int idplat, int quantite, double prixUnitaire) {
        this.idcommande = idcommande;
        this.idplat = idplat;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public int getIdcommande() { return idcommande; }
    public void setIdcommande(int idcommande) { this.idcommande = idcommande; }

    public int getIdplat() { return idplat; }
    public void setIdplat(int idplat) { this.idplat = idplat; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
}

