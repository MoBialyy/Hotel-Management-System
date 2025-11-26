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

        // automatic price calculation
        this.totalPrice = calculateTotalPrice();

        resident.addBooking(this);
        room.addBooking(this);

        resident.addToBill(this.totalPrice);
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
    public void setRoom(Room room) {
        this.room = room;
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

    
}
