package main.java.gui;

import javax.swing.*;

import main.java.code.HotelDB;

import java.awt.*;

public class ReceptionistPanel extends JPanel {
    private HotelDB hotelDB = HotelDB.getInstance();
    public ReceptionistPanel(JFrame frame) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Welcome Receptionist!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
    }
}
