package model;

import java.time.LocalDateTime;

public class Facture {

    private int idfacture;
    private double montantTotal;
    private LocalDateTime dateFacture;
    private int idcommande;

    public Facture() {}

    public Facture(int idfacture, double montantTotal, int idcommande, LocalDateTime dateFacture) {
        this.idfacture = idfacture;
        this.montantTotal = montantTotal;
        this.idcommande = idcommande;
        this.dateFacture = dateFacture;
    }

    public int getIdfacture() { return idfacture; }
    public void setIdfacture(int idfacture) { this.idfacture = idfacture; }

    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }

    public LocalDateTime getDateFacture() { return dateFacture; }
    public void setDateFacture(LocalDateTime dateFacture) { this.dateFacture = dateFacture; }

    public int getIdcommande() { return idcommande; }
    public void setIdcommande(int idcommande) { this.idcommande = idcommande; }
}