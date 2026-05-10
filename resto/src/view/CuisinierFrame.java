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

        ThemeUtils.stylePanel(p);

        JTabbedPane sub = new JTabbedPane();

        ThemeUtils.styleTabs(sub);

        sub.add("Demandées", new JScrollPane(listDemande));
        sub.add("En cours", new JScrollPane(listCours));
        sub.add("Prêtes", new JScrollPane(listPrete));
        sub.add("Servies", new JScrollPane(listServie));

        JButton start = new JButton("Commencer");
        JButton cancel = new JButton("Annuler");
        JButton ready = new JButton("Prête");

        ThemeUtils.styleButton(start);
        ThemeUtils.styleButton(cancel);
        ThemeUtils.styleButton(ready);

        JPanel btns = new JPanel();

        ThemeUtils.stylePanel(btns);

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

        ThemeUtils.stylePanel(p);

        p.add(new JScrollPane(listPlats), BorderLayout.CENTER);

        JButton add = new JButton("Ajouter");
        JButton edit = new JButton("Modifier");
        JButton del = new JButton("Supprimer");

        ThemeUtils.styleButton(add);
        ThemeUtils.styleButton(edit);
        ThemeUtils.styleButton(del);

        JPanel btns = new JPanel();

        ThemeUtils.stylePanel(btns);

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

                // ================= IMAGE =================
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choisir image du plat");

                String image = "default.png";

                int result = chooser.showOpenDialog(this);

                if (result == JFileChooser.APPROVE_OPTION) {

                    java.io.File file = chooser.getSelectedFile();
                    image = file.getName();
                }

                controller.ajouterPlat(
                        id,
                        nom,
                        prix,
                        true,
                        idMenu,
                        image
                );

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

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choisir image");

                String image = "default.png";

                int result = chooser.showOpenDialog(this);

                if (result == JFileChooser.APPROVE_OPTION) {

                    java.io.File file = chooser.getSelectedFile();
                    image = file.getName();
                }

                controller.modifierPlat(
                        id,
                        nom,
                        prix,
                        true,
                        idMenu,
                        image
                );

                refreshPlats();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Erreur modification !");
            }
        });
    
        del.addActionListener(e -> {
            int i = listPlats.getSelectedIndex();
            if (i == -1) return;

            int id = extractId(modelPlats.get(i));

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Supprimer ce plat ?",
                "Confirmation",
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

        JButton add = new JButton("Ajouter");
        JButton edit = new JButton("Modifier");
        JButton del = new JButton("Supprimer");
        JButton close = new JButton("Fermer");

        ThemeUtils.styleButton(add);
        ThemeUtils.styleButton(edit);
        ThemeUtils.styleButton(del);
        ThemeUtils.styleButton(close);

        JPanel btns = new JPanel();

        ThemeUtils.stylePanel(btns);

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
            modelDemande.addElement(formatCommande(c));

        for (Commande c : controller.getEnCours())
            modelCours.addElement(formatCommande(c));

        for (Commande c : controller.getPrete())
            modelPrete.addElement(formatCommande(c));

        for (Commande c : controller.getServie())
            modelServie.addElement(formatCommande(c));
    }
    private String formatCommande(Commande c) {

        StringBuilder details = new StringBuilder();

        details.append("Commande #")
                .append(c.getIdcommande())
                .append(" | ");

        List<LigneCommande> lignes = controller.getPanier(); // ❌ on ne l’utilise pas ici mais DAO existe

        boolean found = false;

        for (LigneCommande l : new dao.LigneCommandeDAO().getAll()) {

            if (l.getIdcommande() == c.getIdcommande()) {

                Plat p = new dao.PlatDAO().findById(l.getIdplat());

                if (p != null) {

                    details.append(p.getNom())
                            .append(" x")
                            .append(l.getQuantite())
                            .append(" • ");

                    found = true;
                }
            }
        }

        if (!found) {
            details.append("Aucun plat");
        }

        return details.toString();
    }

    private void refreshPlats() {

        modelPlats.clear();

        for (Plat p : controller.getAllPlats()) {

            modelPlats.addElement(
                    "Plat #" + p.getIdplat() + " | " + p.getNom()
            );
        }
    }

    private void refreshMenus() {

        modelMenus.clear();

        for (Menu m : menuController.getAllMenus()) {

            modelMenus.addElement(
                    "Menu #" + m.getIdmenu() + " | " + m.getNom()
            );
        }
    }

    // =====================================================
    // UTILS
    // =====================================================
    private int extractId(String text) {

        try {

            return Integer.parseInt(
                    text.split("#")[1]
                            .split("\\|")[0]
                            .trim()
            );

        } catch (Exception e) {

            return -1;
        }
    }

    public static void main(String[] args) {

        new CuisinierFrame();
    }
}