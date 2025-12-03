package main.java.gui;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import main.java.code.BoardingOption;
import main.java.code.Booking;
import main.java.code.HotelManagement;
import main.java.code.Resident;
import main.java.code.Room;

public class NewBookingPanel extends JPanel {
    
    private HotelManagement hotelMgmt = new HotelManagement();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Guest Information Fields
    private JTextField firstNameField, lastNameField, ageField;
    private JTextField nationalityField, emailField, phoneField;
    private JTextField addressField, govIdField;
    private JComboBox<String> existingGuestCombo;
    private JRadioButton newGuestRadio, existingGuestRadio;
    
    // Booking Information Fields
    private JTextField checkInField, nightsField;
    private JComboBox<String> roomTypeCombo, boardingCombo;
    private JRadioButton bookByTypeRadio, bookByNumberRadio;
    private JTextField roomNumberField;
    private JLabel checkOutLabel;
    
    // Available Rooms Display
    private JTextArea availableRoomsArea;

    public NewBookingPanel(JFrame frame) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------------- Top Panel ----------------
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            frame.setContentPane(new ReceptionistPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        topPanel.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("New Booking", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- Main Content (Scrollable) ----------------
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Guest Selection Section
        mainPanel.add(createGuestSelectionPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Guest Information Section
        mainPanel.add(createGuestInfoPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Booking Information Section
        mainPanel.add(createBookingInfoPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Available Rooms Section
        mainPanel.add(createAvailableRoomsPanel());
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // ---------------- Bottom Panel (Action Buttons) ----------------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton checkAvailabilityBtn = new JButton("Check Availability");
        checkAvailabilityBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkAvailabilityBtn.addActionListener(e -> checkAvailability());
        bottomPanel.add(checkAvailabilityBtn);
        
        JButton createBookingBtn = new JButton("Create Booking");
        createBookingBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        createBookingBtn.setBackground(new Color(46, 204, 113));
        createBookingBtn.setForeground(Color.WHITE);
        createBookingBtn.addActionListener(e -> createBooking());
        bottomPanel.add(createBookingBtn);
        
        JButton clearBtn = new JButton("Clear Form");
        clearBtn.addActionListener(e -> clearForm());
        bottomPanel.add(clearBtn);

        add(bottomPanel, BorderLayout.SOUTH);
        
        // Initialize form
        toggleGuestFields();
    }

    private JPanel createGuestSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Guest Selection"));
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newGuestRadio = new JRadioButton("New Guest", true);
        existingGuestRadio = new JRadioButton("Existing Guest");
        
        ButtonGroup guestGroup = new ButtonGroup();
        guestGroup.add(newGuestRadio);
        guestGroup.add(existingGuestRadio);
        
        newGuestRadio.addActionListener(e -> toggleGuestFields());
        existingGuestRadio.addActionListener(e -> toggleGuestFields());
        
        radioPanel.add(newGuestRadio);
        radioPanel.add(existingGuestRadio);
        panel.add(radioPanel);
        
        // Existing Guest Selection
        JPanel existingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        existingPanel.add(new JLabel("Select Guest:"));
        
        existingGuestCombo = new JComboBox<>();
        loadExistingGuests();
        existingGuestCombo.setPreferredSize(new Dimension(300, 25));
        existingGuestCombo.addActionListener(e -> loadGuestInfo());
        existingPanel.add(existingGuestCombo);
        
        panel.add(existingPanel);
        
        return panel;
    }

    private JPanel createGuestInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Guest Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0: First Name and Last Name
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
        
        // Row 1: Age and Nationality
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
        
        // Row 2: Email and Phone
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
        
        // Row 3: Address and Gov ID
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

    private JPanel createBookingInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Booking Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0: Room Selection Type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Book by:"), gbc);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookByTypeRadio = new JRadioButton("Room Type", true);
        bookByNumberRadio = new JRadioButton("Room Number");
        
        ButtonGroup bookGroup = new ButtonGroup();
        bookGroup.add(bookByTypeRadio);
        bookGroup.add(bookByNumberRadio);
        
        bookByTypeRadio.addActionListener(e -> toggleRoomSelection());
        bookByNumberRadio.addActionListener(e -> toggleRoomSelection());
        
        radioPanel.add(bookByTypeRadio);
        radioPanel.add(bookByNumberRadio);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(radioPanel, gbc);
        
        // Row 1: Room Type or Room Number
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Triple"});
        panel.add(roomTypeCombo, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 3;
        roomNumberField = new JTextField(20);
        roomNumberField.setEnabled(false);
        panel.add(roomNumberField, gbc);
        
        // Row 2: Check-in and Nights
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Check-In (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        checkInField = new JTextField(LocalDate.now().format(formatter), 20);
        checkInField.addActionListener(e -> calculateCheckOut());
        panel.add(checkInField, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Number of Nights:"), gbc);
        gbc.gridx = 3;
        nightsField = new JTextField("1", 20);
        nightsField.addActionListener(e -> calculateCheckOut());
        panel.add(nightsField, gbc);
        
        // Row 3: Check-out (calculated)
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Check-Out:"), gbc);
        gbc.gridx = 1;
        checkOutLabel = new JLabel(LocalDate.now().plusDays(1).format(formatter));
        checkOutLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(checkOutLabel, gbc);
        
        // Row 4: Boarding Option
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Boarding Option:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        boardingCombo = new JComboBox<>(new String[]{
            "ROOM_ONLY", "BED_AND_BREAKFAST", "HALF_BOARD", "FULL_BOARD"
        });
        panel.add(boardingCombo, gbc);
        
        return panel;
    }

    private JPanel createAvailableRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Available Rooms"));
        
        availableRoomsArea = new JTextArea(8, 50);
        availableRoomsArea.setEditable(false);
        availableRoomsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(availableRoomsArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadExistingGuests() {
        existingGuestCombo.removeAllItems();
        existingGuestCombo.addItem("-- Select a Guest --");
        
        for (Resident resident : hotelMgmt.getResidents()) {
            String displayText = String.format("%s (ID: %d, Phone: %s)", 
                resident.getName(), resident.getId(), resident.getPhoneNumber());
            existingGuestCombo.addItem(displayText);
        }
    }

    private void loadGuestInfo() {
        if (existingGuestRadio.isSelected() && existingGuestCombo.getSelectedIndex() > 0) {
            int selectedIndex = existingGuestCombo.getSelectedIndex() - 1;
            Resident resident = hotelMgmt.getResidents().get(selectedIndex);
            
            firstNameField.setText(resident.getFirstName());
            lastNameField.setText(resident.getLastName());
            ageField.setText(String.valueOf(resident.getAge()));
            nationalityField.setText(resident.getNationality());
            emailField.setText(resident.getEmail());
            phoneField.setText(resident.getPhoneNumber());
            addressField.setText(resident.getAddress());
            govIdField.setText(resident.getGovernmentId());
        }
    }

    private void toggleGuestFields() {
        boolean isNewGuest = newGuestRadio.isSelected();
        
        existingGuestCombo.setEnabled(!isNewGuest);
        
        firstNameField.setEditable(isNewGuest);
        lastNameField.setEditable(isNewGuest);
        ageField.setEditable(isNewGuest);
        nationalityField.setEditable(isNewGuest);
        emailField.setEditable(isNewGuest);
        phoneField.setEditable(isNewGuest);
        addressField.setEditable(isNewGuest);
        govIdField.setEditable(isNewGuest);
        
        if (isNewGuest) {
            clearGuestFields();
        }
    }

    private void toggleRoomSelection() {
        boolean byType = bookByTypeRadio.isSelected();
        roomTypeCombo.setEnabled(byType);
        roomNumberField.setEnabled(!byType);
    }

    private void calculateCheckOut() {
        try {
            LocalDate checkIn = LocalDate.parse(checkInField.getText().trim(), formatter);
            int nights = Integer.parseInt(nightsField.getText().trim());
            
            if (nights < 1) {
                JOptionPane.showMessageDialog(this, 
                    "Number of nights must be at least 1.", 
                    "Invalid Input", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate checkOut = checkIn.plusDays(nights);
            checkOutLabel.setText(checkOut.format(formatter));
        } catch (DateTimeParseException e) {
            checkOutLabel.setText("Invalid Date");
        } catch (NumberFormatException e) {
            checkOutLabel.setText("Invalid Nights");
        }
    }

    private void checkAvailability() {
        try {
            LocalDate checkIn = LocalDate.parse(checkInField.getText().trim(), formatter);
            int nights = Integer.parseInt(nightsField.getText().trim());
            LocalDate checkOut = checkIn.plusDays(nights);
            
            List<Room> availableRooms = hotelMgmt.getAvailableRooms(checkIn, checkOut);
            
            availableRoomsArea.setText("");
            availableRoomsArea.append(String.format("Available Rooms from %s to %s:\n\n", 
                checkIn.format(formatter), checkOut.format(formatter)));
            
            if (availableRooms.isEmpty()) {
                availableRoomsArea.append("No rooms available for these dates.\n");
            } else {
                availableRoomsArea.append(String.format("%-10s %-15s %-10s %-15s\n", 
                    "Room #", "Type", "Price", "Max Guests"));
                availableRoomsArea.append("=".repeat(60) + "\n");
                
                for (Room room : availableRooms) {
                    availableRoomsArea.append(String.format("%-10d %-15s $%-9.2f %-15d\n",
                        room.getRoomNumber(), room.getType(), room.getPrice(), room.getMaxResidents()));
                }
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid date format. Use yyyy-MM-dd.", 
                "Date Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid number of nights.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createBooking() {
        try {
            // Validate and get guest
            Resident resident = getOrCreateResident();
            if (resident == null) return;
            
            // Validate booking dates
            LocalDate checkIn = LocalDate.parse(checkInField.getText().trim(), formatter);
            int nights = Integer.parseInt(nightsField.getText().trim());
            
            if (checkIn.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, 
                    "Check-in date cannot be in the past.", 
                    "Invalid Date", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (nights < 1) {
                JOptionPane.showMessageDialog(this, 
                    "Number of nights must be at least 1.", 
                    "Invalid Input", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Get boarding option
            String boardingStr = (String) boardingCombo.getSelectedItem();
            BoardingOption boarding = BoardingOption.valueOf(boardingStr);
            
            // Create booking
            boolean success;
            if (bookByTypeRadio.isSelected()) {
                String roomType = (String) roomTypeCombo.getSelectedItem();
                success = hotelMgmt.bookRoomByType(resident, roomType, checkIn, nights, boarding);
            } else {
                int roomNumber = Integer.parseInt(roomNumberField.getText().trim());
                success = hotelMgmt.bookRoomByNumber(resident, roomNumber, checkIn, nights, boarding);
            }
            
            if (success) {
                // Get the last booking created to show the total price
                Booking lastBooking = hotelMgmt.getBookings(resident).get(hotelMgmt.getBookings(resident).size() - 1);
                
                JOptionPane.showMessageDialog(this, 
                    "Booking created successfully!\n\n" +
                    "Room: " + lastBooking.getRoom().getRoomNumber() + " (" + lastBooking.getRoom().getType() + ")\n" +
                    "Guest: " + resident.getName() + "\n" +
                    "Check-in: " + checkIn.format(formatter) + "\n" +
                    "Nights: " + nights + "\n" +
                    "Boarding: " + boarding + "\n" +
                    "Total Cost: $" + String.format("%.2f", lastBooking.getTotalPrice()), 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to create booking. Room may not be available.", 
                    "Booking Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid date format. Use yyyy-MM-dd.", 
                "Date Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid number input.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error creating booking: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private Resident getOrCreateResident() {
        if (existingGuestRadio.isSelected()) {
            if (existingGuestCombo.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please select an existing guest.", 
                    "No Guest Selected", 
                    JOptionPane.WARNING_MESSAGE);
                return null;
            }
            int selectedIndex = existingGuestCombo.getSelectedIndex() - 1;
            return hotelMgmt.getResidents().get(selectedIndex);
        } else {
            // Validate new guest fields
            if (firstNameField.getText().trim().isEmpty() || 
                lastNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty() ||
                govIdField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all required guest fields.", 
                    "Missing Information", 
                    JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
            try {
                int age = Integer.parseInt(ageField.getText().trim());
                
                // Create new resident directly
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
                hotelMgmt.addResident(newResident);
                return newResident;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid age value.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    }

    private void clearForm() {
        // Reset guest selection
        newGuestRadio.setSelected(true);
        existingGuestCombo.setSelectedIndex(0);
        clearGuestFields();
        
        // Reset booking fields
        bookByTypeRadio.setSelected(true);
        roomTypeCombo.setSelectedIndex(0);
        roomNumberField.setText("");
        checkInField.setText(LocalDate.now().format(formatter));
        nightsField.setText("1");
        checkOutLabel.setText(LocalDate.now().plusDays(1).format(formatter));
        boardingCombo.setSelectedIndex(0);
        availableRoomsArea.setText("");
        
        toggleGuestFields();
        toggleRoomSelection();
    }

    private void clearGuestFields() {
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