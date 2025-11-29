package main.java.gui;

import main.java.code.HotelDB;
import main.java.code.Room;
import main.java.code.RoomFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ViewRoomsPanel extends JPanel {

    private HotelDB db = HotelDB.getInstance();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField checkInField, checkOutField;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ViewRoomsPanel(JFrame frame) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------------- Top Panel ----------------
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            frame.setContentPane(new ManagerPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        topPanel.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Room Status Monitoring", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- Controls Panel ----------------
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controls.add(new JLabel("Check-In (yyyy-MM-dd):"));
        checkInField = new JTextField(10);
        controls.add(checkInField);

        controls.add(new JLabel("Check-Out (yyyy-MM-dd):"));
        checkOutField = new JTextField(10);
        controls.add(checkOutField);

        JButton filterBtn = new JButton("Show Available");
        filterBtn.addActionListener(e -> refreshRoomTable());
        controls.add(filterBtn);

        JButton addRoomBtn = new JButton("Add Room(s)");
        addRoomBtn.addActionListener(e -> addRoomDialog(frame));
        controls.add(addRoomBtn);

        add(controls, BorderLayout.SOUTH);

        // ---------------- Table ----------------
        String[] columns = {"Room #", "Type", "Price", "Max Residents", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Initial load
        refreshRoomTable();
    }

    private void refreshRoomTable() {
        tableModel.setRowCount(0);

        LocalDate checkIn = null;
        LocalDate checkOut = null;

        if (!checkInField.getText().trim().isEmpty() && !checkOutField.getText().trim().isEmpty()) {
            try {
                checkIn = LocalDate.parse(checkInField.getText().trim(), formatter);
                checkOut = LocalDate.parse(checkOutField.getText().trim(), formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.");
                return;
            }
        }

        List<Room> rooms = db.getRooms();
        for (Room r : rooms) {
            String status = "Available";

            if (checkIn != null && checkOut != null) {
                if (!r.isAvailable(checkIn, checkOut)) {
                    status = "Occupied";
                }
            }

            Object[] row = {
                    r.getRoomNumber(),
                    r.getType(),
                    r.getPrice(),
                    r.getMaxResidents(),
                    status
            };
            tableModel.addRow(row);
        }
    }

    private void addRoomDialog(JFrame frame) {
        // Open the AddMultipleRoomsDialog
        AddMultipleRoomsDialog dialog = new AddMultipleRoomsDialog(frame);
        dialog.setVisible(true);

        if (dialog.isRoomsAdded()) {
            refreshRoomTable();
        }
    }
}
