package main.java.gui;

import main.java.code.HotelManagement;

import javax.swing.*;
import java.awt.*;

public class ManagerPanel extends JPanel {

    private HotelManagement hotelManagement = new HotelManagement();

    public ManagerPanel(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(240,240,240));

        // ---------------------------
        // Header Label
        // ---------------------------
        JLabel header = new JLabel("Manager Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // ---------------------------
        // Button Panel
        // ---------------------------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        //buttonPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Create buttons
        JButton manageEmpsBtn = new JButton("Manage Employees"); // add/edit/delete employees
        JButton viewEmpsBtn = new JButton("View Employees");     // all workers/receptionists
        JButton manageResidentsBtn = new JButton("Manage Residents"); // add/edit/delete residents
        JButton viewResidentsBtn = new JButton("View Residents"); // view residents
        JButton trackRoomsBtn = new JButton("Track Income");       // income from rooms
        JButton viewRoomssBtn = new JButton(" --");          // --

        // Style all buttons consistently
        JButton[] buttons = {manageEmpsBtn, viewEmpsBtn, manageResidentsBtn, viewResidentsBtn, trackRoomsBtn, viewRoomssBtn};
        for (JButton btn : buttons) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
            btn.setBackground(new Color(95, 95, 95));
            btn.setForeground(Color.WHITE);
            btn.setFocusable(false);
            btn.setPreferredSize(new Dimension(200, 50));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            // Simple hover
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
        buttonPanel.add(manageEmpsBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(viewEmpsBtn, gbc);

        // Second row
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(manageResidentsBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        buttonPanel.add(viewResidentsBtn, gbc);

        // Third row
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(trackRoomsBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        buttonPanel.add(viewRoomssBtn, gbc);

        // Add button panel to main panel
        add(buttonPanel, BorderLayout.CENTER);

        // ---------------------------
        // Action Listeners (example)
        // ---------------------------
        manageEmpsBtn.addActionListener(e -> {
            frame.setContentPane(new WorkerManagementPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        viewEmpsBtn.addActionListener(e -> {
            frame.setContentPane(new ViewWorkersPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        manageResidentsBtn.addActionListener(e -> {
            frame.setContentPane(new ViewResidentsPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        viewResidentsBtn.addActionListener(e -> {
            frame.setContentPane(new ViewResidentsPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        trackRoomsBtn.addActionListener(e -> {
            // TODO: Open panel/dialog to Add/Edit/Delete residents
            JOptionPane.showMessageDialog(this, "Open Manage rooms Panel");
        });

        viewRoomssBtn.addActionListener(e -> {        
            frame.setContentPane(new ViewRoomsPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
    }
}
