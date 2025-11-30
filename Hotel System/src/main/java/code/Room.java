package main.java.code;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Room {
    private int roomNumber;
    private String type;
    private double price;
    private int maxResidents;
    private List<Booking> bookings;

    public Room(int roomNumber, String type, double price, int maxResidents) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.maxResidents = maxResidents;
        this.bookings = new ArrayList<>();
    }

    // Getters
    public int getRoomNumber() { 
        return roomNumber; 
    }
    public double getPrice() { 
        return price; 
    }
    public int getMaxResidents() { 
        return maxResidents; 
    }
    public String getType() { 
        return type; 
    }
    public List<Booking> getBookings() { 
        return bookings; 
    }

    // Setters
    public void setRoomNumber(int roomNumber) { 
        this.roomNumber = roomNumber; 
    }
    public void setPrice(double price) { 
        this.price = price; 
    }
    public void setMaxResidents(int maxResidents) { 
        this.maxResidents = maxResidents; 
    }
    public void setType(String type) { 
        this.type = type; 
    }
    public void setBookings(List<Booking> bookings) { 
        this.bookings = bookings; 
    }

    // Method to display room information
    public void displayInfo() {
        System.out.println("Room " + roomNumber + " | Type: " + type + " | Price: " + price + " | Max: " + maxResidents);
        System.out.println("Bookings: " + bookings.size());
    }

    // Additional Methods    
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {
        for (Booking b : bookings) {
            if (!b.isCheckedOut() && b.overlaps(checkIn, checkOut)) return false;
        }
        return true;
    }

}
