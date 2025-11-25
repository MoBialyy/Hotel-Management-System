package main.java.code;

public abstract class Employee {
    private static int idCounter = 1; // static counter shared by all employees
    private int id;
    private String name;
    private int age;
    private double salary;
    private String jobTitle;
    private String birthPlace;
    private String email;
    private String phoneNumber;
    private String address;

    public Employee(String name, int age, double salary, String jobTitle,
                    String birthPlace, String email, String phoneNumber, String address) {
        this.id = idCounter++; // assign and then increment
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.jobTitle = jobTitle;
        this.birthPlace = birthPlace;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Shared method
    public void displayInfo() {
        System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age +
                ", Salary: " + salary + ", Job Title: " + jobTitle +
                ", Birth Place: " + birthPlace + ", Email: " + email +
                ", Phone: " + phoneNumber + ", Address: " + address);
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public double getSalary() {
        return salary;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public String getBirthPlace() {
        return birthPlace;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAddress() {
        return address;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    // Abstract method to force child classes to implement specific duties
    public abstract void performDuty();
}