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

    public Employee findEmployeeByEmail(String email) {
        for (Employee e : db.getEmployees()) {
            if (e.getEmail().equalsIgnoreCase(email)) {
                return e;
            }
        }
        return null; // not found
    }

    public void setEmployeeNewPassword(Employee emp, String newPassword) {
        emp.setPassword(newPassword);
        System.out.println("Password updated for employee: " + emp.getName());
    }

    public List<Worker> getWorkers(){
        return db.getWorkers();
    }

    public int getWorkerCount() {
        return db.getWorkers().size();
    }

    public void deleteWorker(Worker worker) {
        db.deleteWorker(worker);
        System.out.println("Worker deleted: " + worker.getName());
    }

    public List<Receptionist> getReceptionists(){
        return db.getReceptionists();
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

    public void addResident(Resident resident) {
        db.addResident(resident);
        System.out.println("Resident added: " + resident.getName());
    }

    public List<Resident> getResidents(){
        return db.getResidents();
    }
    
    public List<Booking> getBookings(Resident resident){
        return resident.getBookings();
    }

    public List<Booking> getBookings(){
        return db.getBookings();
    }

    public void deleteResident(Resident resident) {
        db.getResidents().remove(resident);
        System.out.println("Resident deleted: " + resident.getName());
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

    public void addBooking(Booking booking) {
        db.addBooking(booking);
        System.out.println("Booking added for resident: " + booking.getResident().getName() +
                " in Room " + booking.getRoom().getRoomNumber());
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

    public int getTotalBookingsCount(LocalDate startDate, LocalDate endDate) {
        return getBookingsBetweenDates(startDate, endDate).size();
    }

    public double getTotalIncomeBetweenDates(LocalDate startDate, LocalDate endDate) {
        double totalIncome = 0.0;
        for (Booking b : getBookingsBetweenDates(startDate, endDate)) {
            totalIncome += b.getTotalPrice();
        }
        return totalIncome;
    }

    public List<Room> getAvailableRoomsCurrently() {
        return db.getAvailableRoomsCurrently();
    }

    public void changeBookingRoom(Booking booking, Room oldRoom, Room newRoom) {
        // Get old total before any changes
        double oldTotal = booking.getTotalPrice();
        
        // Move booking between rooms
        oldRoom.removeBooking(booking);
        newRoom.addBooking(booking);
        booking.updateRoomReference(newRoom);
        
        // Recalculate price with new room
        booking.recalculateTotalPrice();
        double newTotal = booking.getTotalPrice();
        
        // Adjust resident's bill automatically
        double difference = newTotal - oldTotal;
        booking.getResident().addToBill(difference);
        
        System.out.println("Booking moved from Room " + oldRoom.getRoomNumber() + 
                        " to Room " + newRoom.getRoomNumber() + 
                        " for resident: " + booking.getResident().getName() +
                        " | Price difference: $" + String.format("%.2f", difference));
    }

    public boolean extendBookingStay(Booking booking, int extraNights) {
        Room room = booking.getRoom();
        LocalDate newCheckOut = booking.getCheckOutDate().plusDays(extraNights);
        
        // Check if room is available for extended period
        if (!room.isAvailable(booking.getCheckOutDate(), newCheckOut)) {
            return false; // Room not available
        }
        
        // Get old total before extension
        double oldTotal = booking.getTotalPrice();
        
        // Extend the booking
        booking.extendStay(extraNights);
        
        // Calculate new total and adjust bill
        double newTotal = booking.getTotalPrice();
        double extraCost = newTotal - oldTotal;
        booking.getResident().addToBill(extraCost);
        
        System.out.println("Booking extended by " + extraNights + " nights for resident: " + 
                        booking.getResident().getName() + 
                        " | Extra cost: $" + String.format("%.2f", extraCost) +
                        " | New checkout: " + booking.getCheckOutDate());
        
        return true; 
    }

    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        return db.getAvailableRooms(checkIn, checkOut);
    }

    public List<Room> getRooms() {
        return db.getRooms();
    }

    public Room getRoomByNumber(int number) {
        return db.getRoomByNumber(number);
    }

    public Resident getResidentById(int id) {
        return db.getResidentById(id);
    }
}
