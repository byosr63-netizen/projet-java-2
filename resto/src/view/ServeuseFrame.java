package view;

import controller.CommandeController;
import controller.FactureController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServeuseFrame extends JFrame {

    private CommandeController controller = CommandeController.getInstance();
    private FactureController factureController = new FactureController();

    private DefaultListModel<String> modelRecues = new DefaultListModel<>();
    private DefaultListModel<String> modelCours = new DefaultListModel<>();
    private DefaultListModel<String> modelPrete = new DefaultListModel<>();
    private DefaultListModel<String> modelServie = new DefaultListModel<>();

    private JList<String> listRecues = new JList<>(modelRecues);
    private JList<String> listCours = new JList<>(modelCours);
    private JList<String> listPrete = new JList<>(modelPrete);
    private JList<String> listServie = new JList<>(modelServie);

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
            if (i == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionne une commande prête !");
                return;
            }

            int id = extractId(modelPrete.get(i));

            if (controller.passerServie(id)) {
                JOptionPane.showMessageDialog(this, "Commande SERVIE !");
            } else {
                JOptionPane.showMessageDialog(this, "Transition impossible !");
            }

            refresh();
        });

        // ================= FACTURE =================
        facture.addActionListener(e -> {

            int i = listServie.getSelectedIndex();

            if (i == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionne une commande SERVIE !");
                return;
            }

            int id = extractId(modelServie.get(i));

            Facture f = factureController.genererFacture(id);

            if (f != null) {
                new FactureFrame(f); // 🔥 OUVRE FACTURE PROPRE
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur ou facture déjà existante !");
            }
        });
        refresh.addActionListener(e -> refresh());

        refresh();
        setVisible(true);
    }

    // ================= REFRESH =================
    private void refresh() {

        modelRecues.clear();
        modelCours.clear();
        modelPrete.clear();
        modelServie.clear();

        // reçues = DEMANDEE
        for (Commande c : controller.getDemandee())
            modelRecues.addElement(format(c));

        for (Commande c : controller.getEnCours())
            modelCours.addElement(format(c));

        for (Commande c : controller.getPrete())
            modelPrete.addElement(format(c));

        for (Commande c : controller.getServie())
            modelServie.addElement(format(c));
    }

    private String format(Commande c) {
        return "Commande #" + c.getIdcommande();
    }

    private int extractId(String text) {
        String[] parts = text.split("#");
        String idPart = parts[1].split(" ")[0];
        return Integer.parseInt(idPart);
    }

    public static void main(String[] args) {
        new ServeuseFrame();
    }
}