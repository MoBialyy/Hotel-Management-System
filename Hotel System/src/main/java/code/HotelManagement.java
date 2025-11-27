package main.java.code;
import java.time.LocalDate;

// Facade for hotel management operations
public class HotelManagement {

    private HotelDB db;

    public HotelManagement() {
        db = HotelDB.getInstance();
    }

    // -------------------------
    // EMPLOYEE METHODS
    // -------------------------
    public void createManager(String firstName, String lastName, int age, double salary, String jobTitle,
                              String birthPlace, String email, String phone, String address, 
                              String password) {
        Manager m = new Manager(firstName, lastName, age, salary, jobTitle, birthPlace, email, phone, address, 
            password);
        db.addManager(m);
        System.out.println("Manager created: " + firstName + " " + lastName);
    }

    public void createReceptionist(String firstName, String lastName, int age, double salary, String jobTitle,
                                   String birthPlace, String email, String phone, 
                                   String address, String password) {
        Receptionist r = new Receptionist(firstName, lastName, age, salary, jobTitle, birthPlace, email, phone, address,
            password);
        db.addReceptionist(r);
        System.out.println("Receptionist created: " + firstName + " " + lastName);
    }

    public void createWorker(String firstName, String lastName, int age, double salary, String jobTitle,
                             String birthPlace, String email, String phone, String address,
                             String password) {
        Worker w = new Worker(firstName, lastName, age, salary, jobTitle, birthPlace, email, phone, address,
            password);
        db.addWorker(w);
        System.out.println("Worker created: " + firstName + " " + lastName);
    }

    // -------------------------
    // RESIDENT METHODS
    // -------------------------
    public Resident addResident(String firstName, String lastName, int age, String nationality, 
                            String email, String phone, String address, String govId) {
        Resident resident = new Resident(firstName, lastName, age, nationality, email, phone, address, govId);
        db.addResident(resident);
        System.out.println("Resident added: " + firstName + " " + lastName);
        return resident;
    }

    // -------------------------
    // ROOM BOOKING METHODS
    // -------------------------
    public boolean bookRoomByType(Resident resident, String type, LocalDate checkIn, int nights, BoardingOption boardingOption) {
        LocalDate checkOut = checkIn.plusDays(nights);

        for (Room room : db.getRooms()) {
            if (room.getType().equalsIgnoreCase(type) && room.isAvailable(checkIn, checkOut)) {
                Booking booking = new Booking(resident, room, checkIn, nights, boardingOption);
                db.addBooking(booking); // adds to global DB, resident, and room
                System.out.println("Booking confirmed: " + resident.getName() +
                        " -> Room " + room.getRoomNumber() +
                        ", Type: " + type +
                        ", Boarding: " + boardingOption +
                        " From " + checkIn + " To " + checkOut);
                return true;
            }
        }
        System.out.println("No available " + type + " rooms for these dates.");
        return false;
    }

    public boolean bookRoomByNumber(Resident resident, int roomNumber, LocalDate checkIn, int nights, BoardingOption boarding) {
        Room room = db.getRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room " + roomNumber + " does not exist.");
            return false;
        }

        LocalDate checkOut = checkIn.plusDays(nights);

        if (!room.isAvailable(checkIn, checkOut)) {
            System.out.println("Room " + roomNumber + " is not available for those dates.");
            return false;
        }

        Booking booking = new Booking(resident, room, checkIn, nights, boarding);
        db.addBooking(booking);

        System.out.println("Booking confirmed: " + resident.getName() +
                " -> Room " + roomNumber +
                ", Boarding: " + boarding +
                ", From " + checkIn + " To " + checkOut);

        return true;
    }


    // Print all bookings in hotel
    public void printAllBookings() {
        System.out.println("\n===== ALL BOOKINGS =====");
        if (db.getBookings().isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }

        for (Booking b : db.getBookings()) {
           System.out.println("Resident: " + b.getResident().getName() +
                ", Room: " + b.getRoom().getRoomNumber() +
                ", Type: " + b.getRoom().getType() +
                ", Check-in: " + b.getCheckInDate() +
                ", Check-out: " + b.getCheckOutDate() +
                ", Nights: " + b.getNights() +
                ", Boarding: " + b.getBoarding() +
                ", Total Price: " + b.getTotalPrice());
        }
    }

    public void printAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        System.out.println("------ AVAILABLE ROOMS from " + checkIn + " to " + checkOut + " ------");
        boolean anyAvailable = false;

        for (Room room : db.getRooms()) {
            if (room.isAvailable(checkIn, checkOut)) {
                room.displayInfo();
                anyAvailable = true;
            }
        }

        if (!anyAvailable) {
            System.out.println("No rooms are available for these dates.");
        }
    }

}
