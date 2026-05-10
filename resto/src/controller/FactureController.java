package controller;

import dao.*;
import model.*;

import java.time.LocalDateTime;
import java.util.List;

public class FactureController {

    private CommandeController commandeController = CommandeController.getInstance();

    private FactureDAO factureDAO = new FactureDAO();
    private CommandeDAO commandeDAO = new CommandeDAO();
    private LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
    private PlatDAO platDAO = new PlatDAO();

    public Facture genererFacture(int idCommande) {

        Commande c = commandeDAO.findById(idCommande);

        if (c == null || c.getEtat() != EtatCommande.SERVIE) {
            return null;
        }

        Facture exist = factureDAO.findByCommandeId(idCommande);
        if (exist != null) return exist;

       
        double total = commandeController.getTotalCommande(idCommande);

        Facture f = new Facture(
                0,
                total,
                idCommande,
                LocalDateTime.now()
        );

        factureDAO.insert(f);

        return factureDAO.findByCommandeId(idCommande);
    }

    public List<Facture> getAllFactures() {
        return factureDAO.getAll();
    }
}