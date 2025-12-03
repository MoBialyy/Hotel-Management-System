package main.java.gui;
import javax.swing.*;
import java.awt.*;

public class ReceptionistPanel extends JPanel {

    public ReceptionistPanel(JFrame frame) {

        setLayout(new BorderLayout());
        setBackground(new Color(240,240,240));

        // ---------------------------
        // Header Label
        // ---------------------------
        JLabel header = new JLabel("Receptionist Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // ---------------------------
        // Button Panel (same as ManagerPanel)
        // ---------------------------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Create buttons
        JButton manageResidentsBtn = new JButton("Manage Residents");
        JButton newBookingBtn = new JButton("New Booking");
        JButton checkoutBtn = new JButton("Checkout & Payment");
        JButton viewRoomsBtn = new JButton("View Available Rooms");
        JButton currentResidentsBtn = new JButton("Current Residents");
        JButton searchBtn = new JButton("Search booking");
        JButton addResidentBtn = new JButton("Add Resident"); 

        // Style all buttons consistently
        JButton[] buttons = {manageResidentsBtn, newBookingBtn, checkoutBtn,
                viewRoomsBtn, currentResidentsBtn, searchBtn, addResidentBtn};

        for (JButton btn : buttons) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
            btn.setBackground(new Color(95, 95, 95));
            btn.setForeground(Color.WHITE);
            btn.setFocusable(false);
            btn.setPreferredSize(new Dimension(200, 50));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // hover effect
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(120, 120, 120));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(95, 95, 95));
                }
            });
        }

        // Reset grid width for each row
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // First row
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(manageResidentsBtn, gbc);

        gbc.gridx = 1;
        buttonPanel.add(currentResidentsBtn, gbc);

        // Second row
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(newBookingBtn, gbc);

        gbc.gridx = 1;
        buttonPanel.add(checkoutBtn, gbc);

        // Third row
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(viewRoomsBtn, gbc);

        gbc.gridx = 1;
        buttonPanel.add(searchBtn, gbc);

        // Fourth row
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; 
        buttonPanel.add(addResidentBtn, gbc);

        add(buttonPanel, BorderLayout.CENTER);

        // ---------------------------
        // Actions
        // ---------------------------
        manageResidentsBtn.addActionListener(e -> {
            frame.setContentPane(new ResidentManagementPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        currentResidentsBtn.addActionListener(e -> {
            frame.setContentPane(new ViewResidentsPanelRecep(frame));
            frame.revalidate();
            frame.repaint();
        });

        newBookingBtn.addActionListener(e -> {
            frame.setContentPane(new NewBookingPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        checkoutBtn.addActionListener(e -> {
            frame.setContentPane(new PaymentPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        viewRoomsBtn.addActionListener(e -> {
            frame.setContentPane(new ViewAvailableRoomsPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        searchBtn.addActionListener(e -> {
            frame.setContentPane(new SearchBookingPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        addResidentBtn.addActionListener(e -> {
            frame.setContentPane(new AddResidentToBookingPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
    }
}
