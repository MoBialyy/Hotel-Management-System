package main.java.code;
import java.util.ArrayList;
import java.util.List;

public class HotelDB {

    // Singleton instance
    private static HotelDB instance;

    private List<Employee> employees;         // All employees (managers + receptionists)
    private List<Manager> managers;           // Only managers
    private List<Receptionist> receptionists; // Only receptionists
    private List<Resident> residents;         // All residents

    // Private constructor
    private HotelDB() {
        employees = new ArrayList<>();
        managers = new ArrayList<>();
        receptionists = new ArrayList<>();
        residents = new ArrayList<>();
    }

    // Lazy initialization
    public static HotelDB getInstance() {
        if (instance == null) {
            instance = new HotelDB();
        }
        return instance;
    }

    // Add manager
    public void addManager(Manager manager) {
        managers.add(manager);
        employees.add(manager); // also add to general employees
    }

    // Add receptionist
    public void addReceptionist(Receptionist receptionist) {
        receptionists.add(receptionist);
        employees.add(receptionist); // also add to general employees
    }

    // Add resident
    public void addResident(Resident resident) {
        residents.add(resident);
    }

    // Getters
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
}

