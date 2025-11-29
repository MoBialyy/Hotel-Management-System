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

        // Table
        String[] columns = {"ID", "First Name", "Last Name", "Job Title", "Type"};
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

        // Load workers
        List<Worker> workers = db.getWorkers();
        for (Worker w : workers) {
            tableModel.addRow(new Object[]{
                    w.getId(),
                    w.getFirstName(),
                    w.getLastName(),
                    w.getJobTitle(),
                    "Worker"
            });
        }

        // Load receptionists
        List<Receptionist> receptionists = db.getReceptionists();
        for (Receptionist r : receptionists) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getFirstName(),
                    r.getLastName(),
                    "Receptionist",
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
            String type = (String) tableModel.getValueAt(modelRow, 4);

            if (type.equals("Worker")) {
                Worker w = db.getWorkers()
                        .stream()
                        .filter(worker -> worker.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (w != null) {
                    detailPanel.add(new JLabel("Full Name: " + w.getFirstName() + " " + w.getLastName()));
                    detailPanel.add(new JLabel("Age: " + w.getAge()));
                    detailPanel.add(new JLabel("Salary: $" + w.getSalary()));
                    detailPanel.add(new JLabel("Job Title: " + w.getJobTitle()));
                    detailPanel.add(new JLabel("Birth Place: " + w.getBirthPlace()));
                    detailPanel.add(new JLabel("Email: " + w.getEmail()));
                    detailPanel.add(new JLabel("Phone: " + w.getPhoneNumber()));
                    detailPanel.add(new JLabel("Address: " + w.getAddress()));
                }

            } else if (type.equals("Receptionist")) {
                Receptionist r = db.getReceptionists()
                        .stream()
                        .filter(rec -> rec.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (r != null) {
                    detailPanel.add(new JLabel("Full Name: " + r.getFirstName() + " " + r.getLastName()));
                    detailPanel.add(new JLabel("Age: " + r.getAge()));
                    detailPanel.add(new JLabel("Salary: $" + r.getSalary()));
                    detailPanel.add(new JLabel("Job Title: Receptionist"));
                    detailPanel.add(new JLabel("Birth Place: " + r.getBirthPlace()));
                    detailPanel.add(new JLabel("Email: " + r.getEmail()));
                    detailPanel.add(new JLabel("Phone: " + r.getPhoneNumber()));
                    detailPanel.add(new JLabel("Address: " + r.getAddress()));
                }
            }
        }

        detailPanel.revalidate();
        detailPanel.repaint();
    }
}
