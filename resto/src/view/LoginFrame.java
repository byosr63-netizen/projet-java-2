package view;

import controller.UserController;
import model.Role;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private UserController controller = new UserController();

    public LoginFrame() {

        setTitle("Connexion Restaurant");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel panel = new BackgroundPanel("/image/bg.jpg");
        panel.setLayout(null);

        // ================= COLORS =================
        Color rose = new Color(236, 160, 180);
        Color roseDark = new Color(219, 112, 147);

        // ================= TITLE =================
        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(rose);
        title.setBounds(180, 80, 200, 40);
        panel.add(title);

        // ================= USERNAME =================
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(rose);
        userLabel.setBounds(70, 180, 200, 25);
        panel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(70, 210, 300, 35);
        panel.add(userField);

        // ================= PASSWORD =================
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(rose);
        passLabel.setBounds(70, 270, 200, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(70, 300, 300, 35);
        panel.add(passField);

        // ================= ROLE =================
        JLabel roleLabel = new JLabel("Role");

        // ================= BUTTON =================
        JButton loginBtn = new JButton("Connexion");
        loginBtn.setBounds(120, 470, 200, 45);
        loginBtn.setBackground(roseDark);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        panel.add(loginBtn);

        // ================= ACTION =================
        loginBtn.addActionListener(e -> {

            String username = userField.getText();
            String password = new String(passField.getPassword());

            boolean ok = controller.login(username, password);

            if (ok) {

                Role role = controller.getCurrentUser().getRole();

                JOptionPane.showMessageDialog(this, "Connexion OK");

                dispose();

                switch (role) {

                    case CLIENT -> new ClientFrame();
                    case CUISINIER -> new CuisinierFrame();
                    case SERVEUR -> new ServeuseFrame();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Login ou mot de passe incorrect !");
            }
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}