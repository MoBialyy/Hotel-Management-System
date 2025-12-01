package main.java.code;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                resident.addBooking(booking);
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

    public void checkoutResident(Resident resident) {
        Booking activeBooking = resident.getActiveBooking(); // get the active booking

        if (activeBooking == null) {
            System.out.println("No active booking found for " + resident.getName());
            return;
        }

        if (activeBooking.isCheckedOut()) {
            System.out.println(resident.getName() + " has already checked out.");
            return;
        }

        // Perform checkout
        activeBooking.setCheckedOut(true);       // mark booking as checked out
        resident.setCheckedOut(true);            // optional: mark resident as checked out
        System.out.println("Checkout completed for " + resident.getName() +
                ". Total amount due: " + activeBooking.getTotalPrice());
    }

    public void checkoutRoomResidents(Resident resident) {
        Booking activeBooking = resident.getActiveBooking();
        if (activeBooking == null) {
            System.out.println("No active booking found for " + resident.getName());
            return;
        }

        Room room = activeBooking.getRoom();
        double totalAmount = 0.0;
        StringBuilder message = new StringBuilder("Checkout completed for:\n");

        for (Booking b : room.getBookings()) {
            if (!b.isCheckedOut() &&
                b.overlaps(activeBooking.getCheckInDate(), activeBooking.getCheckOutDate())) {
                
                Resident r = b.getResident();
                b.setCheckedOut(true);
                r.setCheckedOut(true);  // mark resident as checked out

                totalAmount += b.getTotalPrice();
                message.append(String.format("%s - $%.2f\n", r.getName(), b.getTotalPrice()));
            }
        }

        message.append(String.format("Total Amount Paid: $%.2f", totalAmount));
        System.out.println(message.toString());
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


    public List<Booking> getBookingsBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : db.getBookings()) {
            if ((b.getCheckInDate().isEqual(startDate) || b.getCheckInDate().isAfter(startDate)) &&
                (b.getCheckInDate().isBefore(endDate) || b.getCheckInDate().isEqual(endDate))) {
                result.add(b);
            }
        }
        return result;
    }
}
