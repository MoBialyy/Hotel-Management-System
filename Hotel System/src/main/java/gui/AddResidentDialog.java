package main.java.gui;

import main.java.code.Resident;

import javax.swing.*;
import java.awt.*;

public class AddResidentDialog extends JDialog {

    private JTextField firstNameField = new JTextField(15);
    private JTextField lastNameField = new JTextField(15);
    private JTextField ageField = new JTextField(5);
    private JComboBox<String> nationalityField;
    private JTextField emailField = new JTextField(20);
    private JTextField phoneField = new JTextField(15);
    private JTextField addressField = new JTextField(20);
    private JTextField govIdField = new JTextField(15);

    private JButton saveBtn;
    private boolean saved = false;
    private Resident residentToEdit; // null if adding

    private final String[] countries = {
            "Egypt", "USA", "UK", "Germany", "France", "Italy", "Spain",
            "Canada", "Australia", "India", "China", "Japan"
    };

    // ---------------- Add Mode Constructor ----------------
    public AddResidentDialog(JFrame parent) {
        super(parent, "Add New Resident", true);
        initUI();
    }

    // ---------------- Edit Mode Constructor ----------------
    public AddResidentDialog(JFrame parent, Resident resident) {
        super(parent, "Edit Resident", true);
        this.residentToEdit = resident;
        initUI();

        // Pre-fill fields
        firstNameField.setText(resident.getFirstName());
        lastNameField.setText(resident.getLastName());
        ageField.setText(String.valueOf(resident.getAge()));
        nationalityField.setSelectedItem(resident.getNationality());
        emailField.setText(resident.getEmail());
        phoneField.setText(resident.getPhoneNumber());
        addressField.setText(resident.getAddress());
        govIdField.setText(resident.getGovernmentId());

        saveBtn.setText("Save Changes");
    }

    // ---------------- Initialize UI ----------------
    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        nationalityField = new JComboBox<>(countries);
        nationalityField.setSelectedItem("Egypt");

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.add(new JLabel("First Name:")); inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:")); inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Age:")); inputPanel.add(ageField);
        inputPanel.add(new JLabel("Nationality:")); inputPanel.add(nationalityField);
        inputPanel.add(new JLabel("Email:")); inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:")); inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Address:")); inputPanel.add(addressField);
        inputPanel.add(new JLabel("Government ID:")); inputPanel.add(govIdField);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        wrapper.add(inputPanel, BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);

        // ---------------- Buttons ----------------
        JPanel buttonPanel = new JPanel();
        saveBtn = new JButton(residentToEdit == null ? "Add Resident" : "Save Changes");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ---------------- Button Actions ----------------
        saveBtn.addActionListener(e -> {
            if (validateFields()) {
                saved = true;
                dispose();
            }
        });
        cancelBtn.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(getParent());
    }

    // ---------------- Field Validation ----------------
    private boolean validateFields() {
        // Names
        if (firstNameField.getText().trim().isEmpty() || !firstNameField.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(this, "Invalid first name. Only letters allowed.");
            return false;
        }
        if (lastNameField.getText().trim().isEmpty() || !lastNameField.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(this, "Invalid last name. Only letters allowed.");
            return false;
        }

        // Age
        int age;
        try {
            age = Integer.parseInt(ageField.getText().trim());
            if (age <= 0 || age > 120) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid age. Enter a number between 1 and 120.");
            return false;
        }

        // Email
        String email = emailField.getText().trim();
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return false;
        }

        // Phone
        String phone = phoneField.getText().trim();
        if (!phone.matches("\\d{7,15}")) {
            JOptionPane.showMessageDialog(this, "Phone must be 7-15 digits.");
            return false;
        }

        // Address
        if (addressField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address cannot be empty.");
            return false;
        }

        // Government ID
        String govId = govIdField.getText().trim();
        if (!govId.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Government ID must be digits only.");
            return false;
        }

        return true;
    }

    // ---------------- Getters ----------------
    public boolean isSaved() { return saved; }
    public String getFirstName() { return firstNameField.getText().trim(); }
    public String getLastName() { return lastNameField.getText().trim(); }
    public int getAge() { return Integer.parseInt(ageField.getText().trim()); }
    public String getNationality() { return (String) nationalityField.getSelectedItem(); }
    public String getEmail() { return emailField.getText().trim(); }
    public String getPhone() { return phoneField.getText().trim(); }
    public String getAddress() { return addressField.getText().trim(); }
    public String getGovernmentId() { return govIdField.getText().trim(); }

    // ---------------- Resident Getter ----------------
    public Resident getResident() {
        return new Resident(
                getFirstName(),
                getLastName(),
                getAge(),
                getNationality(),
                getEmail(),
                getPhone(),
                getAddress(),
                getGovernmentId()
        );
    }
}
