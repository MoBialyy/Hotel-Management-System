package main.java.code;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HotelDB {

    // Singleton instance
    private static HotelDB instance;

    private List<Employee> employees;         // All employees (managers + receptionists)
    private List<Manager> managers;           // Only managers
    private List<Receptionist> receptionists; // Only receptionists
    private List<Resident> residents;         // All residents
    private List<Room> rooms;                 // All rooms in the hotel
    private List<Booking> bookings; // Global booking list

    // Private constructor
    private HotelDB() {
        employees = new ArrayList<>();
        managers = new ArrayList<>();
        receptionists = new ArrayList<>();
        residents = new ArrayList<>();
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
    }

    // Lazy initialization
    public static HotelDB getInstance() {
        if (instance == null) {
            instance = new HotelDB();
        }
        return instance;
    }

    // -------------------------
    // GETTERS
    // -------------------------
    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public List<Receptionist> getReceptionists() {
        return receptionists;
    }

    public List<Resident> getResidents() {
        return residents;
    }

    public List<Room> getRooms() {
        return rooms;
    }
    public List<Booking> getBookings() {
        return bookings;
    }

    // -------------------------
    // EMPLOYEE MANAGEMENT
    // -------------------------
    public void addManager(Manager manager) {
        managers.add(manager);
        employees.add(manager);
    }

    public void addReceptionist(Receptionist receptionist) {
        receptionists.add(receptionist);
        employees.add(receptionist);
    }

    // -------------------------
    // RESIDENTS
    // -------------------------
    public void addResident(Resident resident) {
        residents.add(resident);
    }

    // -------------------------
    // ROOMS
    // -------------------------
    public void addRoom(Room room) {
        rooms.add(room);
    }
    
    public void addRooms(List<Room> roomList) {
        rooms.addAll(roomList);
    }
    
    // -------------------------
    // BOOKINGS
    // -------------------------
    public void addBooking(Booking booking) {
        bookings.add(booking);
        //booking.getRoom().addBooking(booking);
        //booking.getResident().addBooking(booking);
    }

    // -------------------------
    // ADDITIONAL METHODS
    // -------------------------
    
    // Get Only Free Rooms for a specific period
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable(checkIn, checkOut)) {
                available.add(room);
            }
        }
        return available;
    }

    // Find room by number
    public Room getRoomByNumber(int number) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == number) {
                return room;
            }
        }
        return null; // not found
    }

}
