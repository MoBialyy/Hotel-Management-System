package main.java.code;

public abstract class Employee {
    private static int idCounter = 1; // static counter shared by all employees
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private double salary;
    private String jobTitle;
    private String birthPlace;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;

    public Employee(String firstName, String lastName, int age, double salary, String jobTitle,
                    String birthPlace, String email, String phoneNumber, String address, 
                    String password) {
        this.id = idCounter++; // assign and then increment
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.salary = salary;
        this.jobTitle = jobTitle;
        this.birthPlace = birthPlace;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
    }

    // Shared method
    public void displayInfo() {
        System.out.println("ID: " + id + ", First Name: " + firstName + 
                ", Last Name: " + lastName + ", Age: " + age +
                ", Salary: " + salary + ", Job Title: " + jobTitle +
                ", Birth Place: " + birthPlace + ", Email: " + email +
                ", Phone: " + phoneNumber + ", Address: " + address + ", Password: " + password);
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getName() {
        return firstName + " " + lastName;
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
    public String getPassword() {
        return password;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
    public void setPassword(String password) {
        this.password = password;
    }

    // Abstract method to force child classes to implement specific duties
    public abstract void performDuty();
}