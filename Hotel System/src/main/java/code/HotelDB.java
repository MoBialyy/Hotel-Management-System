package main.java.code;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HotelDB {

    // Singleton instance
    private static HotelDB instance;
    private static HotelManagement hotelManagement; // Facade for hotel operations

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
            instance.addWorker(new Worker("Mike", "Johnson", 28, 25000, "Housekeeping", "Giza", "mike@worker.com", "01020304052", "56 pyramid st", "worker123"));
            instance.addWorker(new Worker("Sara", "Davis", 26, 24000, "Maintenance", "Luxor", "sara@worker.com" , "01020934054", "90 nile rd", "worker124"));
            instance.addWorker(new Worker("Alex", "Scott", 35, 22000, "Cleaner", "Cairo", "alex@worker.com", "01021204055", "98 nile st", "worker125"));
            instance.addResident(new Resident("Alice", "Brown", 32, "American", "alice@gmail.com", "01020304053", "78 elm st", "1234567"));
            instance.addResident(new Resident("Bob", "Wilson", 45, "British", "bob@gmail.com", "01020304056", "90 oak st", "7654321"));
            instance.addResident(new Resident("Charlie", "Miller", 29, "Canadian", "charlie@gmail.com", "01020304057", "12 maple st", "1122334"));
            Resident r1 = new Resident("Ali", "Hassan", 28, "Egyptian", "ali@gmail.com", "0123456789", "Cairo", "20202919210192");
            instance.addResident(r1);
            instance.hotelManagement = new HotelManagement();
            hotelManagement.bookRoomByType(r1, "Single", LocalDate.now().plusDays(0), 3, BoardingOption.BED_AND_BREAKFAST);
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

    public void deleteResident(Resident resident) {
        residents.remove(resident);
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

    // Get currently available rooms (today)
    public List<Room> getAvailableRoomsCurrently() {
        LocalDate today = LocalDate.now();
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable(today, today.plusDays(1))) {
                available.add(room);
            }
        }
        return available;
    }
}
