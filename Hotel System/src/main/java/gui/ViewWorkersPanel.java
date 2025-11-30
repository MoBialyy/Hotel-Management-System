package main.java.gui;

import main.java.code.HotelDB;
import main.java.code.Worker;
import main.java.code.Receptionist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewWorkersPanel extends JPanel {

    private HotelDB db = HotelDB.getInstance();
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel detailPanel;

    public ViewWorkersPanel(JFrame frame) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            frame.setContentPane(new ManagerPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        topPanel.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("All Employees", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        // Table — removed Type column
        String[] columns = {"ID", "First Name", "Last Name", "Job Title"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        loadEmployees();

        splitPane.setLeftComponent(new JScrollPane(table));

        // Detail panel
        detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        splitPane.setRightComponent(detailPanel);

        table.getSelectionModel().addListSelectionListener(e -> showEmployeeDetails());

        add(splitPane, BorderLayout.CENTER);
    }

    // Load Workers + Receptionists
    private void loadEmployees() {
        tableModel.setRowCount(0);

        // Workers
        List<Worker> workers = db.getWorkers();
        for (Worker w : workers) {
            tableModel.addRow(new Object[]{
                    w.getId(),
                    w.getFirstName(),
                    w.getLastName(),
                    w.getJobTitle()
            });
        }

        // Receptionists
        List<Receptionist> receptionists = db.getReceptionists();
        for (Receptionist r : receptionists) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getFirstName(),
                    r.getLastName(),
                    "Receptionist"
            });
        }
    }

    private void showEmployeeDetails() {
        detailPanel.removeAll();
        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0);

            // Determine whether ID belongs to Worker or Receptionist
            Worker worker = db.getWorkers()
                    .stream()
                    .filter(w -> w.getId() == id)
                    .findFirst()
                    .orElse(null);

            Receptionist rec = db.getReceptionists()
                    .stream()
                    .filter(r -> r.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (worker != null) {
                detailPanel.add(new JLabel("Full Name: " + worker.getFirstName() + " " + worker.getLastName()));
                detailPanel.add(new JLabel("Age: " + worker.getAge()));
                detailPanel.add(new JLabel("Salary: $" + worker.getSalary()));
                detailPanel.add(new JLabel("Job Title: " + worker.getJobTitle()));
                detailPanel.add(new JLabel("Birth Place: " + worker.getBirthPlace()));
                detailPanel.add(new JLabel("Email: " + worker.getEmail()));
                detailPanel.add(new JLabel("Phone: " + worker.getPhoneNumber()));
                detailPanel.add(new JLabel("Address: " + worker.getAddress()));
            }

            if (rec != null) {
                detailPanel.add(new JLabel("Full Name: " + rec.getFirstName() + " " + rec.getLastName()));
                detailPanel.add(new JLabel("Age: " + rec.getAge()));
                detailPanel.add(new JLabel("Salary: $" + rec.getSalary()));
                detailPanel.add(new JLabel("Job Title: Receptionist"));
                detailPanel.add(new JLabel("Birth Place: " + rec.getBirthPlace()));
                detailPanel.add(new JLabel("Email: " + rec.getEmail()));
                detailPanel.add(new JLabel("Phone: " + rec.getPhoneNumber()));
                detailPanel.add(new JLabel("Address: " + rec.getAddress()));
            }
        }

        detailPanel.revalidate();
        detailPanel.repaint();
    }
}
