package view;

import controller.UserController;
import model.Role;
import util.ThemeUtils;

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

        JLabel title = new JLabel("LOGIN");
        title.setBounds(160, 80, 200, 40);

        ThemeUtils.styleTitle(title);

        panel.add(title);

     
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(70, 180, 200, 25);

        ThemeUtils.styleText(userLabel);

        panel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(70, 210, 300, 35);

        userField.setFont(Theme.TEXT_FONT);

        panel.add(userField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(70, 270, 200, 25);

        ThemeUtils.styleText(passLabel);

        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(70, 300, 300, 35);

        passField.setFont(Theme.TEXT_FONT);

        panel.add(passField);

      
        JButton loginBtn = new JButton("Connexion");
        loginBtn.setBounds(120, 470, 200, 45);

        ThemeUtils.styleButton(loginBtn);

        panel.add(loginBtn);

       
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

                JOptionPane.showMessageDialog(
                        this,
                        "Login ou mot de passe incorrect !"
                );
            }
        });

        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}