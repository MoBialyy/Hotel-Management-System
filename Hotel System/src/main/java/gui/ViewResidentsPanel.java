package main.java.gui;

import main.java.code.HotelDB;
import main.java.code.Resident;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewResidentsPanel extends JPanel {

    private HotelDB db = HotelDB.getInstance();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea detailArea;
    private JButton checkOutBtn;

    public ViewResidentsPanel(JFrame frame) {
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

        JLabel title = new JLabel("All Residents", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- Table ----------------
        String[] columns = {"ID", "First Name", "Last Name", "Age", "Nationality"};
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

        // ---------------- Load Residents ----------------
        loadResidents();

        // ---------------- Table Selection Listener ----------------
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    Resident r = db.getResidents().get(modelRow);
                    showResidentDetails(r);
                    checkOutBtn.setEnabled(!r.hasCheckedOut());
                } else {
                    detailArea.setText("");
                    checkOutBtn.setEnabled(false);
                }
            }
        });
    }

    private void loadResidents() {
        tableModel.setRowCount(0);
        List<Resident> residents = db.getResidents();
        for (Resident r : residents) {
            Object[] row = {
                    r.getId(),
                    r.getFirstName(),
                    r.getLastName(),
                    r.getAge(),
                    r.getNationality()
            };
            tableModel.addRow(row);
        }
    }

    private void showResidentDetails(Resident r) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(r.getId()).append("\n");
        sb.append("Name: ").append(r.getFirstName()).append(" ").append(r.getLastName()).append("\n");
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
        Resident r = db.getResidents().get(modelRow);

        if (r.hasCheckedOut()) {
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
            showResidentDetails(r);
            checkOutBtn.setEnabled(false);
        }
    }
}
