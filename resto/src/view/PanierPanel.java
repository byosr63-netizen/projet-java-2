package view;

import controller.CommandeController;
import model.LigneCommande;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanierPanel extends JPanel {

    private JTextArea area = new JTextArea();
    private JLabel totalLabel = new JLabel("TOTAL: 0 DT");

    // ✅ idClient et idServeur configurables
    private int idClient;
    private int idServeur;

    public PanierPanel(CommandeController controller, int idClient, int idServeur) {

        this.idClient  = idClient;
        this.idServeur = idServeur;

        setLayout(new BorderLayout());
        setBackground(new Color(255, 240, 245));

        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 14));

        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(236, 160, 180));

        JButton valider = new JButton("Valider commande");
        valider.setBackground(new Color(236, 160, 180));
        valider.setForeground(Color.WHITE);

        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(totalLabel, BorderLayout.CENTER);
        bottom.add(valider,    BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        refresh(controller);

        valider.addActionListener(e -> {
            int idCommande = (int)(System.currentTimeMillis() % 100000);
            controller.validerCommande(idCommande, this.idClient, this.idServeur);
            JOptionPane.showMessageDialog(this, "Commande envoyée !");
            refresh(controller);
        });
    }

    public void refresh(CommandeController controller) {

        List<LigneCommande> panier = controller.getPanier();
        area.setText("");
        double total = 0;

        for (LigneCommande l : panier) {
            double sub = l.getQuantite() * l.getPrixUnitaire();
            total += sub;
            area.append("Plat " + l.getIdplat()
                    + " | Qte: " + l.getQuantite()
                    + " | " + sub + " DT\n");
        }

        totalLabel.setText("TOTAL: " + total + " DT");
    }
}