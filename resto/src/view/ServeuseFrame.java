package view;

import controller.CommandeController;
import controller.FactureController;
import controller.MenuController;
import model.*;

import javax.swing.*;
import java.awt.*;

public class ServeuseFrame extends JFrame {

    private CommandeController controller = CommandeController.getInstance();
    private FactureController factureController = new FactureController();
    private MenuController menuController = new MenuController();

    private DefaultListModel<String> modelRecues = new DefaultListModel<>();
    private DefaultListModel<String> modelCours = new DefaultListModel<>();
    private DefaultListModel<String> modelPrete = new DefaultListModel<>();
    private DefaultListModel<String> modelServie = new DefaultListModel<>();

    private JList<String> listRecues = new JList<>(modelRecues);
    private JList<String> listCours = new JList<>(modelCours);
    private JList<String> listPrete = new JList<>(modelPrete);
    private JList<String> listServie = new JList<>(modelServie);

    private DefaultListModel<String> modelPlats = new DefaultListModel<>();
    private JList<String> listPlats = new JList<>(modelPlats);

    // ✅ AJOUT NOTIFICATION
    private int lastSeenPreteCount = 0;

    public ServeuseFrame() {

        setTitle("Espace Serveuse");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Commandes reçues", new JScrollPane(listRecues));
        tabs.add("En cours", new JScrollPane(listCours));
        tabs.add("Prêtes", new JScrollPane(listPrete));
        tabs.add("Servies", new JScrollPane(listServie));

        tabs.add("Menu", panelMenu());

        JButton servir = new JButton("Servir");
        JButton facture = new JButton("Facture");
        JButton refresh = new JButton("Actualiser");

        JPanel btns = new JPanel();
        btns.add(servir);
        btns.add(facture);
        btns.add(refresh);

        add(tabs, BorderLayout.CENTER);
        add(btns, BorderLayout.SOUTH);

        // ================= SERVIR =================
        servir.addActionListener(e -> {

            int i = listPrete.getSelectedIndex();
            if (i == -1) return;

            int id = extractId(modelPrete.get(i));

            if (controller.passerServie(id)) {
                JOptionPane.showMessageDialog(this, "Commande SERVIE !");
            }

            refresh();
        });

        // ================= FACTURE =================
        facture.addActionListener(e -> {

            int i = listServie.getSelectedIndex();
            if (i == -1) return;

            int id = extractId(modelServie.get(i));

            Facture f = factureController.genererFacture(id);

            if (f != null) {
                new FactureFrame(f);
            }
        });

        refresh.addActionListener(e -> refresh());

        refresh();

        // ✅ AUTO REFRESH (OPTION BONUS PROF)
        Timer timer = new Timer(3000, e -> refresh());
        timer.start();

        setVisible(true);
    }

    // ================= MENU =================
    private JPanel panelMenu() {

        JPanel p = new JPanel(new BorderLayout());

        listPlats.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton refreshMenu = new JButton("Actualiser");

        p.add(new JScrollPane(listPlats), BorderLayout.CENTER);
        p.add(refreshMenu, BorderLayout.SOUTH);

        refreshMenu.addActionListener(e -> refreshMenu());

        refreshMenu();

        return p;
    }

    // ================= REFRESH =================
    private void refresh() {

        modelRecues.clear();
        modelCours.clear();
        modelPrete.clear();
        modelServie.clear();

        for (Commande c : controller.getDemandee())
            modelRecues.addElement(format(c));

        for (Commande c : controller.getEnCours())
            modelCours.addElement(format(c));

        for (Commande c : controller.getPrete())
            modelPrete.addElement(format(c));

        for (Commande c : controller.getServie())
            modelServie.addElement(format(c));

        // ✅ NOTIFICATION
        checkNotifications();
    }

    // ================= NOTIFICATION =================
    private void checkNotifications() {

        int currentCount = controller.getPrete().size();

        if (currentCount > lastSeenPreteCount) {

            JOptionPane.showMessageDialog(
                    this,
                    " Nouvelle commande prête !"
            );
        }

        lastSeenPreteCount = currentCount;
    }

    // ================= MENU REFRESH =================
    private void refreshMenu() {

        modelPlats.clear();

        for (Plat p : controller.getAllPlats()) {

            modelPlats.addElement(
                    p.getNom() + " | " + p.getPrix() + " DT"
            );
        }
    }

    private String format(Commande c) {
        return "Commande #" + c.getIdcommande();
    }

    private int extractId(String text) {
        String[] parts = text.split("#");
        return Integer.parseInt(parts[1].split(" ")[0]);
    }

    public static void main(String[] args) {
        new ServeuseFrame();
    }
}