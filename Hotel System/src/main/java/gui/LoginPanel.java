package main.java.gui;
import main.java.code.EmailSender;
import main.java.code.Employee;
import javax.mail.MessagingException;
import main.java.code.HotelManagement;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    //private HotelDB hotelDB = HotelDB.getInstance();
    private HotelManagement hotelmg = new HotelManagement();

    public LoginPanel(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(240,240,240));

        // ---------------------------
        // Top Banner
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
        // Login Button
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
        // Forgot Password Button
        // ---------------------------
        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setBorderPainted(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setFocusPainted(false);
        forgotBtn.setForeground(new Color(30, 100, 200)); // link-like blue
        forgotBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // hover effect
        forgotBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                forgotBtn.setForeground(new Color(20, 80, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                forgotBtn.setForeground(new Color(30, 100, 200));
            }
        });

        // add it under the login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(forgotBtn, gbc);

        // action on click
        forgotBtn.addActionListener(e -> {
            // Ask for employee email
            String email = JOptionPane.showInputDialog(
                    this,
                    "Enter your registered email:",
                    "Reset Password",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (email == null || email.isBlank()) {
                return; // user canceled
            }

            // Check if employee exists
            Employee emp = hotelmg.findEmployeeByEmail(email);
            if (emp == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No employee found with this email.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Generate 6-digit code
            String code = String.valueOf((int)(Math.random() * 900000) + 100000);

            try {
                // Send code
                EmailSender.sendCode(email, code);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to send email:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Ask user to enter verification code
            String userCode = JOptionPane.showInputDialog(
                    this,
                    "A verification code was sent to your email.\nEnter the code:",
                    "Verify Code",
                    JOptionPane.QUESTION_MESSAGE
            );

            // User canceled
            if (userCode == null) return;

            // Check code
            if (!userCode.equals(code)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Incorrect code.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Let user set new password
            String newPass = JOptionPane.showInputDialog(
                    this,
                    "Enter your new password:",
                    "Reset Password",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (newPass == null || newPass.isBlank()) return;

            // Update password
            hotelmg.setEmployeeNewPassword(emp, newPass);
            JOptionPane.showMessageDialog(
                    this,
                    "Password reset successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        // ---------------------------
        // Login Action
        // ---------------------------
        loginButton.addActionListener(e -> {
            String email = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            Employee emp = hotelmg.findEmployeeByEmail(email);

            // Hardcoded admin credentials
            if (email.equalsIgnoreCase("admin@hotel.com") && password.equals("admin123")) {
                frame.setContentPane(new AdminPanel(frame));}
            // Check employee credentials
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
