package main.java.code;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HotelDB {

    // Singleton instance
    private static HotelDB instance;

    private List<Manager> managers;           // Only managers
    private List<Receptionist> receptionists; // Only receptionists
    private List<Worker> workers;             // Only workers
    private List<Employee> employees;         // All employees (managers + receptionists)
    private List<Resident> residents;         // All residents
    private List<Room> rooms;                 // All rooms in the hotel
    private List<Booking> bookings;           // Global booking list

    // Private constructor
    private HotelDB() {
        employees = new ArrayList<>();
        managers = new ArrayList<>();
        receptionists = new ArrayList<>();
        workers = new ArrayList<>();
        residents = new ArrayList<>();
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        
    }

    // Lazy initialization
    public static HotelDB getInstance() {
        if (instance == null) {
            instance = new HotelDB();
            RoomFactory.createRooms(5, 5, 5); // Initial rooms
            instance.addManager(new Manager("John", "Doe", 55, 50000, "Manager", "Cairo", "manager@hotel.com", "01020304050", "12 downtown cairo", "manager123"));
            instance.addReceptionist(new Receptionist("Jane", "Smith", 30, 30000, "Receptionist", "Alexandria", "receptionist@hotel.com", "01020304051", "34 seaside ave", "receptionist123"));
            instance.addWorker(new Worker("Mike", "Johnson", 28, 25000, "Housekeeping", "Giza", "worker@hotel.com", "01020304052", "56 pyramid st", "worker123"));
            instance.addResident(new Resident("Alice", "Brown", 32, "American", "alice@gmail.com", "01020304053", "78 elm st", "A1234567"));
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

    public List<Worker> getWorkers() {
        return workers;
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

    public void addWorker(Worker worker) {
        workers.add(worker);
        employees.add(worker);
    }

    public void deleteWorker(Worker worker) {
        workers.remove(worker);
        employees.remove(worker);
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

    // Find employee by username
    public Employee findEmployeeByEmail(String email) {
        for (Employee emp : employees) {
            if (emp.getEmail().equals(email)) {
                return emp;
            }
        }
        return null; // not found
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

    // Find resident by ID
    public Resident getResidentById(int id) {
        for (Resident res : residents) {
            if (res.getId() == id) {
                return res;
            }
        }
        return null; // not found
    }

}
