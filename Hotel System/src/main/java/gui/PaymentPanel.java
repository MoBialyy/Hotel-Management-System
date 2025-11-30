package main.java.gui;

import main.java.code.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentPanel extends JPanel {
    private HotelDB db = HotelDB.getInstance();
    private HotelManagement hotelMgmt = new HotelManagement();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private JComboBox<String> guestCombo;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JLabel totalBillLabel;
    private Resident selectedResident;

    public PaymentPanel(JFrame frame) {
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

        JLabel title = new JLabel("Guest Payment", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Guest Selection Panel
        JPanel guestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        guestPanel.setBorder(BorderFactory.createTitledBorder("Select Guest"));
        guestPanel.add(new JLabel("Guest:"));
        guestCombo = new JComboBox<>();
        guestCombo.setPreferredSize(new Dimension(300, 25));
        guestPanel.add(guestCombo);

        // Bookings Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        String[] columns = {"Room #", "Check-In", "Check-Out", "Nights", "Boarding", "Status", "Cost"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        bookingTable = new JTable(tableModel);
        bookingTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bookingTable.setRowHeight(25);
        tablePanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        // Payment Panel
        JPanel paymentPanel = new JPanel(new BorderLayout(10, 10));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment Summary"));
        
        totalBillLabel = new JLabel("Total Bill: $0.00");
        totalBillLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalBillLabel.setForeground(new Color(231, 76, 60));
        totalBillLabel.setHorizontalAlignment(JLabel.CENTER);
        paymentPanel.add(totalBillLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton checkoutBtn = new JButton("Process Checkout & Payment");
        checkoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkoutBtn.setBackground(new Color(46, 204, 113));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.addActionListener(e -> processCheckout());
        buttonPanel.add(checkoutBtn);
        paymentPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Add action listener and load guests AFTER all components are initialized
        guestCombo.addActionListener(e -> loadGuestBookings());
        loadGuests();

        // Main Layout
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(guestPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(paymentPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadGuests() {
        guestCombo.removeAllItems();
        guestCombo.addItem("-- Select a Guest --");
        for (Resident r : db.getResidents()) {
            if (!r.hasCheckedOut()) {
                guestCombo.addItem(String.format("%s (ID: %d)", r.getName(), r.getId()));
            }
        }
    }

    private void loadGuestBookings() {
        tableModel.setRowCount(0);
        totalBillLabel.setText("Total Bill: $0.00");
        
        if (guestCombo.getSelectedIndex() <= 0) return;
        
        // Get the selected guest by finding them in the filtered list
        int selectedIndex = guestCombo.getSelectedIndex() - 1;
        int currentIndex = 0;
        for (Resident r : db.getResidents()) {
            if (!r.hasCheckedOut()) {
                if (currentIndex == selectedIndex) {
                    selectedResident = r;
                    break;
                }
                currentIndex++;
            }
        }
        
        if (selectedResident == null) return;
        
        List<Booking> bookings = selectedResident.getBookings();

        for (Booking b : bookings) {
            if (!b.isCheckedOut()) {
                tableModel.addRow(new Object[]{
                    b.getRoom().getRoomNumber(),
                    b.getCheckInDate().format(formatter),
                    b.getCheckOutDate().format(formatter),
                    b.getNights(),
                    b.getBoarding().toString(),
                    "Active",
                    String.format("$%.2f", b.getTotalPrice())
                });
            }
        }
        
        totalBillLabel.setText(String.format("Total Bill: $%.2f", selectedResident.getTotalBill()));
    }

    private void processCheckout() {
        if (selectedResident == null || guestCombo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a guest.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Process checkout for %s?\nTotal Amount: $%.2f", 
                selectedResident.getName(), selectedResident.getTotalBill()),
            "Confirm Checkout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            hotelMgmt.checkoutRoomResidents(selectedResident);
            JOptionPane.showMessageDialog(this, 
                String.format("Checkout complete!\nGuest: %s\nAmount Paid: $%.2f", 
                    selectedResident.getName(), selectedResident.getTotalBill()),
                "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
            
            loadGuests();
            tableModel.setRowCount(0);
            totalBillLabel.setText("Total Bill: $0.00");
            selectedResident = null;
        }
    }
}