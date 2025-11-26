package main.java.code;
import java.util.ArrayList;
import java.util.List;

public class Resident {

    private static int idCounter = 1;

    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String nationality;
    private String email;
    private String phoneNumber;
    private String address;
    private String governmentId;
    private double totalBill;
    private boolean hasCheckedOut;
    private List<Booking> bookings;

    // -------------------------
    // CONSTRUCTOR
    // -------------------------
    public Resident(String firstName, String lastName, int age, String nationality,
                    String email, String phoneNumber, String address,
                    String governmentId) {
        this.id = idCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.nationality = nationality;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.governmentId = governmentId;
        this.totalBill = 0.0;
        this.hasCheckedOut = false;
        this.bookings = new ArrayList<>();
    }

    // -------------------------
    // GETTERS
    // -------------------------
    public int getId() { 
        return id; 
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getName() {
        return firstName + " " + lastName;
    }
    public int getAge() { 
        return age; 
    }
    public String getNationality() { 
        return nationality; 
    }
    public String getEmail() { 
        return email;
    }
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    public String getAddress() { 
        return address; 
    }
    public String getGovernmentId() { 
        return governmentId; 
    }
    public double getTotalBill() { 
        return totalBill; 
    }
    public boolean hasCheckedOut() { 
        return hasCheckedOut; 
    }
    public List<Booking> getBookings() {
        return bookings;
    }

    // -------------------------
    // SETTERS
    // -------------------------
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public void setAge(int age) { 
        this.age = age; 
    }
    public void setNationality(String nationality) { 
        this.nationality = nationality; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
    public void setAddress(String address) { 
        this.address = address; 
    }
    public void setGovernmentId(String governmentId) { 
        this.governmentId = governmentId; 
    }
    public void setTotalBill(double amount) { 
        this.totalBill = amount; 
    }
    public void setCheckedOut(boolean value) { 
        this.hasCheckedOut = value; 
    }

    // -------------------------
    // DISPLAY INFO
    // -------------------------
    public void displayInfo() {
        System.out.println("\n--- RESIDENT INFO ---");
        System.out.println("ID: " + id + ", Name: " + getName() + ", Age: " + age + ", Nationality: " + nationality);
        System.out.println("Email: " + email + ", Phone: " + phoneNumber + ", Address: " + address + ", Government ID: " + governmentId);
        System.out.println("Total Bill: " + totalBill + ", Checked Out: " + hasCheckedOut);
        System.out.println("Bookings: " + bookings.size());
    }
    
    // -------------------------
    // PRINT ALL BOOKINGS
    // -------------------------
    public void printBookings() {
        System.out.println("\n--- BOOKINGS for " + getName() + " ---");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        for (Booking b : bookings) {
            System.out.println("Room " + b.getRoom().getRoomNumber() + " (" + b.getRoom().getType() + ")");
            System.out.println("Check-in: " + b.getCheckInDate() + ", Check-out: " + b.getCheckOutDate() + ", Nights: " + b.getNights());
            System.out.println("Checked Out: " + b.isCheckedOut());
            System.out.println("---");
        }
    }


    // Additional Methods
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public Booking getActiveBooking() {
        for (Booking b : bookings) {
            if (!b.isCheckedOut()) return b;
        }
        return null;
    }

    public void addToBill(double amount) {
        totalBill += amount;
    }



}
