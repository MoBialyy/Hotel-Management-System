package main.java.gui;
import main.java.code.Room;
import main.java.code.Booking;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import main.java.code.HotelManagement;

public class ViewAvailableRoomsPanel extends JPanel {

    private HotelManagement hotelManagement = new HotelManagement();
    private JTable roomTable;
    private DefaultTableModel roomTableModel;
    private JTable historyTable;
    private DefaultTableModel historyTableModel;
    private JTextField checkInField, checkOutField;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean showOnlyAvailable = false;
    private JLabel selectedRoomLabel;

    public ViewAvailableRoomsPanel(JFrame frame) {
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

        JLabel title = new JLabel("View Available Rooms", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- Main Split Panel ---------------- 
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.6);

        // Left Panel: Room List
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Rooms"));

        // Room Table
        String[] roomColumns = {"Room #", "Type", "Price", "Max Residents", "Status"};
        roomTableModel = new DefaultTableModel(roomColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(roomTableModel);
        roomTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roomTable.setRowHeight(25);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.setAutoCreateRowSorter(true);
        
        // Add selection listener to show booking history
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = roomTable.convertRowIndexToModel(selectedRow);
                    int roomNumber = (int) roomTableModel.getValueAt(modelRow, 0);
                    loadBookingHistory(roomNumber);
                }
            }
        });

        leftPanel.add(new JScrollPane(roomTable), BorderLayout.CENTER);

        // Controls Panel
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controls.add(new JLabel("Check-In (yyyy-MM-dd):"));
        checkInField = new JTextField(10);
        controls.add(checkInField);

        controls.add(new JLabel("Check-Out (yyyy-MM-dd):"));
        checkOutField = new JTextField(10);
        controls.add(checkOutField);

        JButton availableOnlyBtn = new JButton("Show Available Only");
        availableOnlyBtn.addActionListener(e -> {
            showOnlyAvailable = !showOnlyAvailable;
            availableOnlyBtn.setText(showOnlyAvailable ? "Show All Rooms" : "Show Available Only");
            refreshRoomTable();
        });
        controls.add(availableOnlyBtn);

        JButton filterBtn = new JButton("Filter by Date");
        filterBtn.addActionListener(e -> {
            if (!checkInField.getText().trim().isEmpty() && !checkOutField.getText().trim().isEmpty()) {
                showOnlyAvailable = true;
                availableOnlyBtn.setText("Show All Rooms");
            }
            refreshRoomTable();
        });
        controls.add(filterBtn);

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> {
            checkInField.setText("");
            checkOutField.setText("");
            showOnlyAvailable = false;
            availableOnlyBtn.setText("Show Available Only");
            refreshRoomTable();
        });
        controls.add(resetBtn);

        leftPanel.add(controls, BorderLayout.SOUTH);

        // Right Panel: Booking History
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Booking History"));

        selectedRoomLabel = new JLabel("Select a room to view booking history");
        selectedRoomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        selectedRoomLabel.setHorizontalAlignment(JLabel.CENTER);
        rightPanel.add(selectedRoomLabel, BorderLayout.NORTH);

        // Booking History Table
        String[] historyColumns = {"Guest Name", "Check-In", "Check-Out", "Nights", "Boarding", "Status", "Total Cost"};
        historyTableModel = new DefaultTableModel(historyColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(historyTableModel);
        historyTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        historyTable.setRowHeight(25);
        historyTable.setAutoCreateRowSorter(true);

        rightPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);

        // Initial load
        refreshRoomTable();
    }

    private void refreshRoomTable() {
        roomTableModel.setRowCount(0);
        historyTableModel.setRowCount(0);
        selectedRoomLabel.setText("Select a room to view booking history");

        LocalDate checkIn = null;
        LocalDate checkOut = null;

        // Parse dates if provided
        if (!checkInField.getText().trim().isEmpty() && !checkOutField.getText().trim().isEmpty()) {
            try {
                checkIn = LocalDate.parse(checkInField.getText().trim(), formatter);
                checkOut = LocalDate.parse(checkOutField.getText().trim(), formatter);
                
                if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                    JOptionPane.showMessageDialog(this, 
                        "Check-out date must be after check-in date.", 
                        "Invalid Date Range", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid date format. Use yyyy-MM-dd.", 
                    "Date Format Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        List<Room> rooms;
        
        // Get rooms based on filter settings
        if (showOnlyAvailable && checkIn != null && checkOut != null) {
            rooms = hotelManagement.getAvailableRooms(checkIn, checkOut);
        } else if (showOnlyAvailable) {
            // No dates specified but "Available Only" is active
            rooms = hotelManagement.getAvailableRoomsCurrently();
        } else {
            rooms = hotelManagement.getRooms();
        }

        // Populate table
        for (Room r : rooms) {
            String status = "Available";
            
            if (checkIn != null && checkOut != null) {
                if (!r.isAvailable(checkIn, checkOut)) {
                    if (showOnlyAvailable) continue; // Skip occupied rooms when filter is on
                    status = "Occupied";
                }
            } else {
                // Check current availability
                LocalDate today = LocalDate.now();
                if (!r.isAvailable(today, today.plusDays(1))) {
                    if (showOnlyAvailable) continue;
                    status = "Occupied";
                }
            }

            Object[] row = {
                r.getRoomNumber(),
                r.getType(),
                String.format("$%.2f", r.getPrice()),
                r.getMaxResidents(),
                status
            };
            roomTableModel.addRow(row);
        }

        if (roomTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "No rooms found matching the criteria.", 
                "No Results", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadBookingHistory(int roomNumber) {
        historyTableModel.setRowCount(0);
        selectedRoomLabel.setText("Booking History for Room " + roomNumber);

        // Get the room and its bookings
        Room room = hotelManagement.getRoomByNumber(roomNumber);
        if (room == null) {
            historyTableModel.addRow(new Object[]{"Room not found", "-", "-", "-", "-", "-", "-"});
            return;
        }

        List<Booking> bookings = room.getBookings();
        System.out.println("Loading booking history for Room " + roomNumber + ", total bookings: " + bookings.size());
        if (bookings == null || bookings.isEmpty()) {
            historyTableModel.addRow(new Object[]{"No booking history", "-", "-", "-", "-", "-", "-"});
            return;
        }

        // Sort bookings by check-in date (most recent first)
        bookings.sort((b1, b2) -> b2.getCheckInDate().compareTo(b1.getCheckInDate()));

        for (Booking booking : bookings) {
            String status = determineBookingStatus(booking);
            Object[] row = {
                booking.getResident().getName(),
                booking.getCheckInDate().format(formatter),
                booking.getCheckOutDate().format(formatter),
                booking.getNights(),
                booking.getBoarding().toString(),
                status,
                String.format("$%.2f", booking.getTotalPrice())
            };
            historyTableModel.addRow(row);
        }
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
}