package main.java.gui;

import javax.swing.*;

public class HotelApp extends JFrame {

    public HotelApp() {
        setTitle("JW Marriott Hotel");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/app_icon.png")); // your icon path
        setIconImage(icon.getImage());

        setContentPane(new WelcomePanel(this)); 

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelApp::new);
    }
}
