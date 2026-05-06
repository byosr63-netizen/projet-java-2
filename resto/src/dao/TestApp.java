package dao;

import model.*;
import java.time.LocalDateTime;

public class TestApp {

    public static void main(String[] args) {

        System.out.println("===== DEMARRAGE TEST =====");

        // =========================
        // DAOs
        // =========================
        UserDAO userDAO = new UserDAO();
        MenuDAO menuDAO = new MenuDAO();
        PlatDAO platDAO = new PlatDAO();
        CommandeDAO commandeDAO = new CommandeDAO();
        LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
        FactureDAO factureDAO = new FactureDAO();

        // =========================
        // 1. USER
        // =========================
        User client = new User(1, "client1", "123", Role.CLIENT);
        User serveur = new User(2, "serveur1", "123", Role.SERVEUR);

        userDAO.insert(client);
        userDAO.insert(serveur);

        System.out.println("✔ Users insérés:");
        System.out.println(client.getName() + " - CLIENT");
        System.out.println(serveur.getName() + " - SERVEUR");

        // =========================
        // 2. MENU
        // =========================
        Menu menu = new Menu(1, "Menu Test", "Menu rapide");
        menuDAO.insert(menu);

        System.out.println("\n✔ Menu inséré:");
        System.out.println(menu.getNom());

        // 3. PLAT
        // =========================
        Plat plat = new Plat(1, "soupe", 10.0, true, 1,"soupe.jpg");
        platDAO.insert(plat);
        Plat plat1 = new Plat(2, "sushi", 13.0, true, 1,"futomaki.jpg");
        platDAO.insert(plat1);

        Plat plat11 = new Plat(3, "burger", 14.0, true, 1,"sushiburger.jpg");
        platDAO.insert(plat11);

        Plat plat111 = new Plat(4, "calfornia", 13.0, true, 1,"calfornia.jpg");
        platDAO.insert(plat111);
        Plat plat2 = new Plat(1, "juice", 11.0, true, 1,"juice.jpg");
        platDAO.insert(plat2);



        System.out.println("\n✔ Plat inséré:");
        System.out.println(plat111.getNom() + " - " + plat111.getPrix() + " DT");

        // =========================
        // 4. COMMANDE
        // =========================
        Commande commande = new Commande(
                1,
                EtatCommande.DEMANDEE,
                LocalDateTime.now(),
                1,
                2
        );

        commandeDAO.insert(commande);

        System.out.println("\n✔ Commande insérée:");
        System.out.println("ID: " + commande.getIdcommande());
        System.out.println("Etat: " + commande.getEtat());

        // =========================
        // 5. LIGNE COMMANDE
        // =========================
        LigneCommande lc = new LigneCommande(
                1,
                1,
                2,
                10.0
        );

        ligneDAO.insert(lc);

        System.out.println("\n✔ Ligne commande insérée:");
        System.out.println("Plat ID: " + lc.getIdplat());
        System.out.println("Commande ID: " + lc.getIdcommande());
        System.out.println("Quantité: " + lc.getQuantite());
        System.out.println("Prix: " + lc.getPrixUnitaire());

        // =========================
        // 6. FACTURE
        // =========================
        Facture facture = new Facture(
                1,
                20.0,
                1,
                LocalDateTime.now()
        );

        factureDAO.insert(facture);

        System.out.println("\n✔ Facture créée:");
        System.out.println("ID facture: " + facture.getIdfacture());
        System.out.println("Montant total: " + facture.getMontantTotal() + " DT");

        System.out.println("\n===== FIN TEST OK ✔ =====");
    }
}