package util;

import javax.swing.*;

import view.Theme;

import java.awt.*;

public class ThemeUtils {

    public static void styleButton(JButton btn) {

        btn.setBackground(Theme.ROSE);
        btn.setForeground(Color.WHITE);

        btn.setFont(Theme.BUTTON_FONT);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setPreferredSize(new Dimension(140, 40));
    }

    public static void stylePanel(JPanel panel) {

        panel.setBackground(Theme.ROSE_LIGHT);
    }

    public static void styleTitle(JLabel label) {

        label.setForeground(Theme.ROSE_DARK);
        label.setFont(Theme.TITLE_FONT);
    }

    public static void styleText(JLabel label) {

        label.setForeground(Theme.DARK_TEXT);
        label.setFont(Theme.TEXT_FONT);
    }

    public static void styleList(JList<?> list) {

        list.setFont(Theme.TEXT_FONT);
        list.setBackground(Color.WHITE);
        list.setSelectionBackground(Theme.ROSE);
        list.setSelectionForeground(Color.WHITE);
    }

    public static void styleTabs(JTabbedPane tabs) {

        tabs.setBackground(Theme.ROSE);
        tabs.setForeground(Color.WHITE);

        tabs.setFont(Theme.BUTTON_FONT);
    }
}