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
        if (instance == null) instance = new CommandeController();
        return instance;
    }

    private CommandeDAO commandeDAO = new CommandeDAO();
    private LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
    private PlatDAO platDAO = new PlatDAO();

    private List<LigneCommande> panierClient   = new ArrayList<>();
    private List<LigneCommande> panierServeuse = new ArrayList<>();

    public List<LigneCommande> getPanier(String role) {
        return "SERVEUR".equals(role) ? panierServeuse : panierClient;
    }

   
    public void ajouterPlatAuPanier(int idPlat, String role) {
        List<LigneCommande> panier = getPanier(role);
        Plat p = platDAO.findById(idPlat);
        if (p == null || !p.isDisponible()) return;
        for (LigneCommande l : panier) {
            if (l.getIdplat() == idPlat) {
                l.setQuantite(l.getQuantite() + 1);
                return;
            }
        }
        panier.add(new LigneCommande(0, idPlat, 1, p.getPrix()));
    }

    public void supprimerPlatDuPanier(int idPlat, String role) {
        List<LigneCommande> panier = getPanier(role);
        LigneCommande target = null;
        for (LigneCommande l : panier) {
            if (l.getIdplat() == idPlat) {
                if (l.getQuantite() > 1) l.setQuantite(l.getQuantite() - 1);
                else target = l;
                break;
            }
        }
        if (target != null) panier.remove(target);
    }

  
    public Commande validerCommande(int idCommande, int idClient,
                                     int idServeur, String role) {
        List<LigneCommande> panier = getPanier(role);
        if (panier.isEmpty()) return null;

        Commande c = new Commande(idCommande, EtatCommande.DEMANDEE,
                                   LocalDateTime.now(), idClient, idServeur);
        commandeDAO.insert(c);

        int realId = c.getIdcommande();
        for (LigneCommande l : panier) {
            l.setIdcommande(realId);
            ligneDAO.insert(l);
        }
        panier.clear();
        return c;
    }


    public double calculerTotalPanier(String role) {
        double total = 0;
        for (LigneCommande l : getPanier(role)) {
            Plat p = platDAO.findById(l.getIdplat());
            if (p != null) total += l.getQuantite() * p.getPrix();
        }
        return total;
    }


    public Commande getCommandeById(int id) {
        return commandeDAO.findById(id);
    }

    public List<Commande> getCommandesParEtat(EtatCommande etat) {
        List<Commande> res = new ArrayList<>();
        for (Commande c : commandeDAO.getAll())
            if (c.getEtat() == etat) res.add(c);
        return res;
    }

    public List<Commande> getDemandee() { return getCommandesParEtat(EtatCommande.DEMANDEE); }
    public List<Commande> getEnCours()  { return getCommandesParEtat(EtatCommande.EN_COURS); }
    public List<Commande> getPrete()    { return getCommandesParEtat(EtatCommande.PRETE); }
    public List<Commande> getServie()   { return getCommandesParEtat(EtatCommande.SERVIE); }

    
    public boolean passerEnCours(int id) { return updateEtat(id, EtatCommande.EN_COURS); }
    public boolean passerPrete(int id)   { return updateEtat(id, EtatCommande.PRETE); }
    public boolean passerServie(int id)  { return updateEtat(id, EtatCommande.SERVIE); }

    private boolean updateEtat(int id, EtatCommande etat) {
        Commande c = commandeDAO.findById(id);
        if (c == null) return false;
        c.setEtat(etat);
        commandeDAO.update(c);
        return true;
    }

    public boolean annulerTraitement(int idCommande) {
        Commande c = commandeDAO.findById(idCommande);
        if (c == null) return false;
        if (c.getEtat() != EtatCommande.EN_COURS) return false;
        c.setEtat(EtatCommande.DEMANDEE);
        commandeDAO.update(c);
        return true;
    }

  
    public double getTotalCommande(int idCommande) {
        double total = 0;
        for (LigneCommande l : ligneDAO.getAll())
            if (l.getIdcommande() == idCommande)
                total += l.getQuantite() * l.getPrixUnitaire();
        return total;
    }

    public List<Plat> getAllPlats() { return platDAO.getAll(); }

    public boolean ajouterPlat(int id, String nom, double prix,
                                boolean disponible, int idMenu, String image) {
        if (nom == null || nom.isEmpty() || prix <= 0) return false;
        platDAO.insert(new Plat(id, nom, prix, disponible, idMenu, image));
        return true;
    }

    public boolean modifierPlat(int id, String nom, double prix,
                                 boolean disponible, int idMenu, String image) {
        Plat p = platDAO.findById(id);
        if (p == null) return false;
        p.setNom(nom); p.setPrix(prix);
        p.setDisponible(disponible);
        p.setIdmenu(idMenu); p.setImage(image);
        platDAO.update(p);
        return true;
    }

    public boolean supprimerPlat(int id) {
        Plat p = platDAO.findById(id);
        if (p == null) return false;
        platDAO.delete(p.getIdplat());
        return true;
    }
}