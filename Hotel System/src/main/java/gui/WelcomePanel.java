package main.java.gui;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {

    public WelcomePanel(JFrame frame) {

        setLayout(new BorderLayout());
        setBackground(new Color(240,240,240));

        // ---------------------------
        // Top Image (Hotel Banner)
        // ---------------------------
        System.out.println(getClass().getResource("/assets/hotel_banner.jpg"));
        ImageIcon bannerIcon = new ImageIcon(
            getClass().getResource("/assets/hotel_banner.jpg")

        );
        Image scaledImg = bannerIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        JLabel bannerLabel = new JLabel(new ImageIcon(scaledImg));
        bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(bannerLabel, BorderLayout.NORTH);
        // Add padding above the image: top, left, bottom, right
        bannerLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, -10, 0));

        // ---------------------------
        // Center Welcome Text
        // ---------------------------
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(240,240,240));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome to Hotel Management System", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 22));
        title.setForeground(new Color(60, 60, 60));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Experience a seamless and elegant workflow", SwingConstants.CENTER);
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        subtitle.setForeground(new Color(90, 90, 90));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(40));  
        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(subtitle);

        add(centerPanel, BorderLayout.CENTER);

        // ---------------------------
        // Bottom Button Panel (gray, simple hover, no rounded corners)
        // ---------------------------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240,240,240));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JButton continueButton = new JButton("Continue to Login");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBackground(new Color(95, 95, 95)); // gray
        continueButton.setFocusable(false);
        continueButton.setPreferredSize(new Dimension(220, 50));
        continueButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Simple hover: just change background color
        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(120, 120, 120));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                continueButton.setBackground(new Color(95, 95, 95));
            }
        });

        continueButton.addActionListener(e -> {
            frame.setContentPane(new LoginPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        bottomPanel.add(continueButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        add(bottomPanel, BorderLayout.SOUTH);

    }
}