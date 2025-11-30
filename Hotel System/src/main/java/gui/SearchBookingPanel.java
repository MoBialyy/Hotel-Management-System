package main.java.gui;

import main.java.code.HotelDB;
import main.java.code.Booking;
import main.java.code.Resident;
import main.java.code.Room;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import main.java.code.HotelManagement;

public class SearchBookingPanel extends JPanel {
    private HotelDB db = HotelDB.getInstance();
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JComboBox<String> statusFilterCombo;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public SearchBookingPanel(JFrame frame) {
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

        JLabel title = new JLabel("Search Bookings", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- Search Panel ----------------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Criteria"));

        searchPanel.add(new JLabel("Search by:"));
        searchTypeCombo = new JComboBox<>(new String[]{
            "Guest Name", "Room Number", "Guest ID", "Phone Number"
        });
        searchPanel.add(searchTypeCombo);

        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchBtn);

        searchPanel.add(Box.createHorizontalStrut(20));

        searchPanel.add(new JLabel("Status Filter:"));
        statusFilterCombo = new JComboBox<>(new String[]{
            "All", "Active", "Upcoming", "Completed", "Checked Out"
        });
        statusFilterCombo.addActionListener(e -> performSearch());
        searchPanel.add(statusFilterCombo);

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            searchTypeCombo.setSelectedIndex(0);
            statusFilterCombo.setSelectedIndex(0);
            loadAllBookings();
        });
        searchPanel.add(resetBtn);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.CENTER);
        
        add(topContainer, BorderLayout.NORTH);

        // ---------------- Table Panel ----------------
        String[] columns = {
            "Booking ID", "Guest Name", "Guest ID", "Phone", 
            "Room #", "Room Type", "Check-In", "Check-Out", 
            "Nights", "Boarding", "Status", "Total Cost"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(tableModel);
        bookingTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bookingTable.setRowHeight(25);
        bookingTable.setAutoCreateRowSorter(true);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add double-click listener to view booking details
        bookingTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = bookingTable.getSelectedRow();
                    if (selectedRow != -1) {
                        showBookingDetails(selectedRow);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        add(scrollPane, BorderLayout.CENTER);

        // ---------------- Bottom Panel ----------------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        JButton viewDetailsBtn = new JButton("View Details");
        viewDetailsBtn.addActionListener(e -> {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                showBookingDetails(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please select a booking to view details.", 
                    "No Selection", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        bottomPanel.add(viewDetailsBtn);

        JButton checkoutBtn = new JButton("Checkout Guest");
        checkoutBtn.addActionListener(e -> checkoutSelectedBooking());
        bottomPanel.add(checkoutBtn);

        JLabel infoLabel = new JLabel("Double-click on a booking to view details");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(infoLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Initial load
        loadAllBookings();
    }

    private void loadAllBookings() {
        tableModel.setRowCount(0);
        List<Booking> bookings = db.getBookings();

        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No bookings found in the system.", 
                "No Bookings", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Sort by check-in date (most recent first)
        bookings.sort((b1, b2) -> b2.getCheckInDate().compareTo(b1.getCheckInDate()));

        for (int i = 0; i < bookings.size(); i++) {
            addBookingToTable(bookings.get(i), i + 1);
        }
    }

    private void performSearch() {
        tableModel.setRowCount(0);
        String searchText = searchField.getText().trim().toLowerCase();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        String statusFilter = (String) statusFilterCombo.getSelectedItem();
        
        List<Booking> bookings = db.getBookings();
        
        // Filter by search criteria
        if (!searchText.isEmpty()) {
            bookings = bookings.stream().filter(booking -> {
                switch (searchType) {
                    case "Guest Name":
                        return booking.getResident().getName().toLowerCase().contains(searchText);
                    case "Room Number":
                        try {
                            int roomNum = Integer.parseInt(searchText);
                            return booking.getRoom().getRoomNumber() == roomNum;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    case "Guest ID":
                        return String.valueOf(booking.getResident().getId()).contains(searchText);
                    case "Phone Number":
                        return booking.getResident().getPhoneNumber().toLowerCase().contains(searchText);
                    default:
                        return false;
                }
            }).collect(Collectors.toList());
        }

        // Filter by status
        if (!statusFilter.equals("All")) {
            bookings = bookings.stream().filter(booking -> {
                String bookingStatus = determineBookingStatus(booking);
                return bookingStatus.equals(statusFilter);
            }).collect(Collectors.toList());
        }

        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No bookings found matching your search criteria.", 
                "No Results", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Sort by check-in date
        bookings.sort((b1, b2) -> b2.getCheckInDate().compareTo(b1.getCheckInDate()));

        for (int i = 0; i < bookings.size(); i++) {
            addBookingToTable(bookings.get(i), i + 1);
        }
    }

    private void addBookingToTable(Booking booking, int bookingId) {
        Resident resident = booking.getResident();
        Room room = booking.getRoom();
        String status = determineBookingStatus(booking);

        Object[] row = {
            bookingId,
            resident.getName(),
            resident.getId(),
            resident.getPhoneNumber(),
            room.getRoomNumber(),
            room.getType(),
            booking.getCheckInDate().format(formatter),
            booking.getCheckOutDate().format(formatter),
            booking.getNights(),
            booking.getBoarding().toString(),
            status,
            String.format("$%.2f", booking.getTotalPrice())
        };
        tableModel.addRow(row);
    }

    private String determineBookingStatus(Booking booking) {
        LocalDate today = LocalDate.now();
        LocalDate checkIn = booking.getCheckInDate();
        LocalDate checkOut = booking.getCheckOutDate();

        if (booking.isCheckedOut()) {
            return "Checked Out";
        } else if (today.isBefore(checkIn)) {
            return "Upcoming";
        } else if (today.isAfter(checkOut) || today.isEqual(checkOut)) {
            return "Completed";
        } else {
            return "Active";
        }
    }

    private void showBookingDetails(int selectedRow) {
        int modelRow = bookingTable.convertRowIndexToModel(selectedRow);
        
        String guestName = (String) tableModel.getValueAt(modelRow, 1);
        int guestId = (int) tableModel.getValueAt(modelRow, 2);
        String phone = (String) tableModel.getValueAt(modelRow, 3);
        int roomNumber = (int) tableModel.getValueAt(modelRow, 4);
        String roomType = (String) tableModel.getValueAt(modelRow, 5);
        String checkIn = (String) tableModel.getValueAt(modelRow, 6);
        String checkOut = (String) tableModel.getValueAt(modelRow, 7);
        int nights = (int) tableModel.getValueAt(modelRow, 8);
        String boarding = (String) tableModel.getValueAt(modelRow, 9);
        String status = (String) tableModel.getValueAt(modelRow, 10);
        String totalCost = (String) tableModel.getValueAt(modelRow, 11);

        String details = String.format(
            "=== BOOKING DETAILS ===\n\n" +
            "Guest Information:\n" +
            "  Name: %s\n" +
            "  Guest ID: %d\n" +
            "  Phone: %s\n\n" +
            "Room Information:\n" +
            "  Room Number: %d\n" +
            "  Room Type: %s\n\n" +
            "Booking Information:\n" +
            "  Check-In: %s\n" +
            "  Check-Out: %s\n" +
            "  Number of Nights: %d\n" +
            "  Boarding Option: %s\n" +
            "  Status: %s\n\n" +
            "Payment Information:\n" +
            "  Total Cost: %s",
            guestName, guestId, phone, roomNumber, roomType,
            checkIn, checkOut, nights, boarding, status, totalCost
        );

        JTextArea textArea = new JTextArea(details);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 400));

        JOptionPane.showMessageDialog(this, scrollPane, 
            "Booking Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void checkoutSelectedBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a booking to checkout.", 
                "No Selection", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = bookingTable.convertRowIndexToModel(selectedRow);
        int guestId = (int) tableModel.getValueAt(modelRow, 2);
        String status = (String) tableModel.getValueAt(modelRow, 10);

        if (status.equals("Checked Out")) {
            JOptionPane.showMessageDialog(this, 
                "This guest has already been checked out.", 
                "Already Checked Out", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (status.equals("Upcoming")) {
            JOptionPane.showMessageDialog(this, 
                "Cannot checkout a guest with an upcoming booking.", 
                "Invalid Action", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Resident resident = db.getResidentById(guestId);
        if (resident == null) {
            JOptionPane.showMessageDialog(this, 
                "Guest not found in the system.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to checkout " + resident.getName() + "?",
            "Confirm Checkout",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            HotelManagement hotelMgmt = new HotelManagement();
            hotelMgmt.checkoutResident(resident);
            
            JOptionPane.showMessageDialog(this, 
                "Guest " + resident.getName() + " has been checked out successfully.", 
                "Checkout Complete", 
                JOptionPane.INFORMATION_MESSAGE);
            
            performSearch(); // Refresh the table
        }
    }
}