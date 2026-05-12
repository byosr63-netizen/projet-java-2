package view;

import controller.CommandeController;
import controller.FactureController;
import controller.MenuController;
import controller.PlatController;
import model.*;
import util.ThemeUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServeuseFrame extends JFrame {

    private CommandeController controller = CommandeController.getInstance();
    private FactureController factureController = new FactureController();
    private MenuController menuController = new MenuController();
    private PlatController platController = new PlatController(); 

    private DefaultListModel<String> modelRecues  = new DefaultListModel<>();
    private DefaultListModel<String> modelCours   = new DefaultListModel<>();
    private DefaultListModel<String> modelPrete   = new DefaultListModel<>();
    private DefaultListModel<String> modelServie  = new DefaultListModel<>();

    private JList<String> listRecues  = new JList<>(modelRecues);
    private JList<String> listCours   = new JList<>(modelCours);
    private JList<String> listPrete   = new JList<>(modelPrete);
    private JList<String> listServie  = new JList<>(modelServie);

    private int lastSeenPreteCount = 0;

    private PanierPanel panierServeuse = new PanierPanel(controller, 2, 2, "SERVEUR");

    public ServeuseFrame() {

        setTitle("Espace Serveuse");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(Theme.ROSE_LIGHT);

        ThemeUtils.styleList(listRecues);
        ThemeUtils.styleList(listCours);
        ThemeUtils.styleList(listPrete);
        ThemeUtils.styleList(listServie);

        JTabbedPane tabs = new JTabbedPane();
        ThemeUtils.styleTabs(tabs);

        tabs.add("Commandes reçues", panel(modelRecues, listRecues));
        tabs.add("En cours",         panel(modelCours,  listCours));
        tabs.add("Prêtes",           panel(modelPrete,  listPrete));
        tabs.add("Servies",          panel(modelServie, listServie));
        tabs.add("Passer commande",  panelCommander());
        tabs.add("Menu",             panelMenu());

        add(tabs, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        ThemeUtils.stylePanel(btns);

        JButton servir  = new JButton("Servir");
        JButton facture = new JButton("Facture");
        JButton refresh = new JButton("Actualiser");

        ThemeUtils.styleButton(servir);
        ThemeUtils.styleButton(facture);
        ThemeUtils.styleButton(refresh);

        btns.add(servir);
        btns.add(facture);
        btns.add(refresh);

        add(btns, BorderLayout.SOUTH);

        servir.addActionListener(e -> {
            int i = listPrete.getSelectedIndex();
            if (i == -1) return;
            int id = extractId(modelPrete.get(i));
            if (controller.passerServie(id))
                JOptionPane.showMessageDialog(this, "Commande SERVIE !");
            refresh();
        });

        facture.addActionListener(e -> {
            int i = listServie.getSelectedIndex();
            if (i == -1) return;
            int id = extractId(modelServie.get(i));
            Facture f = factureController.genererFacture(id);
            if (f != null) new FactureFrame(f);
        });

        refresh.addActionListener(e -> refresh());

        refresh();

        Timer timer = new Timer(3000, e -> refresh());
        timer.start();

        setVisible(true);
    }

    private JPanel panelCommander() {
        JPanel p = new JPanel(new BorderLayout());
        ThemeUtils.stylePanel(p);

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setOpaque(false);

        for (Plat plat : platController.getAllPlats()) { 
            gridPanel.add(new PlatCardPanel(plat, controller, panierServeuse, "SERVEUR"));
        }

        JScrollPane scroll = new JScrollPane(gridPanel);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, panierServeuse);
        split.setDividerLocation(700);
        p.add(split, BorderLayout.CENTER);
        return p;
    }

    private JPanel panel(DefaultListModel<String> model, JList<String> list) {
        JPanel p = new JPanel(new BorderLayout());
        ThemeUtils.stylePanel(p);
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        return p;
    }

    private void refresh() {
        modelRecues.clear();
        modelCours.clear();
        modelPrete.clear();
        modelServie.clear();

        for (Commande c : controller.getDemandee())
            modelRecues.addElement(formatCommande(c));
        for (Commande c : controller.getEnCours())
            modelCours.addElement(formatCommande(c));
        for (Commande c : controller.getPrete())
            modelPrete.addElement(formatCommande(c));
        for (Commande c : controller.getServie())
            modelServie.addElement(formatCommande(c));

        checkNotifications();
    }

    private String formatCommande(Commande c) {
        StringBuilder sb = new StringBuilder();
        sb.append("Commande #").append(c.getIdcommande()).append(" | ");

        boolean found = false;

        for (LigneCommande l : new dao.LigneCommandeDAO().getAll()) {
            if (l.getIdcommande() == c.getIdcommande()) {
                Plat p = platController.getPlatById(l.getIdplat()); 
                if (p != null) {
                    sb.append(p.getNom()).append(" x").append(l.getQuantite()).append(" • ");
                    found = true;
                }
            }
        }

        if (!found) sb.append("Aucun plat");
        return sb.toString();
    }

    private JPanel panelMenu() {
        JPanel p = new JPanel(new BorderLayout());
        ThemeUtils.stylePanel(p);

        DefaultListModel<String> modelPlats = new DefaultListModel<>();
        JList<String> listPlats = new JList<>(modelPlats);
        listPlats.setFont(new Font("Arial", Font.PLAIN, 16));
        ThemeUtils.styleList(listPlats);

        JButton refreshMenu = new JButton("Actualiser");
        ThemeUtils.styleButton(refreshMenu);

        p.add(new JScrollPane(listPlats), BorderLayout.CENTER);
        p.add(refreshMenu, BorderLayout.SOUTH);

        Runnable load = () -> {
            modelPlats.clear();
            for (Plat pl : platController.getAllPlats()) 
                modelPlats.addElement(pl.getNom() + " | " + pl.getPrix() + " DT");
        };

        refreshMenu.addActionListener(e -> load.run());
        load.run();

        return p;
    }

    private void checkNotifications() {
        int current = controller.getPrete().size();
        if (current > lastSeenPreteCount)
            JOptionPane.showMessageDialog(this, "Nouvelle commande prête !");
        lastSeenPreteCount = current;
    }

    private int extractId(String text) {
        return Integer.parseInt(text.split("#")[1].split(" ")[0]);
    }
}