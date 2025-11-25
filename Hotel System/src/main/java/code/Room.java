package main.java.code;

public class Room {
    private int roomNumber;
    private String type; // Single, Double, Triple
    private double price;
    private boolean isOccupied;

    public Room(int roomNumber, String type, double price) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isOccupied = false;
    }

    // Getters
    public int getRoomNumber() {
        return roomNumber;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    // Setters
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    // Optional: display room info
    public void displayInfo() {
        System.out.println("Room " + roomNumber + ": Type=" + type +
                ", Price=" + price + ", Occupied=" + isOccupied);
    }
}
