package main.java.gui;

import main.java.code.RoomFactory;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

// ---------------- Numeric Filter ----------------
class NumericFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string.matches("\\d*")) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.matches("\\d*")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}

// ---------------- AddMultipleRoomsDialog ----------------
public class AddMultipleRoomsDialog extends JDialog {

    private JFormattedTextField singleField, doubleField, tripleField;
    private JButton addBtn, cancelBtn;
    private boolean roomsAdded = false;

    public AddMultipleRoomsDialog(JFrame parent) {
        super(parent, "Add Multiple Rooms", true);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // ---------------- Input Panel ----------------
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel singleLabel = new JLabel("Number of Single Rooms:");
        singleField = new JFormattedTextField();
        singleField.setColumns(5);
        ((AbstractDocument) singleField.getDocument()).setDocumentFilter(new NumericFilter());

        JLabel doubleLabel = new JLabel("Number of Double Rooms:");
        doubleField = new JFormattedTextField();
        doubleField.setColumns(5);
        ((AbstractDocument) doubleField.getDocument()).setDocumentFilter(new NumericFilter());

        JLabel tripleLabel = new JLabel("Number of Triple Rooms:");
        tripleField = new JFormattedTextField();
        tripleField.setColumns(5);
        ((AbstractDocument) tripleField.getDocument()).setDocumentFilter(new NumericFilter());

        inputPanel.add(singleLabel);
        inputPanel.add(singleField);
        inputPanel.add(doubleLabel);
        inputPanel.add(doubleField);
        inputPanel.add(tripleLabel);
        inputPanel.add(tripleField);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        wrapper.add(inputPanel, BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);

        // ---------------- Buttons Panel ----------------
        JPanel buttonPanel = new JPanel();
        addBtn = new JButton("Add Rooms");
        cancelBtn = new JButton("Cancel");
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ---------------- Button Actions ----------------
        addBtn.addActionListener(e -> addRoomsAction());
        cancelBtn.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(getParent());
    }

    private void addRoomsAction() {
        int numSingles = parseField(singleField);
        int numDoubles = parseField(doubleField);
        int numTriples = parseField(tripleField);

        if (numSingles < 0 || numDoubles < 0 || numTriples < 0) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers (0 or more).");
            return;
        }

        if (numSingles == 0 && numDoubles == 0 && numTriples == 0) {
            JOptionPane.showMessageDialog(this, "Enter at least one room to add.");
            return;
        }

        // Add rooms via RoomFactory
        RoomFactory.createRooms(numSingles, numDoubles, numTriples);

        JOptionPane.showMessageDialog(this, "Rooms added successfully!");
        roomsAdded = true;
        dispose();
    }

    private int parseField(JFormattedTextField field) {
        String text = field.getText().trim();
        if (text.isEmpty()) return 0;
        try {
            int value = Integer.parseInt(text);
            return Math.max(0, value);
        } catch (NumberFormatException ex) {
            return -1; // invalid
        }
    }

    public boolean isRoomsAdded() {
        return roomsAdded;
    }
}
