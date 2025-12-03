package main.java.gui;
import main.java.code.Booking;
import main.java.code.Resident;
import main.java.code.Room;
import javax.swing.*;
import java.awt.*;
import main.java.code.HotelManagement;

public class ResidentManagementPanel extends JPanel {

    private HotelManagement hotelManagement = new HotelManagement();

    private DefaultListModel<Resident> residentListModel;
    private JList<Resident> residentJList;

    public ResidentManagementPanel(JFrame frame) {
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

        JLabel title = new JLabel("Resident Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- Resident List ----------------
        residentListModel = new DefaultListModel<>();
        for (Resident r : hotelManagement.getResidents()) {
            residentListModel.addElement(r);
        }

        residentJList = new JList<>(residentListModel);
        residentJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        residentJList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        residentJList.setCellRenderer(new ResidentCellRenderer());

        JScrollPane listScrollPane = new JScrollPane(residentJList);
        listScrollPane.setPreferredSize(new Dimension(400, 300));
        add(listScrollPane, BorderLayout.CENTER);

        // ---------------- Buttons Panel ----------------
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setPreferredSize(new Dimension(100, 60)); 

        JButton addBtn = new JButton("Add Resident");
        styleButton(addBtn, new Color(70, 130, 180));
        addBtn.addActionListener(e -> openAddResidentDialog(frame));

        JButton editBtn = new JButton("Edit Resident");
        styleButton(editBtn, new Color(255, 165, 0));
        editBtn.addActionListener(e -> editSelectedResident(frame));

        JButton deleteBtn = new JButton("Delete Resident");
        styleButton(deleteBtn, new Color(178, 34, 34));
        deleteBtn.addActionListener(e -> deleteSelectedResident());

        JButton extendBtn = new JButton("Extend Stay");
        styleButton(extendBtn, new Color(34, 139, 34)); 
        extendBtn.addActionListener(e -> extendResidentStay());

        JButton changeRoomBtn = new JButton("Change Room");
        styleButton(changeRoomBtn, new Color(75, 0, 130)); 
        changeRoomBtn.addActionListener(e -> changeResidentRoom());

        buttonsPanel.add(extendBtn);
        buttonsPanel.add(changeRoomBtn);
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

    // ---------------- Delete Resident ----------------
    private void deleteSelectedResident() {
        Resident selected = residentJList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete " + selected.getName() + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                hotelManagement.deleteResident(selected);
                residentListModel.removeElement(selected);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please select a resident to delete.");
        }
    }

    // ---------------- Add Resident Dialog ----------------
    private void openAddResidentDialog(JFrame parent) {
        AddResidentDialog dialog = new AddResidentDialog(parent);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            Resident newResident = dialog.getResident();
            hotelManagement.addResident(newResident);
            residentListModel.addElement(newResident);

            JOptionPane.showMessageDialog(this, "Resident added successfully!");
        }
    }

    // ---------------- Edit Resident ----------------
    private void editSelectedResident(JFrame parent) {
        Resident selected = residentJList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a resident to edit.");
            return;
        }

        AddResidentDialog dialog = new AddResidentDialog(parent, selected);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            selected.setFirstName(dialog.getFirstName());
            selected.setLastName(dialog.getLastName());
            selected.setAge(dialog.getAge());
            selected.setNationality(dialog.getNationality());
            selected.setEmail(dialog.getEmail());
            selected.setPhoneNumber(dialog.getPhone());
            selected.setAddress(dialog.getAddress());
            selected.setGovernmentId(dialog.getGovernmentId());

