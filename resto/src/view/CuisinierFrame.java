package view;

import controller.CommandeController;
import controller.MenuController;
import model.Commande;
import model.Menu;
import model.Plat;

import javax.swing.*;
import java.awt.*;

public class CuisinierFrame extends JFrame {

    private final CommandeController controller = CommandeController.getInstance();
    private final MenuController menuController = new MenuController();

    private DefaultListModel<String> modelDemande = new DefaultListModel<>();
    private DefaultListModel<String> modelCours = new DefaultListModel<>();
    private DefaultListModel<String> modelPrete = new DefaultListModel<>();
    private DefaultListModel<String> modelServie = new DefaultListModel<>();

    private DefaultListModel<String> modelPlats = new DefaultListModel<>();
    private DefaultListModel<String> modelMenus = new DefaultListModel<>();

    private JList<String> listDemande = new JList<>(modelDemande);
    private JList<String> listCours = new JList<>(modelCours);
    private JList<String> listPrete = new JList<>(modelPrete);
    private JList<String> listServie = new JList<>(modelServie);

    private JList<String> listPlats = new JList<>(modelPlats);
    private JList<String> listMenus = new JList<>(modelMenus);

    public CuisinierFrame() {

        setTitle("👨‍🍳 Cuisinier");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Commandes", panelCommandes());
        tabs.add("Plats", panelPlats());
        tabs.add("Menus", panelMenus());

        add(tabs);

        refreshAll();

        setVisible(true);
    }

    // =====================================================
    // COMMANDES
    // =====================================================
    private JPanel panelCommandes() {

        JPanel p = new JPanel(new BorderLayout());

        JTabbedPane sub = new JTabbedPane();

        sub.add("Demandées", new JScrollPane(listDemande));
        sub.add("En cours", new JScrollPane(listCours));
        sub.add("Prêtes", new JScrollPane(listPrete));
        sub.add("Servies", new JScrollPane(listServie));

        JButton start = new JButton("Commencer");
        JButton cancel = new JButton("Annuler");
        JButton ready = new JButton("Prête");

        JPanel btns = new JPanel();
        btns.add(start);
        btns.add(cancel);
        btns.add(ready);

        p.add(sub, BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);

        start.addActionListener(e -> {
            int i = listDemande.getSelectedIndex();
            if (i == -1) return;
            int id = extractId(modelDemande.get(i));
            controller.passerEnCours(id);
            refreshCommandes();
        });

        cancel.addActionListener(e -> {
            int i = listCours.getSelectedIndex();
            if (i == -1) return;
            int id = extractId(modelCours.get(i));
            controller.annulerTraitement(id);
            refreshCommandes();
        });

        ready.addActionListener(e -> {
            int i = listCours.getSelectedIndex();
            if (i == -1) return;
            int id = extractId(modelCours.get(i));
            controller.passerPrete(id);
            refreshCommandes();
        });

        return p;
    }

    // =====================================================
    // PLATS
    // =====================================================
    private JPanel panelPlats() {

        JPanel p = new JPanel(new BorderLayout());

        p.add(new JScrollPane(listPlats), BorderLayout.CENTER);

        JButton add = new JButton("Ajouter");
        JButton edit = new JButton("Modifier");
        JButton del = new JButton("Supprimer");

        JPanel btns = new JPanel();
        btns.add(add);
        btns.add(edit);
        btns.add(del);

        p.add(btns, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("ID"));
                String nom = JOptionPane.showInputDialog("Nom");
                double prix = Double.parseDouble(JOptionPane.showInputDialog("Prix"));
                int idMenu = Integer.parseInt(JOptionPane.showInputDialog("ID Menu"));

                controller.ajouterPlat(id, nom, prix, true, idMenu, "default.png");
                refreshPlats();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur ajout !");
            }
        });

        edit.addActionListener(e -> {
            int i = listPlats.getSelectedIndex();
            if (i == -1) return;

            try {
                int id = extractId(modelPlats.get(i));
                String nom = JOptionPane.showInputDialog("Nom");
                double prix = Double.parseDouble(JOptionPane.showInputDialog("Prix"));
                int idMenu = Integer.parseInt(JOptionPane.showInputDialog("ID Menu"));

                controller.modifierPlat(id, nom, prix, true, idMenu, "default.png");
                refreshPlats();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur modification !");
            }
        });

        del.addActionListener(e -> {
            int i = listPlats.getSelectedIndex();
            if (i == -1) return;

            int id = extractId(modelPlats.get(i));
            controller.supprimerPlat(id);
            refreshPlats();
        });

        return p;
    }

    // =====================================================
    // MENUS
    // =====================================================
    private JPanel panelMenus() {

        JPanel p = new JPanel(new BorderLayout());

        p.add(new JScrollPane(listMenus), BorderLayout.CENTER);

        JButton add = new JButton("Ajouter");
        JButton edit = new JButton("Modifier");
        JButton del = new JButton("Supprimer");
        JButton close = new JButton("Fermer");

        JPanel btns = new JPanel();
        btns.add(add);
        btns.add(edit);
        btns.add(del);
        btns.add(close);

        p.add(btns, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            String nom = JOptionPane.showInputDialog("Nom");
            String desc = JOptionPane.showInputDialog("Description");

            menuController.ajouterMenu(nom, desc);
            refreshMenus();
        });

        edit.addActionListener(e -> {
            int i = listMenus.getSelectedIndex();
            if (i == -1) return;

            int id = extractId(modelMenus.get(i));

            String nom = JOptionPane.showInputDialog("Nom");
            String desc = JOptionPane.showInputDialog("Description");

            menuController.modifierMenu(id, nom, desc);
            refreshMenus();
        });

        del.addActionListener(e -> {
            int i = listMenus.getSelectedIndex();
            if (i == -1) return;

            int id = extractId(modelMenus.get(i));
            menuController.supprimerMenu(id);
            refreshMenus();
        });

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
            modelDemande.addElement("Commande #" + c.getIdcommande());

        for (Commande c : controller.getEnCours())
            modelCours.addElement("Commande #" + c.getIdcommande());

        for (Commande c : controller.getPrete())
            modelPrete.addElement("Commande #" + c.getIdcommande());

        for (Commande c : controller.getServie())
            modelServie.addElement("Commande #" + c.getIdcommande());
    }

    private void refreshPlats() {
        modelPlats.clear();

        for (Plat p : controller.getAllPlats()) {
            modelPlats.addElement("Plat #" + p.getIdplat() + " | " + p.getNom());
        }
    }

    private void refreshMenus() {
        modelMenus.clear();

        for (Menu m : menuController.getAllMenus()) {
            modelMenus.addElement("Menu #" + m.getIdmenu() + " | " + m.getNom());
        }
    }

    // =====================================================
    // UTILS
    // =====================================================
    private int extractId(String text) {
        try {
            return Integer.parseInt(text.split("#")[1].split("\\|")[0].trim());
        } catch (Exception e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        new CuisinierFrame();
    }
}