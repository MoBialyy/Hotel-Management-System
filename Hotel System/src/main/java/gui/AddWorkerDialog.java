package main.java.gui;

import javax.swing.*;
import java.awt.*;
import main.java.code.Worker;

public class AddWorkerDialog extends JDialog {

    private JTextField firstNameField = new JTextField(15);
    private JTextField lastNameField = new JTextField(15);
    private JTextField ageField = new JTextField(5);
    private JTextField salaryField = new JTextField(10);
    private JComboBox<String> jobTitleField;
    private JComboBox<String> birthPlaceField;
    private JTextField emailField = new JTextField(20);
    private JTextField phoneField = new JTextField(15);
    private JTextField addressField = new JTextField(20);
    private JTextField passwordField = new JTextField(15);

    private JButton saveBtn;
    private boolean saved = false;
    private Worker workerToEdit; // null if adding

    private final String[] jobTitles = {
        "Housekeeping", "Chef", "Waiter", "Concierge", "Bellboy", "Maintenance", "Security"
    };

    private final String[] birthPlaces = {
        "Cairo", "Alexandria", "Giza", "Shubra El Kheima","Port Said",
        "Suez", "Luxor", "Aswan", "Mansoura", "Tanta", "Fayoum", "Ismailia", "Zagazig",
        "Damietta", "Damanhur", "Kafr El Sheikh", "El Mahalla El Kubra", "Hurghada",
        "Al-Minya", "Beni Suef", "Sohag", "Qena", "El Arish", "Shibin El Kom",
        "Banha", "6th of October City", "Helwan"
    };

    // ---------------- Add Mode Constructor ----------------
    public AddWorkerDialog(JFrame parent) {
        super(parent, "Add New Worker", true);
        initUI();
    }

    // ---------------- Edit Mode Constructor ----------------
    public AddWorkerDialog(JFrame parent, Worker worker) {
        super(parent, "Edit Worker", true);
        this.workerToEdit = worker;
        initUI();

        // Pre-fill fields with worker info
        firstNameField.setText(worker.getFirstName());
        lastNameField.setText(worker.getLastName());
        ageField.setText(String.valueOf(worker.getAge()));
        salaryField.setText(String.valueOf(worker.getSalary()));
        jobTitleField.setSelectedItem(worker.getJobTitle());
        birthPlaceField.setSelectedItem(worker.getBirthPlace());
        emailField.setText(worker.getEmail());
        phoneField.setText(worker.getPhoneNumber());
        addressField.setText(worker.getAddress());
        passwordField.setText(worker.getPassword());

        saveBtn.setText("Save Changes");
    }

    // ---------------- Initialize UI ----------------
    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        jobTitleField = new JComboBox<>(jobTitles);
        birthPlaceField = new JComboBox<>(birthPlaces);

        // Input panel with 2 columns
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.add(new JLabel("First Name:")); inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:")); inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Age:")); inputPanel.add(ageField);
        inputPanel.add(new JLabel("Salary:")); inputPanel.add(salaryField);
        inputPanel.add(new JLabel("Job Title:")); inputPanel.add(jobTitleField);
        inputPanel.add(new JLabel("Birth Place:")); inputPanel.add(birthPlaceField);
        inputPanel.add(new JLabel("Email:")); inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:")); inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Address:")); inputPanel.add(addressField);
        inputPanel.add(new JLabel("Password:")); inputPanel.add(passwordField);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // left/right margin
        wrapper.add(inputPanel, BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        saveBtn = new JButton(workerToEdit == null ? "Add Worker" : "Save Changes");
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
        // First/Last name
        if (firstNameField.getText().trim().isEmpty() || firstNameField.getText().contains(" ")) {
            JOptionPane.showMessageDialog(this, "First name must be one word.");
            return false;
        }
        if (lastNameField.getText().trim().isEmpty() || lastNameField.getText().contains(" ")) {
            JOptionPane.showMessageDialog(this, "Last name must be one word.");
            return false;
        }

        // Age
        int age;
        try {
            age = Integer.parseInt(ageField.getText().trim());
            if (age < 18 || age > 65) {
                JOptionPane.showMessageDialog(this, "Age must be between 18 and 65.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be an integer.");
            return false;
        }

        // Salary
        double salary;
        try {
            salary = Double.parseDouble(salaryField.getText().trim());
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary must be positive.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salary must be a number.");
            return false;
        }

        // Address & Password
        if (addressField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address cannot be empty.");
            return false;
        }
        if (passwordField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.");
            return false;
        }

        // Email
        if (!emailField.getText().trim().matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return false;
        }

        // Phone
        if (!phoneField.getText().trim().matches("^\\+?\\d{7,15}$")) {
            JOptionPane.showMessageDialog(this, "Phone must be 7-15 digits (optionally start with +).");
            return false;
        }

        return true;
    }

    // ---------------- Getters ----------------
    public boolean isSaved() { return saved; }
    public String getFirstName() { return firstNameField.getText().trim(); }
    public String getLastName() { return lastNameField.getText().trim(); }
    public int getAge() { return Integer.parseInt(ageField.getText().trim()); }
    public double getSalary() { return Double.parseDouble(salaryField.getText().trim()); }
    public String getJobTitle() { return (String) jobTitleField.getSelectedItem(); }
    public String getBirthPlace() { return (String) birthPlaceField.getSelectedItem(); }
    public String getEmail() { return emailField.getText().trim(); }
    public String getPhone() { return phoneField.getText().trim(); }
    public String getAddress() { return addressField.getText().trim(); }
    public String getPassword() { return passwordField.getText().trim(); }
}
