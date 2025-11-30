package main.java.gui;

import javax.swing.*;
import java.awt.*;

public class ManagerPanel extends JPanel {

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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Create only 4 buttons
        JButton manageEmpsBtn = new JButton("Manage Employees");
        JButton viewEmpsBtn   = new JButton("View Employees");
        JButton trackIncomeBtn = new JButton("Track Income");
        JButton viewResidentsBtn = new JButton("View Residents");

        // Style buttons
        JButton[] buttons = {manageEmpsBtn, viewEmpsBtn, trackIncomeBtn, viewResidentsBtn};
        for (JButton btn : buttons) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
            btn.setBackground(new Color(95, 95, 95));
            btn.setForeground(Color.WHITE);
            btn.setFocusable(false);
            btn.setPreferredSize(new Dimension(230, 55));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(120, 120, 120));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(95, 95, 95));
                }
            });
        }

        // ---------------------------
        // Add buttons (2 rows × 2 columns)
        // ---------------------------
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 0;
        buttonPanel.add(manageEmpsBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        buttonPanel.add(viewEmpsBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        buttonPanel.add(trackIncomeBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        buttonPanel.add(viewResidentsBtn, gbc);

        add(buttonPanel, BorderLayout.CENTER);

        // ---------------------------
        // Action Listeners
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

        trackIncomeBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open Track Income Panel");
        });

        viewResidentsBtn.addActionListener(e -> {
            frame.setContentPane(new ViewResidentsPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
    }
}
