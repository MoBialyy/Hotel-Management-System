package main.java.code;

public class Receptionist extends Employee {

    public Receptionist(String firstName, String lastName, int age, double salary, String jobTitle,
                   String birthPlace, String email, String phoneNumber, String address, 
                   String password) {
        super(firstName, lastName, age, salary, jobTitle, birthPlace, email, phoneNumber, address, password);
    }

    @Override
    public void performDuty() {
        System.out.println("Managing residents, room assignments, and bookings.");
    }
}
