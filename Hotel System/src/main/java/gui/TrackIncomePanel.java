package main.java.gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import main.java.code.Booking;
import main.java.code.HotelManagement;

public class TrackIncomePanel extends JPanel {
    private HotelManagement hotelMgmt = new HotelManagement();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private JTable incomeTable;
    private DefaultTableModel tableModel;
    private JLabel totalIncomeLabel, totalBookingsLabel, avgBookingLabel;
    private JComboBox<String> periodCombo;
    private JTextField customStartField, customEndField;
    private JPanel customDatePanel;

    public TrackIncomePanel(JFrame frame) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            frame.setContentPane(new ManagerPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        topPanel.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Track Hotel Income", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Report Period"));
        
        controlPanel.add(new JLabel("Select Period:"));
        periodCombo = new JComboBox<>(new String[]{
            "This Week", "This Month", "This Year", "Last Week", 
            "Last Month", "Last Year", "Custom Range"
        });
        periodCombo.setPreferredSize(new Dimension(150, 25));
        periodCombo.addActionListener(e -> {
            toggleCustomDateFields();
            if (!periodCombo.getSelectedItem().equals("Custom Range")) {
                generateReport();
            }
        });
        controlPanel.add(periodCombo);

        // Custom date fields
        customDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        customDatePanel.add(new JLabel("From:"));
        customStartField = new JTextField(10);
        customDatePanel.add(customStartField);
        customDatePanel.add(new JLabel("To:"));
        customEndField = new JTextField(10);
        customDatePanel.add(customEndField);
        
        JButton customReportBtn = new JButton("Generate");
        customReportBtn.addActionListener(e -> generateReport());
        customDatePanel.add(customReportBtn);
        customDatePanel.setVisible(false);
        
        controlPanel.add(customDatePanel);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> generateReport());
        controlPanel.add(refreshBtn);

        add(controlPanel, BorderLayout.NORTH);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 10));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Income Summary"));
        summaryPanel.setPreferredSize(new Dimension(0, 100));

        totalIncomeLabel = new JLabel("Total Income: $0.00", JLabel.CENTER);
        totalIncomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalIncomeLabel.setForeground(new Color(46, 204, 113));
        
        totalBookingsLabel = new JLabel("Total Bookings: 0", JLabel.CENTER);
        totalBookingsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        avgBookingLabel = new JLabel("Avg per Booking: $0.00", JLabel.CENTER);
        avgBookingLabel.setFont(new Font("Arial", Font.BOLD, 16));

        summaryPanel.add(totalIncomeLabel);
        summaryPanel.add(totalBookingsLabel);
        summaryPanel.add(avgBookingLabel);

        // Table
        String[] columns = {"Booking Date", "Guest Name", "Room #", "Room Type", 
                           "Check-In", "Check-Out", "Nights", "Boarding", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        incomeTable = new JTable(tableModel);
        incomeTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        incomeTable.setRowHeight(25);
        incomeTable.setAutoCreateRowSorter(true);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(summaryPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(incomeTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Generate initial report
        generateReport();
    }

    private void toggleCustomDateFields() {
        customDatePanel.setVisible(periodCombo.getSelectedItem().equals("Custom Range"));
    }

    private void generateReport() {
        tableModel.setRowCount(0);
        
        LocalDate startDate, endDate;
        String selectedPeriod = (String) periodCombo.getSelectedItem();

        try {
            // Calculate date range based on selection
            switch (selectedPeriod) {
                case "This Week":
                    startDate = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
                    endDate = LocalDate.now();
                    break;
                case "This Month":
                    startDate = LocalDate.now().withDayOfMonth(1);
                    endDate = YearMonth.now().atEndOfMonth();
                    break;
                case "This Year":
                    startDate = LocalDate.now().withDayOfYear(1);
                    // in case leap years, too much detail :)
                    if(LocalDate.now().isLeapYear())
                        endDate = LocalDate.now().withDayOfYear(  366);
                    else
                        endDate = LocalDate.now().withDayOfYear(365);
                    break;
                case "Last Week":
                    LocalDate lastWeekEnd = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue());
                    startDate = lastWeekEnd.minusDays(6);
                    endDate = lastWeekEnd;
                    break;
                case "Last Month":
                    startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    endDate = LocalDate.now().minusMonths(1).withDayOfMonth(
                        LocalDate.now().minusMonths(1).lengthOfMonth());
                    break;
                case "Last Year":
                    startDate = LocalDate.now().minusYears(1).withDayOfYear(1);
                    endDate = LocalDate.now().minusYears(1).withDayOfYear(
                        LocalDate.now().minusYears(1).lengthOfYear());
                    break;
                case "Custom Range":
                    startDate = LocalDate.parse(customStartField.getText().trim(), formatter);
                    endDate = LocalDate.parse(customEndField.getText().trim(), formatter);
                    break;
                default:
                    startDate = LocalDate.now().withDayOfMonth(1);
                    endDate = LocalDate.now();
            }

            // Get bookings in date range
            List<Booking> bookings = hotelMgmt.getBookingsBetweenDates(startDate, endDate);
            // Calculate total income within date range
            double totalIncome = hotelMgmt.getTotalIncomeBetweenDates(startDate, endDate);
            // Get total bookings count
            int bookingCount = hotelMgmt.getTotalBookingsCount(startDate, endDate);

            // this is just to populate the table
            for (Booking b : bookings) {
                tableModel.addRow(new Object[]{
                    b.getCheckInDate().format(formatter),
                    b.getResident().getName(),
                    b.getRoom().getRoomNumber(),
                    b.getRoom().getType(),
                    b.getCheckInDate().format(formatter),
                    b.getCheckOutDate().format(formatter),
                    b.getNights(),
                    b.getBoarding().toString(),
                    String.format("$%.2f", b.getTotalPrice())
                });
            }

            // Update summary
            totalIncomeLabel.setText(String.format("Total Income: $%.2f", totalIncome));
            totalBookingsLabel.setText(String.format("Total Bookings: %d", bookingCount));
            
            if (bookingCount > 0) {
                avgBookingLabel.setText(String.format("Avg per Booking: $%.2f", totalIncome / bookingCount));
            } else {
                avgBookingLabel.setText("Avg per Booking: $0.00");
            }

            if (bookingCount == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No bookings found for the selected period.", 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error generating report: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}