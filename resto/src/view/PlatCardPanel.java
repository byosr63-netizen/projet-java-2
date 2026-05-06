package view;

import controller.CommandeController;
import model.Plat;

import javax.swing.*;
import java.awt.*;

public class PlatCardPanel extends JPanel {

    public PlatCardPanel(Plat plat,
                         CommandeController controller,
                         PanierPanel panierPanel) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(236,160,180), 2));

        // ================= IMAGE SAFE =================
        JLabel img = new JLabel();

        java.net.URL imgUrl = getClass().getResource("/image/" + plat.getImage());

        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            Image scaled = icon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(scaled));
        } else {
            img.setText("No Image");
            img.setHorizontalAlignment(SwingConstants.CENTER);
        }

        img.setAlignmentX(CENTER_ALIGNMENT);

        // ================= INFOS =================
        JLabel name = new JLabel(plat.getNom());
        JLabel price = new JLabel(plat.getPrix() + " DT");

        name.setAlignmentX(CENTER_ALIGNMENT);
        price.setAlignmentX(CENTER_ALIGNMENT);

        price.setForeground(new Color(236,160,180));

        // ================= BUTTONS =================
        JButton plus = new JButton("+");
        JButton minus = new JButton("-");

        style(plus);
        style(minus);

        JPanel btns = new JPanel();
        btns.setOpaque(false);
        btns.add(minus);
        btns.add(plus);

        // ================= ACTIONS =================
        plus.addActionListener(e -> {

            controller.ajouterPlatAuPanier(plat.getIdplat());

            panierPanel.refresh(controller);
        });

        minus.addActionListener(e -> {

            controller.supprimerPlatDuPanier(plat.getIdplat());

            panierPanel.refresh(controller);
        });

        // ================= UI =================
        add(img);
        add(name);
        add(price);
        add(btns);
    }

    private void style(JButton b) {
        b.setBackground(new Color(236,160,180));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
    }
}