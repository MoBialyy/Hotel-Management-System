package main.java.gui;

import main.java.code.*;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddResidentToBookingPanel extends JPanel {
    private HotelDB db = HotelDB.getInstance();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private JComboBox<String> bookingCombo;
    private JTextField firstNameField, lastNameField, ageField;
    private JTextField nationalityField, emailField, phoneField;
    private JTextField addressField, govIdField;
    private JLabel roomInfoLabel, capacityLabel;
    private Booking selectedBooking;

    public AddResidentToBookingPanel(JFrame frame) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            frame.setContentPane(new ReceptionistPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        topPanel.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Add Resident to Room", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Booking Selection
        JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bookingPanel.setBorder(BorderFactory.createTitledBorder("Select Active Booking"));
        bookingPanel.add(new JLabel("Booking:"));
        bookingCombo = new JComboBox<>();
        bookingCombo.setPreferredSize(new Dimension(400, 25));
        bookingCombo.addActionListener(e -> loadBookingInfo());
        bookingPanel.add(bookingCombo);
        mainPanel.add(bookingPanel);

        // Room Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Room Information"));
        roomInfoLabel = new JLabel("Select a booking to see room details");
        capacityLabel = new JLabel("");
        infoPanel.add(roomInfoLabel);
        infoPanel.add(capacityLabel);
        mainPanel.add(infoPanel);

        // Guest Info
        mainPanel.add(createGuestInfoPanel());

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addBtn = new JButton("Add Resident to Room");
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> addResident());
        bottomPanel.add(addBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearFields());
        bottomPanel.add(clearBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        loadActiveBookings();
    }

    private JPanel createGuestInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("New Resident Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(20);
        panel.add(firstNameField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        lastNameField = new JTextField(20);
        panel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(20);
        panel.add(ageField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Nationality:"), gbc);
        gbc.gridx = 3;
        nationalityField = new JTextField(20);
        panel.add(nationalityField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3;
        phoneField = new JTextField(20);
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(20);
        panel.add(addressField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Government ID:"), gbc);
        gbc.gridx = 3;
        govIdField = new JTextField(20);
        panel.add(govIdField, gbc);

        return panel;
    }

    private void loadActiveBookings() {
        bookingCombo.removeAllItems();
        bookingCombo.addItem("-- Select a Booking --");
        
        List<Booking> bookings = db.getBookings();
        for (Booking b : bookings) {
            if (!b.isCheckedOut()) {
                Room room = b.getRoom();
                // Only show bookings for rooms with capacity > 1
                if (room.getMaxResidents() > 1) {
                    String display = String.format("Room %d (%s) - %s - %s to %s",
                        room.getRoomNumber(),
                        room.getType(),
                        b.getResident().getName(),
                        b.getCheckInDate().format(formatter),
                        b.getCheckOutDate().format(formatter));
                    bookingCombo.addItem(display);
                }
            }
        }
    }

    private void loadBookingInfo() {
        if (bookingCombo.getSelectedIndex() <= 0) {
            roomInfoLabel.setText("Select a booking to see room details");
            capacityLabel.setText("");
            selectedBooking = null;
            return;
        }

        // Find selected booking
        int selectedIndex = bookingCombo.getSelectedIndex() - 1;
        int currentIndex = 0;
        for (Booking b : db.getBookings()) {
            if (!b.isCheckedOut() && b.getRoom().getMaxResidents() > 1) {
                if (currentIndex == selectedIndex) {
                    selectedBooking = b;
                    break;
                }
                currentIndex++;
            }
        }

        if (selectedBooking != null) {
            Room room = selectedBooking.getRoom();
            roomInfoLabel.setText(String.format("Room %d - Type: %s - Price: $%.2f/night",
                room.getRoomNumber(), room.getType(), room.getPrice()));
            
            // Count current residents in this booking period
            int currentResidents = 0;
            for (Booking b : room.getBookings()) {
                if (!b.isCheckedOut() && b.overlaps(selectedBooking.getCheckInDate(), selectedBooking.getCheckOutDate())) {
                    currentResidents++;
                }
            }
            
            int available = room.getMaxResidents() - currentResidents;
            capacityLabel.setText(String.format("Capacity: %d / %d occupied - %d space(s) available",
                currentResidents, room.getMaxResidents(), available));
            
            if (available <= 0) {
                capacityLabel.setForeground(Color.RED);
            } else {
                capacityLabel.setForeground(new Color(46, 204, 113));
            }
        }
    }

    private void addResident() {
        if (selectedBooking == null || bookingCombo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate fields
        if (firstNameField.getText().trim().isEmpty() || 
            lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            phoneField.getText().trim().isEmpty() ||
            govIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", 
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check capacity
        Room room = selectedBooking.getRoom();
        int currentResidents = 0;
        for (Booking b : room.getBookings()) {
            if (!b.isCheckedOut() && b.overlaps(selectedBooking.getCheckInDate(), selectedBooking.getCheckOutDate())) {
                currentResidents++;
            }
        }

        if (currentResidents >= room.getMaxResidents()) {
            JOptionPane.showMessageDialog(this, 
                "This room is at full capacity. Cannot add more residents.", 
                "Room Full", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int age = Integer.parseInt(ageField.getText().trim());

            // Create new resident
            Resident newResident = new Resident(
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                age,
                nationalityField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                addressField.getText().trim(),
                govIdField.getText().trim()
            );
            db.addResident(newResident);

            // Create booking for the same room and dates
            Booking newBooking = new Booking(
                newResident,
                selectedBooking.getRoom(),
                selectedBooking.getCheckInDate(),
                selectedBooking.getNights(),
                selectedBooking.getBoarding()
            );
            db.addBooking(newBooking);

            JOptionPane.showMessageDialog(this, 
                String.format("Resident %s added successfully to Room %d!\n" +
                    "Check-In: %s\nCheck-Out: %s\nTotal Cost: $%.2f",
                    newResident.getName(),
                    room.getRoomNumber(),
                    newBooking.getCheckInDate().format(formatter),
                    newBooking.getCheckOutDate().format(formatter),
                    newBooking.getTotalPrice()),
                "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            loadActiveBookings();
            loadBookingInfo();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid age value.", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        ageField.setText("");
        nationalityField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        govIdField.setText("");
    }
}