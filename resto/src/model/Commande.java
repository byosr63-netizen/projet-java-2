package model;

import java.time.LocalDateTime;

public class Commande {

    private int idcommande;
    private EtatCommande etat;
    private LocalDateTime dateCommande;
    private int idClient;
    private int idServeur;

    public Commande() {}

    public Commande(int idcommande, EtatCommande etat, LocalDateTime dateCommande,
                    int idClient, int idServeur) {
        this.idcommande = idcommande;
        this.etat = etat;
        this.dateCommande = dateCommande;
        this.idClient = idClient;
        this.idServeur = idServeur;
    }

    public int getIdcommande() { return idcommande; }
    public void setIdcommande(int idcommande) { this.idcommande = idcommande; }

    public EtatCommande getEtat() { return etat; }
    public void setEtat(EtatCommande etat) { this.etat = etat; }

    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }

    public int getIdServeur() { return idServeur; }
    public void setIdServeur(int idServeur) { this.idServeur = idServeur; }
}