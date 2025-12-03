package main.java.gui;
import main.java.code.Worker;
import main.java.code.HotelManagement;
import javax.swing.*;
import java.awt.*;

public class WorkerManagementPanel extends JPanel {

    private HotelManagement hm = new HotelManagement();

    private DefaultListModel<Worker> workerListModel;
    private JList<Worker> workerJList;

    public WorkerManagementPanel(JFrame frame) {
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

        JLabel title = new JLabel("Worker Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- Worker List ----------------
        workerListModel = new DefaultListModel<>();
        for (Worker w : hm.getWorkers()) {
            workerListModel.addElement(w);
        }

        workerJList = new JList<>(workerListModel);
        workerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        workerJList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        workerJList.setCellRenderer(new WorkerCellRenderer());

        add(new JScrollPane(workerJList), BorderLayout.CENTER);

        // ---------------- Buttons Panel ----------------
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton addBtn = new JButton("Add Worker");
        styleButton(addBtn, new Color(70, 130, 180));
        addBtn.addActionListener(e -> openAddWorkerDialog(frame));

        JButton editBtn = new JButton("Edit Worker");
        styleButton(editBtn, new Color(255, 165, 0)); 
        editBtn.addActionListener(e -> editSelectedWorker(frame));
        
        JButton deleteBtn = new JButton("Delete Worker");
        styleButton(deleteBtn, new Color(178, 34, 34));
        deleteBtn.addActionListener(e -> deleteSelectedWorker());
        
        buttonsPanel.add(addBtn);
        buttonsPanel.add(editBtn);
        buttonsPanel.add(deleteBtn);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    // ---------------- Style Buttons ----------------
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ---------------- Delete Worker ----------------
    private void deleteSelectedWorker() {
        Worker selected = workerJList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete " + selected.getFirstName() + " " + selected.getLastName() + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                hm.deleteWorker(selected);
                workerListModel.removeElement(selected);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a worker to delete.");
        }
    }

    // ---------------- Open Add Worker Dialog ----------------
    private void openAddWorkerDialog(JFrame parentFrame) {
        AddWorkerDialog dialog = new AddWorkerDialog(parentFrame);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            hm.createWorker(
                    dialog.getFirstName(),
                    dialog.getLastName(),
                    dialog.getAge(),
                    dialog.getSalary(),
                    dialog.getJobTitle(),
                    dialog.getBirthPlace(),
                    dialog.getEmail(),
                    dialog.getPhone(),
                    dialog.getAddress(),
                    dialog.getPassword()
            );

            Worker newWorker = hm.getWorkers().get(hm.getWorkerCount() - 1);
            workerListModel.addElement(newWorker);

            JOptionPane.showMessageDialog(this, "Worker added successfully!");
        }
    }

    // ---------------- Edit Worker --------------------
    private void editSelectedWorker(JFrame parentFrame) {
    Worker selected = workerJList.getSelectedValue();
    if (selected == null) {
        JOptionPane.showMessageDialog(this, "Please select a worker to edit.");
        return;
    }

    AddWorkerDialog dialog = new AddWorkerDialog(parentFrame, selected); 
    dialog.setVisible(true);

    if (dialog.isSaved()) {
        // Update the worker directly
        selected.setFirstName(dialog.getFirstName());
        selected.setLastName(dialog.getLastName());
        selected.setAge(dialog.getAge());
        selected.setSalary(dialog.getSalary());
        selected.setJobTitle(dialog.getJobTitle());
        selected.setBirthPlace(dialog.getBirthPlace());
        selected.setEmail(dialog.getEmail());
        selected.setPhoneNumber(dialog.getPhone());
        selected.setAddress(dialog.getAddress());
        selected.setPassword(dialog.getPassword());

        workerJList.repaint(); // refresh JList display
        JOptionPane.showMessageDialog(this, "Worker updated successfully!");
    }
}

    // ---------------- Custom Renderer ----------------
    private class WorkerCellRenderer extends JPanel implements ListCellRenderer<Worker> {
        private JLabel nameLabel = new JLabel();
        private JLabel jobLabel = new JLabel();
        private JLabel salaryLabel = new JLabel();

        public WorkerCellRenderer() {
            setLayout(new BorderLayout(10, 5));

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            leftPanel.add(nameLabel);
            add(leftPanel, BorderLayout.WEST);

            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            centerPanel.add(jobLabel);
            add(centerPanel, BorderLayout.CENTER);

            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.add(salaryLabel);
            add(rightPanel, BorderLayout.EAST);

            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Worker> list, Worker worker, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            nameLabel.setText(worker.getFirstName() + " " + worker.getLastName());
            jobLabel.setText(worker.getJobTitle());
            salaryLabel.setText(String.format("$%.2f", worker.getSalary()));

            setBackground(isSelected ? new Color(173, 216, 230) : Color.WHITE);
            return this;
        }
    }
}
