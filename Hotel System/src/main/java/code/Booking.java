package main.java.code;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {

    private BoardingOption boarding;
    private Resident resident;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int nights;
    private boolean checkedOut;
    private double totalPrice;


    public Booking(Resident resident, Room room, LocalDate checkIn, int nights, BoardingOption boarding) {
        this.resident = resident;
        this.room = room;
        this.checkInDate = checkIn;
        this.nights = nights;
        this.boarding = boarding;
        this.checkOutDate = checkIn.plusDays(nights);

        // Automatically determine checkedOut status based on today's date
        if (checkOutDate.isBefore(LocalDate.now())) {
            this.checkedOut = true;   // booking already past
        } else {
            this.checkedOut = false;  // booking still active or ongoing
        }

        // automatic price calculation
        this.totalPrice = calculateTotalPrice();

        // link booking to resident and room
        resident.addBooking(this);
        room.addBooking(this);

        resident.addToBill(this.totalPrice);

        // If booking is active, mark resident as not checked out
        if (!this.checkedOut) {
            resident.setCheckedOut(false);
        }
    }


    private double calculateTotalPrice() {
        double roomCost = room.getPrice() * nights;
        double boardingCost = boarding.getPricePerNight() * nights;
        return roomCost + boardingCost;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // getters
    public Resident getResident() { 
        return resident; 
    }
    public Room getRoom() { 
        return room; 
    }
    public LocalDate getCheckInDate() { 
        return checkInDate; 
    }
    public LocalDate getCheckOutDate() { 
        return checkOutDate; 
    }
    public int getNights() {
        return nights;
    }
    public boolean isCheckedOut() {
        return checkedOut;
    }
    public BoardingOption getBoarding() {
        return boarding;
    }

    // setters
    public void setResident(Resident resident) {
        this.resident = resident;
    }
    public void setRoom(Room newRoom) {
        if (room != null) {
            room.removeBooking(this); // remove from old room
        }
        room = newRoom;
        room.addBooking(this); // add to new room
    }
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkInDate.plusDays(nights);
    }
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        this.nights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    public void setNights(int nights) {
        this.nights = nights;
        this.checkOutDate = checkInDate.plusDays(nights);
    }
    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }
    public void setBoarding(BoardingOption boarding) {
        this.boarding = boarding;
    }

    // additional methods
    public boolean overlaps(LocalDate start, LocalDate end) {
        return start.isBefore(checkOutDate) && end.isAfter(checkInDate);
    }
    
    public void updateCheckedOutStatus() {
        if (checkOutDate.isBefore(LocalDate.now())) {
            this.checkedOut = true;
        } else {
            this.checkedOut = false;
        }
    }

    public void updateRoomReference(Room newRoom) {
        this.room = newRoom;  // Just update the reference, don't add booking
    }

    public void recalculateTotalPrice() {
        this.totalPrice = calculateTotalPrice();
    }

    public void extendStay(int extraNights) {
        this.nights += extraNights;
        this.checkOutDate = checkInDate.plusDays(nights);
        recalculateTotalPrice();
    }
}
