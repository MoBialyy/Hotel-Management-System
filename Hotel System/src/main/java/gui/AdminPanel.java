package main.java.gui;
import main.java.code.HotelManagement;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

public class AdminPanel extends JPanel {

    private HotelManagement hotelManagement = new HotelManagement();
    public AdminPanel(JFrame frame) {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Role
        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0; gbc.gridy = 0;
        add(roleLabel, gbc);

        String[] roles = {"Manager", "Receptionist"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        gbc.gridx = 1;
        add(roleBox, gbc);

        // First Name
        JLabel firstNameLabel = new JLabel("First Name:");
        gbc.gridx = 0; gbc.gridy = 1;
        add(firstNameLabel, gbc);

        JTextField firstNameField = new JTextField(15);
        gbc.gridx = 1;
        add(firstNameField, gbc);

        // Last Name
        JLabel lastNameLabel = new JLabel("Last Name:");
        gbc.gridx = 0; gbc.gridy = 2;
        add(lastNameLabel, gbc);

        JTextField lastNameField = new JTextField(15);
        gbc.gridx = 1;
        add(lastNameField, gbc);

        // Age
        JLabel ageLabel = new JLabel("Age:");
        gbc.gridx = 0; gbc.gridy = 3;
        add(ageLabel, gbc);

        JTextField ageField = new JTextField(15);
        gbc.gridx = 1;
        add(ageField, gbc);

        // Salary
        JLabel salaryLabel = new JLabel("Salary:");
        gbc.gridx = 0; gbc.gridy = 4;
        add(salaryLabel, gbc);

        JTextField salaryField = new JTextField(15);
        gbc.gridx = 1;
        add(salaryField, gbc);

        // Birth Place (dropdown)
        JLabel birthPlaceLabel = new JLabel("Birth Place:");
        gbc.gridx = 0; gbc.gridy = 5;
        add(birthPlaceLabel, gbc);

        String[] countries = {"Cairo", "Alexandria", "Giza", "Shubra El Kheima","Port Said",
            "Suez", "Luxor", "Aswan", "Mansoura", "Tanta", "Fayoum", "Ismailia", "Zagazig",
            "Damietta", "Damanhur", "Kafr El Sheikh", "El Mahalla El Kubra", "Hurghada",
            "Al-Minya", "Beni Suef", "Sohag", "Qena", "El Arish", "Shibin El Kom",
            "Banha", "6th of October City", "Helwan"
        };

        JComboBox<String> birthPlaceBox = new JComboBox<>(countries);
        gbc.gridx = 1;
        add(birthPlaceBox, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0; gbc.gridy = 6;
        add(emailLabel, gbc);

        JTextField emailField = new JTextField(15);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Phone
        JLabel phoneLabel = new JLabel("Phone Number:");
        gbc.gridx = 0; gbc.gridy = 7;
        add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(15);
        gbc.gridx = 1;
        add(phoneField, gbc);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        gbc.gridx = 0; gbc.gridy = 8;
        add(addressLabel, gbc);

        JTextField addressField = new JTextField(15);
        gbc.gridx = 1;
        add(addressField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 9;
        add(passwordLabel, gbc);

        JTextField passwordField = new JTextField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Add User Button
        JButton addButton = new JButton("Add User");
        gbc.gridx = 0; gbc.gridy = 10;
        gbc.gridwidth = 2;
        add(addButton, gbc);

        addButton.addActionListener((ActionEvent e) -> {
            try {
                String role = (String) roleBox.getSelectedItem();
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String ageStr = ageField.getText().trim();
                String salaryStr = salaryField.getText().trim();
                String birthPlace = (String) birthPlaceBox.getSelectedItem();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                String password = passwordField.getText().trim();

                // ---------------- Validation ----------------
                if(firstName.isEmpty() || firstName.contains(" ")) {
                    JOptionPane.showMessageDialog(this, "First name must be one word.");
                    return;
                }

                if(lastName.isEmpty() || lastName.contains(" ")) {
                    JOptionPane.showMessageDialog(this, "Last name must be one word.");
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageStr);
                    if(age < 18 || age > 65) {
                        JOptionPane.showMessageDialog(this, "Age must be between 18 and 65.");
                        return;
                    }
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Age must be an integer.");
                    return;
                }

                int salary;
                try {
                    salary = Integer.parseInt(salaryStr);
                    if(salary < 0) {
                        JOptionPane.showMessageDialog(this, "Salary must be positive.");
                        return;
                    }
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Salary must be an integer.");
                    return;
                }

                if(!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$", email)) {
                    JOptionPane.showMessageDialog(this, "Invalid email format.");
                    return;
                }

                if(!Pattern.matches("^\\+?\\d{7,15}$", phone)) {
                    JOptionPane.showMessageDialog(this, "Phone must be 7-15 digits (optionally start with +).");
                    return;
                }

                if(address.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Address cannot be empty.");
                    return;
                }

                if(password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Password cannot be empty.");
                    return;
                }

                // ---------------- Create User ----------------
                if(role.equals("Manager")) {
                    hotelManagement.createManager(firstName, lastName, age, salary, "Manager",
                            birthPlace, email, phone, address, password);
                } else {
                    hotelManagement.createReceptionist(firstName, lastName, age, salary, "Receptionist",
                            birthPlace, email, phone, address, password);
                }

                JOptionPane.showMessageDialog(this, role + " added successfully!");

                // Clear fields
                firstNameField.setText(""); lastNameField.setText("");
                ageField.setText(""); salaryField.setText("");
                emailField.setText(""); phoneField.setText("");
                addressField.setText(""); passwordField.setText("");

                // ---------------- Redirect to Welcome Page ----------------
                frame.setContentPane(new WelcomePanel(frame));
                frame.revalidate();
                frame.repaint();

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
            }
        });
    }
}
