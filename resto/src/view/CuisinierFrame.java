package view;

import controller.CommandeController;
import controller.MenuController;
import model.Commande;
import model.LigneCommande;
import model.Menu;
import model.Plat;
import util.ThemeUtils;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class CuisinierFrame extends JFrame {

    private final CommandeController controller = CommandeController.getInstance();
    private final MenuController menuController = new MenuController();

    private DefaultListModel<String> modelDemande = new DefaultListModel<>();
    private DefaultListModel<String> modelCours   = new DefaultListModel<>();
    private DefaultListModel<String> modelPrete   = new DefaultListModel<>();
    private DefaultListModel<String> modelServie  = new DefaultListModel<>();

    private DefaultListModel<String> modelPlats = new DefaultListModel<>();
    private DefaultListModel<String> modelMenus = new DefaultListModel<>();

    private JList<String> listDemande = new JList<>(modelDemande);
    private JList<String> listCours   = new JList<>(modelCours);
    private JList<String> listPrete   = new JList<>(modelPrete);
    private JList<String> listServie  = new JList<>(modelServie);

    private JList<String> listPlats = new JList<>(modelPlats);
    private JList<String> listMenus = new JList<>(modelMenus);

    public CuisinierFrame() {

        setTitle("👨‍🍳 Cuisinier");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(Theme.ROSE_LIGHT);

        ThemeUtils.styleList(listDemande);
        ThemeUtils.styleList(listCours);
        ThemeUtils.styleList(listPrete);
        ThemeUtils.styleList(listServie);
        ThemeUtils.styleList(listPlats);
        ThemeUtils.styleList(listMenus);

        JTabbedPane tabs = new JTabbedPane();
        ThemeUtils.styleTabs(tabs);

        tabs.add("Commandes", panelCommandes());
        tabs.add("Plats",     panelPlats());
        tabs.add("Menus",     panelMenus());

        add(tabs);

        refreshAll();

        // ✅ Timer auto-refresh pour voir les nouvelles commandes
        Timer timer = new Timer(3000, e -> refreshCommandes());
        timer.start();

        setVisible(true);
    }

    // =====================================================
    // COMMANDES
    // =====================================================
    private JPanel panelCommandes() {

        JPanel p = new JPanel(new BorderLayout());
        ThemeUtils.stylePanel(p);

        JTabbedPane sub = new JTabbedPane();
        ThemeUtils.styleTabs(sub);

        sub.add("Demandées", new JScrollPane(listDemande));
        sub.add("En cours",  new JScrollPane(listCours));
        sub.add("Prêtes",    new JScrollPane(listPrete));
        sub.add("Servies",   new JScrollPane(listServie));

        JButton start  = new JButton("Commencer");
        JButton cancel = new JButton("Annuler");
        JButton ready  = new JButton("Prête");

        ThemeUtils.styleButton(start);
        ThemeUtils.styleButton(cancel);
        ThemeUtils.styleButton(ready);

        JPanel btns = new JPanel();
        ThemeUtils.stylePanel(btns);
        btns.add(start);
        btns.add(cancel);
        btns.add(ready);

        p.add(sub,  BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);

        // ===== Commencer : DEMANDEE → EN_COURS =====
        start.addActionListener(e -> {
            int i = listDemande.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(this,
                    "Sélectionnez une commande demandée !");
                return;
            }
            int id = extractId(modelDemande.get(i));
            controller.passerEnCours(id);
            refreshCommandes();
        });

        // ===== Annuler : EN_COURS → DEMANDEE =====
        cancel.addActionListener(e -> {
            int i = listCours.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(this,
                    "Sélectionnez une commande en cours !");
                return;
            }
            int id = extractId(modelCours.get(i));
            controller.annulerTraitement(id);
            refreshCommandes();
        });

        // ===== Prête : EN_COURS → PRETE =====
        ready.addActionListener(e -> {
            int i = listCours.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(this,
                    "Sélectionnez une commande en cours !");
                return;
            }
            int id = extractId(modelCours.get(i));
            controller.passerPrete(id);
            // ✅ Notification envoyée à la serveuse (via timer dans ServeuseFrame)
            JOptionPane.showMessageDialog(this,
                "Commande #" + id + " marquée PRÊTE !\nNotification envoyée à la serveuse.");
            refreshCommandes();
        });

        return p;
    }

    // =====================================================
    // PLATS
    // =====================================================
    private JPanel panelPlats() {

        JPanel p = new JPanel(new BorderLayout());
        ThemeUtils.stylePanel(p);

        p.add(new JScrollPane(listPlats), BorderLayout.CENTER);

        JButton add  = new JButton("Ajouter");
        JButton edit = new JButton("Modifier");
        JButton del  = new JButton("Supprimer");

        ThemeUtils.styleButton(add);
        ThemeUtils.styleButton(edit);
        ThemeUtils.styleButton(del);

        JPanel btns = new JPanel();
        ThemeUtils.stylePanel(btns);
        btns.add(add);
        btns.add(edit);
        btns.add(del);

        p.add(btns, BorderLayout.SOUTH);

        // ===== AJOUTER =====
        add.addActionListener(e -> {
            try {
                String nom  = JOptionPane.showInputDialog(this, "Nom du plat :");
                if (nom == null || nom.trim().isEmpty()) return;

                double prix = Double.parseDouble(
                    JOptionPane.showInputDialog(this, "Prix :"));

                int idMenu = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "ID Menu :"));

                // ===== IMAGE =====
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choisir image du plat");
                String image = "default.png";
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                    image = chooser.getSelectedFile().getName();

                // ✅ ID auto — on passe 0, la BDD génère l'ID
                controller.ajouterPlat(0, nom, prix, true, idMenu, image);
                refreshPlats();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur ajout : " + ex.getMessage());
            }
        });

        // ===== MODIFIER =====
        edit.addActionListener(e -> {
            int i = listPlats.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un plat !");
                return;
            }
            try {
                int id = extractId(modelPlats.get(i));

                String nom = JOptionPane.showInputDialog(this, "Nouveau nom :");
                if (nom == null || nom.trim().isEmpty()) return;

                double prix = Double.parseDouble(
                    JOptionPane.showInputDialog(this, "Nouveau prix :"));

                int idMenu = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "Nouvel ID Menu :"));

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choisir image");
                String image = "default.png";
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                    image = chooser.getSelectedFile().getName();

                controller.modifierPlat(id, nom, prix, true, idMenu, image);
                refreshPlats();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur modification : " + ex.getMessage());
            }
        });

        // ===== SUPPRIMER =====
        del.addActionListener(e -> {
            int i = listPlats.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un plat !");
                return;
            }
            int id = extractId(modelPlats.get(i));
            int confirm = JOptionPane.showConfirmDialog(
                this, "Supprimer ce plat ?", "Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controller.supprimerPlat(id);
                refreshPlats();
            }
        });

        return p;
    }

    // =====================================================
    // MENUS
    // =====================================================
    private JPanel panelMenus() {

        JPanel p = new JPanel(new BorderLayout());
        ThemeUtils.stylePanel(p);

        p.add(new JScrollPane(listMenus), BorderLayout.CENTER);

        JButton add    = new JButton("Ajouter");
        JButton edit   = new JButton("Modifier");
        JButton del    = new JButton("Supprimer");
        JButton save   = new JButton("Enregistrer"); // ✅ demandé dans le sujet
        JButton close  = new JButton("Fermer");

        ThemeUtils.styleButton(add);
        ThemeUtils.styleButton(edit);
        ThemeUtils.styleButton(del);
        ThemeUtils.styleButton(save);
        ThemeUtils.styleButton(close);

        JPanel btns = new JPanel();
        ThemeUtils.stylePanel(btns);
        btns.add(add);
        btns.add(edit);
        btns.add(del);
        btns.add(save);
        btns.add(close);

        p.add(btns, BorderLayout.SOUTH);

        // ===== AJOUTER =====
        add.addActionListener(e -> {
            String nom  = JOptionPane.showInputDialog(this, "Nom du menu :");
            if (nom == null || nom.trim().isEmpty()) return;
            String desc = JOptionPane.showInputDialog(this, "Description :");
            menuController.ajouterMenu(nom, desc);
            refreshMenus();
        });

        // ===== MODIFIER =====
        edit.addActionListener(e -> {
            int i = listMenus.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un menu !");
                return;
            }
            int id = extractId(modelMenus.get(i));
            String nom  = JOptionPane.showInputDialog(this, "Nouveau nom :");
            if (nom == null || nom.trim().isEmpty()) return;
            String desc = JOptionPane.showInputDialog(this, "Nouvelle description :");
            menuController.modifierMenu(id, nom, desc);
            refreshMenus();
        });

        // ===== SUPPRIMER =====
        del.addActionListener(e -> {
            int i = listMenus.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un menu !");
                return;
            }
            int id = extractId(modelMenus.get(i));
            int confirm = JOptionPane.showConfirmDialog(
                this, "Supprimer ce menu ?", "Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                menuController.supprimerMenu(id);
                refreshMenus();
            }
        });

        // ===== ENREGISTRER ✅ =====
        save.addActionListener(e -> {
            refreshMenus();
            JOptionPane.showMessageDialog(this,
                "Menus enregistrés avec succès !");
        });

        // ===== FERMER =====
        close.addActionListener(e -> dispose());

        return p;
    }

    // =====================================================
    // REFRESH
    // =====================================================
    private void refreshAll() {
        refreshCommandes();
        refreshPlats();
        refreshMenus();
    }

    private void refreshCommandes() {
        modelDemande.clear();
        modelCours.clear();
        modelPrete.clear();
        modelServie.clear();

        for (Commande c : controller.getDemandee())
            modelDemande.addElement(formatCommande(c));
        for (Commande c : controller.getEnCours())
            modelCours.addElement(formatCommande(c));
        for (Commande c : controller.getPrete())
            modelPrete.addElement(formatCommande(c));
        for (Commande c : controller.getServie())
            modelServie.addElement(formatCommande(c));
    }

    // ✅ BUG CORRIGÉ — suppression de controller.getPanier(title)
    private String formatCommande(Commande c) {
        StringBuilder sb = new StringBuilder();
        sb.append("Commande #").append(c.getIdcommande()).append(" | ");

        boolean found = false;

        for (LigneCommande l : new dao.LigneCommandeDAO().getAll()) {
            if (l.getIdcommande() == c.getIdcommande()) {
                Plat p = new dao.PlatDAO().findById(l.getIdplat());
                if (p != null) {
                    sb.append(p.getNom())
                      .append(" x").append(l.getQuantite())
                      .append(" • ");
                    found = true;
                }
            }
        }

        if (!found) sb.append("Aucun plat");
        return sb.toString();
    }

    private void refreshPlats() {
        modelPlats.clear();
        for (Plat p : controller.getAllPlats())
            modelPlats.addElement("Plat #" + p.getIdplat() + " | " + p.getNom());
    }

    private void refreshMenus() {
        modelMenus.clear();
        for (Menu m : menuController.getAllMenus())
            modelMenus.addElement("Menu #" + m.getIdmenu() + " | " + m.getNom());
    }

    // =====================================================
    // UTILS
    // =====================================================
    private int extractId(String text) {
        try {
            return Integer.parseInt(
                text.split("#")[1].split("\\|")[0].trim()
            );
        } catch (Exception e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        new CuisinierFrame();
    }
}