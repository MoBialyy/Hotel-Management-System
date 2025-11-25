package main.java.code;

public class Manager extends Employee {

    public Manager(String name, int age, double salary, String jobTitle,
                   String birthPlace, String email, String phoneNumber, String address) {
        super(name, age, salary, jobTitle, birthPlace, email, phoneNumber, address);
    }

    @Override
    public void performDuty() {
        System.out.println("Managing hotel operations and staff.");
    }
}
