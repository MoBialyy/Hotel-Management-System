package main.java.gui;

import main.java.code.HotelDB;
import main.java.code.Employee;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private HotelDB hotelDB = HotelDB.getInstance();

    public LoginPanel(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(240,240,240));

        // ---------------------------
        // Top Banner (same as WelcomePanel)
        // ---------------------------
        ImageIcon bannerIcon = new ImageIcon(getClass().getResource("/assets/hotel_banner.jpg"));
        Image scaledImg = bannerIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        JLabel bannerLabel = new JLabel(new ImageIcon(scaledImg));
        bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bannerLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, -10, 0));
        add(bannerLabel, BorderLayout.NORTH);

        // ---------------------------
        // Center Login Form
        // ---------------------------
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240,240,240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        // ---------------------------
        // Login Button (smaller, gray, simple hover)
        // ---------------------------
        JButton loginButton = new JButton("Login") {
            @Override
            public void setContentAreaFilled(boolean b) {
                super.setContentAreaFilled(true);
            }
        };

        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14)); // smaller font
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(95, 95, 95)); // gray
        loginButton.setFocusable(false);
        loginButton.setPreferredSize(new Dimension(160, 40)); // smaller size
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Simple hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(120, 120, 120));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(95, 95, 95));
            }
        });

        // Add to form panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        formPanel.add(loginButton, gbc);


        add(formPanel, BorderLayout.CENTER);

        // ---------------------------
        // Login Action
        // ---------------------------
        loginButton.addActionListener(e -> {
            String email = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            Employee emp = hotelDB.findEmployeeByEmail(email);

            if (email.equalsIgnoreCase("admin@hotel.com") && password.equals("admin123")) {
                frame.setContentPane(new AdminPanel(frame));}
            else if (email.equalsIgnoreCase("manager@hotel.com") && password.equals("admin123")) {
                frame.setContentPane(new ManagerPanel(frame));}
            else if (email.equalsIgnoreCase("recep@hotel.com") && password.equals("admin123")) {
                frame.setContentPane(new ReceptionistPanel(frame));}
            else if (emp != null && emp.getPassword().equals(password)) {
                switch (emp.getJobTitle().toLowerCase()) {
                    case "admin" -> frame.setContentPane(new AdminPanel(frame));
                    case "manager" -> frame.setContentPane(new ManagerPanel(frame));
                    case "receptionist" -> frame.setContentPane(new ReceptionistPanel(frame));
                    default -> JOptionPane.showMessageDialog(this, "Unknown role!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            frame.revalidate();
            frame.repaint();
        });
    }
}