            residentJList.repaint();
            JOptionPane.showMessageDialog(this, "Resident updated successfully!");
        }
    }

    // ---------------- Custom Renderer ----------------
    private class ResidentCellRenderer extends JPanel implements ListCellRenderer<Resident> {

        private JLabel nameLabel = new JLabel();
        private JLabel nationalityLabel = new JLabel();
        private JLabel statusLabel = new JLabel();

        public ResidentCellRenderer() {
            setLayout(new BorderLayout(10, 5));

            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
            left.add(nameLabel);

            JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
            center.add(nationalityLabel);

            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            right.add(statusLabel);

            add(left, BorderLayout.WEST);
            add(center, BorderLayout.CENTER);
            add(right, BorderLayout.EAST);

            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Resident> list,
                Resident r,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            nameLabel.setText(r.getName());
            nationalityLabel.setText(r.getNationality());
            statusLabel.setText(r.hasCheckedOut() ? "Checked Out" : "Active");

            setBackground(isSelected ? new Color(173, 216, 230) : Color.WHITE);

            return this;
        }
    }

    // ---------------- Extend Resident Stay ----------------
    private void extendResidentStay() {
        Resident selected = residentJList.getSelectedValue();
        if (selected == null || selected.hasCheckedOut()) {
            JOptionPane.showMessageDialog(this, "Please select an active resident to extend stay.");
            return;
        }

        Booking activeBooking = selected.getActiveBooking();
        if (activeBooking == null) {
            JOptionPane.showMessageDialog(this, "No active booking found for this resident.");
            return;
        }

        String nightsStr = JOptionPane.showInputDialog(this, "Enter number of nights to add:");
        if (nightsStr == null) return; // cancelled
        
        int extraNights;
        try {
            extraNights = Integer.parseInt(nightsStr);
            if (extraNights <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number of nights.");
            return;
        }

        boolean success = hotelManagement.extendBookingStay(activeBooking, extraNights);
        
        if (!success) {
            JOptionPane.showMessageDialog(this, "Room not available for the extended period.");
            return;
        }

        residentJList.repaint();
        JOptionPane.showMessageDialog(this, "Stay extended. New checkout date: " + activeBooking.getCheckOutDate());
    }
    
    // ---------------- Change Resident Room ----------------
    private void changeResidentRoom() {
        Resident selected = residentJList.getSelectedValue();
        if (selected == null || selected.hasCheckedOut()) {
            JOptionPane.showMessageDialog(this, "Please select an active resident to change room.");
            return;
        }

        Booking activeBooking = selected.getActiveBooking();
        if (activeBooking == null) {
            JOptionPane.showMessageDialog(this, "No active booking found for this resident.");
            return;
        }

        Room[] availableRooms = hotelManagement.getAvailableRoomsCurrently().stream()
                .filter(r -> r.isAvailable(activeBooking.getCheckInDate(), activeBooking.getCheckOutDate()))
                .toArray(Room[]::new);

        if (availableRooms.length == 0) {
            JOptionPane.showMessageDialog(this, "No available rooms for the booking period.");
            return;
        }

        String[] roomOptions = new String[availableRooms.length];
        for (int i = 0; i < availableRooms.length; i++) {
            roomOptions[i] = "Room " + availableRooms[i].getRoomNumber() + " (" + availableRooms[i].getType() + ")";
        }

        String selectedRoomStr = (String) JOptionPane.showInputDialog(
                this,
                "Select new room:",
                "Change Room",
                JOptionPane.PLAIN_MESSAGE,
                null,
                roomOptions,
                roomOptions[0]
        );
        if (selectedRoomStr == null) return;

        Room newRoom = null;
        for (Room r : availableRooms) {
            String display = "Room " + r.getRoomNumber() + " (" + r.getType() + ")";
            if (display.equals(selectedRoomStr)) {
                newRoom = r;
                break;
            }
        }
        if (newRoom == null) return;

        Room oldRoom = activeBooking.getRoom();
        
        hotelManagement.changeBookingRoom(activeBooking, oldRoom, newRoom);

        residentJList.repaint();
        JOptionPane.showMessageDialog(this, "Room changed to: " + newRoom.getRoomNumber() + 
                " (" + newRoom.getType() + ")");
    }

}