package view;

import controller.CommandeController;
import model.Plat;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientFrame extends JFrame {

    private CommandeController controller = CommandeController.getInstance();

    private JPanel gridPanel = new JPanel();
    private PanierPanel panierPanel = new PanierPanel(controller, 1, 1, "CLIENT");

    public ClientFrame() {

        setTitle("Menu Restaurant");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bg = new BackgroundPanel("/image/bg1.jpg");
        bg.setLayout(new BorderLayout());

        JLabel title = new JLabel("MENU RESTAURANT");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(236,160,180));

        JPanel top = new JPanel();
        top.setBackground(new Color(255,255,255,220));
        top.add(title);

        bg.add(top, BorderLayout.NORTH);

        gridPanel.setLayout(new GridLayout(0, 3, 15, 15));
        gridPanel.setOpaque(false);

        loadPlats();

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                scroll,
                panierPanel
        );

        split.setDividerLocation(750);

        bg.add(split, BorderLayout.CENTER);

        add(bg);

        setVisible(true);
    }

    private void loadPlats() {
        gridPanel.removeAll();
        for (Plat p : controller.getAllPlats()) {
            gridPanel.add(new PlatCardPanel(p, controller, panierPanel, "CLIENT")); // ✅
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public static void main(String[] args) {
        new ClientFrame();
    }
}