package main.java.gui;
import main.java.code.Resident;
import main.java.code.HotelManagement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewResidentsPanelRecep extends JPanel {

    private HotelManagement hotelManagement = new HotelManagement();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea detailArea;

    public ViewResidentsPanelRecep(JFrame frame) {
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

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(detailScroll, BorderLayout.CENTER);

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
                    Resident r = hotelManagement.getResidents().get(modelRow);
                    showResidentDetails(r);
                } else {
                    detailArea.setText("");
                }
            }
        });
    }

    private void loadResidents() {
        tableModel.setRowCount(0);
        List<Resident> residents = hotelManagement.getResidents();
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
        sb.append("═══════════════════════════════════════════════\n");
        sb.append("              RESIDENT DETAILS\n");
        sb.append("═══════════════════════════════════════════════\n\n");
        
        sb.append("ID:            ").append(r.getId()).append("\n");
        sb.append("Name:          ").append(r.getFirstName()).append(" ").append(r.getLastName()).append("\n");
        sb.append("Age:           ").append(r.getAge()).append("\n");
        sb.append("Nationality:   ").append(r.getNationality()).append("\n");
        sb.append("Email:         ").append(r.getEmail()).append("\n");
        sb.append("Phone:         ").append(r.getPhoneNumber()).append("\n");
        sb.append("Address:       ").append(r.getAddress()).append("\n");
        sb.append("Government ID: ").append(r.getGovernmentId()).append("\n");
        sb.append("Total Bill:    $").append(String.format("%.2f", r.getTotalBill())).append("\n");
        sb.append("Status:        ").append(r.hasCheckedOut() ? "Checked Out" : "Active").append("\n");

        sb.append("\n═══════════════════════════════════════════════\n");
        sb.append("                  BOOKINGS\n");
        sb.append("═══════════════════════════════════════════════\n\n");

        System.out.println(hotelManagement.getBookings(r).size());
        for (var b : hotelManagement.getBookings(r)) {
            System.out.println(b);
        }

        if (hotelManagement.getBookings(r).isEmpty()) {
            sb.append("No bookings found.\n");
        } else {
            int bookingNum = 1;
            for (var b : hotelManagement.getBookings(r)) {
                sb.append("┌─ Booking #").append(bookingNum++).append(" ─────────────────────────────────\n");
                sb.append("│\n");
                sb.append("│ Room:        Room ").append(b.getRoom().getRoomNumber())
                        .append(" (").append(b.getRoom().getType()).append(")\n");
                sb.append("│ Check-in:    ").append(b.getCheckInDate()).append("\n");
                sb.append("│ Check-out:   ").append(b.getCheckOutDate()).append("\n");
                sb.append("│ Nights:      ").append(b.getNights()).append("\n");
                sb.append("│ Boarding:    ").append(b.getBoarding()).append("\n");
                sb.append("│ Price:       $").append(String.format("%.2f", b.getTotalPrice())).append("\n");
                sb.append("│ Status:      ").append(b.isCheckedOut() ? "Checked Out" : "Active").append("\n");
                sb.append("│\n");
                sb.append("└───────────────────────────────────────────────\n\n");
            }
        }

        detailArea.setText(sb.toString());
        detailArea.setCaretPosition(0);
    }
}