package controller;

import dao.CommandeDAO;
import dao.LigneCommandeDAO;
import dao.PlatDAO;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeController {

    private static CommandeController instance;

    public static CommandeController getInstance() {
        if (instance == null) {
            instance = new CommandeController();
        }
        return instance;
    }

    private CommandeDAO commandeDAO = new CommandeDAO();
    private LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
    private PlatDAO platDAO = new PlatDAO();

    private List<LigneCommande> panier = new ArrayList<>();

    // ================= PANIER =================

    public List<LigneCommande> getPanier() {
        return panier;
    }

    public void clearPanier() {
        panier.clear();
    }

    public void ajouterPlatAuPanier(int idPlat) {

        Plat p = platDAO.findById(idPlat);
        if (p == null || !p.isDisponible()) return;

        for (LigneCommande l : panier) {
            if (l.getIdplat() == idPlat) {
                l.setQuantite(l.getQuantite() + 1);
                return;
            }
        }

        panier.add(new LigneCommande(
                0,
                idPlat,
                1,
                p.getPrix()
        ));
    }

    public void supprimerPlatDuPanier(int idPlat) {

        LigneCommande target = null;

        for (LigneCommande l : panier) {
            if (l.getIdplat() == idPlat) {
                if (l.getQuantite() > 1) {
                    l.setQuantite(l.getQuantite() - 1);
                } else {
                    target = l;
                }
                break;
            }
        }

        if (target != null) panier.remove(target);
    }

    // ================= TOTAL PANIER =================

    public double calculerTotalPanier() {

        double total = 0;

        for (LigneCommande l : panier) {

            Plat p = platDAO.findById(l.getIdplat());

            if (p != null) {
                total += l.getQuantite() * p.getPrix();
            }
        }

        return total;
    }

    // ================= VALIDATION =================

    public Commande validerCommande(int idCommande, int idClient, int idServeur) {

        if (panier.isEmpty()) return null;

        Commande c = new Commande(
                idCommande,
                EtatCommande.DEMANDEE,
                LocalDateTime.now(),
                idClient,
                idServeur
        );

        commandeDAO.insert(c);

     // récupérer vrai id généré
     int realId = c.getIdcommande();

     for (LigneCommande l : panier) {
         l.setIdcommande(realId);
         ligneDAO.insert(l);
     }


        panier.clear();
        return c;
    }

    // ================= ETATS =================

    public boolean passerEnCours(int id) {
        return updateEtat(id, EtatCommande.EN_COURS);
    }

    public boolean passerPrete(int id) {
        return updateEtat(id, EtatCommande.PRETE);
    }

    public boolean passerServie(int id) {
        return updateEtat(id, EtatCommande.SERVIE);
    }

    private boolean updateEtat(int id, EtatCommande etat) {

        Commande c = commandeDAO.findById(id);
        if (c == null) return false;

        c.setEtat(etat);
        commandeDAO.update(c);
        return true;
    }

    // ================= FACTURE TOTAL (FIX FINAL) =================

    public double getTotalCommande(int idCommande) {

        double total = 0;

        List<LigneCommande> lignes = ligneDAO.getAll();

        for (LigneCommande l : lignes) {

            if (l.getIdcommande() == idCommande) {

                total += l.getQuantite() * l.getPrixUnitaire();
            }
        }

        return total;
    }

    // ================= COMMANDES =================

    public List<Commande> getCommandesParEtat(EtatCommande etat) {

        List<Commande> res = new ArrayList<>();

        for (Commande c : commandeDAO.getAll()) {
            if (c.getEtat() == etat) {
                res.add(c);
            }
        }

        return res;
    }

    public List<Commande> getDemandee() { return getCommandesParEtat(EtatCommande.DEMANDEE); }
    public List<Commande> getEnCours() { return getCommandesParEtat(EtatCommande.EN_COURS); }
    public List<Commande> getPrete() { return getCommandesParEtat(EtatCommande.PRETE); }
    public List<Commande> getServie() { return getCommandesParEtat(EtatCommande.SERVIE); }

    public Commande getCommandeById(int id) {
        return commandeDAO.findById(id);
    }

    // ================= FIX IMPORTANT =================
    public List<Plat> getAllPlats() {
        return platDAO.getAll();
    }
 // ================= GESTION PLATS (FIX POUR CUISINIER) =================

    public boolean ajouterPlat(int id, String nom, double prix, boolean disponible, int idMenu, String image) {

        if (nom == null || nom.isEmpty() || prix <= 0) return false;

        Plat p = new Plat(id, nom, prix, disponible, idMenu, image);
        platDAO.insert(p);

        return true;
    }

    public boolean modifierPlat(int id, String nom, double prix, boolean disponible, int idMenu, String image) {

        Plat p = platDAO.findById(id);
        if (p == null) return false;

        p.setNom(nom);
        p.setPrix(prix);
        p.setDisponible(disponible);
        p.setIdmenu(idMenu);
        p.setImage(image);

        platDAO.update(p);

        return true;
    }

    public boolean supprimerPlat(int id) {

        Plat p = platDAO.findById(id);
        if (p == null) return false;

        platDAO.delete(p);

        return true;
    }
    public boolean annulerTraitement(int idCommande) {

        Commande c = commandeDAO.findById(idCommande);
        if (c == null) return false;

        // tu autorises seulement si la commande est EN_COURS
        if (c.getEtat() != EtatCommande.EN_COURS) return false;

        c.setEtat(EtatCommande.DEMANDEE);
        commandeDAO.update(c);

        return true;
    }
}