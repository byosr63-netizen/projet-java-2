package view;

import model.Facture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FactureFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JLabel totalLabel = new JLabel();

    public FactureFrame(Facture f) {

        setTitle("🧾 Facture");
        setSize(450, 500);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel();
        header.setBackground(new Color(236, 160, 180));

        JLabel title = new JLabel("FACTURE RESTAURANT");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel();

        model.addColumn("Détail");
        model.addColumn("Valeur");

        model.addRow(new Object[]{"ID Facture", f.getIdfacture()});
        model.addRow(new Object[]{"Commande", f.getIdcommande()});
        model.addRow(new Object[]{"Date", f.getDateFacture()});
        model.addRow(new Object[]{"----------------", "----------------"});
        model.addRow(new Object[]{"TOTAL", f.getMontantTotal() + " DT"});

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= FOOTER =================
        totalLabel.setText("Merci pour votre visite 🍽");
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));

        add(totalLabel, BorderLayout.SOUTH);

        setVisible(true);
    }
}