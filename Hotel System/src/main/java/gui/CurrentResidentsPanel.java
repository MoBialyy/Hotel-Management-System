package main.java.gui;

import main.java.code.HotelDB;
import main.java.code.Resident;
import main.java.code.Booking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CurrentResidentsPanel extends JPanel {

    private HotelDB db = HotelDB.getInstance();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea detailArea;
    private JButton checkOutBtn;

    public CurrentResidentsPanel(JFrame frame) {
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

        JLabel title = new JLabel("Current Residents", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- Table ----------------
        String[] columns = {"ID", "Name", "Room", "Type", "Check-in", "Check-out"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        JScrollPane tableScroll = new JScrollPane(table);

        // ---------------- Detail Area ----------------
        detailArea = new JTextArea();
        detailArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        detailArea.setEditable(false);
        JScrollPane detailScroll = new JScrollPane(detailArea);

        // ---------------- Buttons Panel ----------------
        checkOutBtn = new JButton("Check Out");
        checkOutBtn.setEnabled(false);
        checkOutBtn.addActionListener(e -> checkOutSelectedResident());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(checkOutBtn);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(detailScroll, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ---------------- Split Pane ----------------
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScroll, rightPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);

        // ---------------- Load Current Residents ----------------
        loadCurrentResidents();

        // ---------------- Table Selection Listener ----------------
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    Resident r = (Resident) table.getValueAt(modelRow, -1); // store resident object if needed
                    showResidentDetails(r);
                    checkOutBtn.setEnabled(!r.hasCheckedOut());
                } else {
                    detailArea.setText("");
                    checkOutBtn.setEnabled(false);
                }
            }
        });
    }

    private void loadCurrentResidents() {
        tableModel.setRowCount(0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Resident> residents = db.getResidents();

        for (Resident r : residents) {
            Booking activeBooking = r.getActiveBooking();
            if (activeBooking != null && !r.hasCheckedOut()) {
                Object[] row = {
                        r.getId(),
                        r.getName(),
                        activeBooking.getRoom().getRoomNumber(),
                        activeBooking.getRoom().getType(),
                        activeBooking.getCheckInDate().format(df),
                        activeBooking.getCheckOutDate().format(df)
                };
                tableModel.addRow(row);
            }
        }
    }

    private void showResidentDetails(Resident r) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(r.getId()).append("\n");
        sb.append("Name: ").append(r.getName()).append("\n");
        sb.append("Age: ").append(r.getAge()).append("\n");
        sb.append("Nationality: ").append(r.getNationality()).append("\n");
        sb.append("Email: ").append(r.getEmail()).append("\n");
        sb.append("Phone: ").append(r.getPhoneNumber()).append("\n");
        sb.append("Address: ").append(r.getAddress()).append("\n");
        sb.append("Government ID: ").append(r.getGovernmentId()).append("\n");
        sb.append("Total Bill: $").append(r.getTotalBill()).append("\n");
        sb.append("Checked Out: ").append(r.hasCheckedOut() ? "Yes" : "No").append("\n\n");

        sb.append("Bookings:\n");
        if (r.getBookings().isEmpty()) {
            sb.append("  No bookings.\n");
        } else {
            for (var b : r.getBookings()) {
                sb.append("  Room ").append(b.getRoom().getRoomNumber())
                        .append(" (").append(b.getRoom().getType()).append(")\n")
                        .append("    Check-in: ").append(b.getCheckInDate())
                        .append(", Check-out: ").append(b.getCheckOutDate())
                        .append(", Nights: ").append(b.getNights())
                        .append(", Checked Out: ").append(b.isCheckedOut() ? "Yes" : "No")
                        .append("\n");
            }
        }

        detailArea.setText(sb.toString());
        detailArea.setCaretPosition(0);
    }

    private void checkOutSelectedResident() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) return;

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int residentId = (int) tableModel.getValueAt(modelRow, 0);
        Resident r = db.getResidentById(residentId);
        if (r == null || r.hasCheckedOut()) {
            JOptionPane.showMessageDialog(this, "Resident already checked out.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to check out " + r.getName() + "?",
                "Confirm Check Out",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            r.setCheckedOut(true);
            JOptionPane.showMessageDialog(this, r.getName() + " has been checked out.");
            loadCurrentResidents();
            detailArea.setText("");
            checkOutBtn.setEnabled(false);
        }
    }
}
