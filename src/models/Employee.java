package models;

public class Employee {
    private String name;
    private String fatherName;
    private String dob;
    private String salary;
    private String address;
    private String phone;
    private String email;
    private String education;
    private String designation;
    private String aadhar;
    private String empID;

    // Constructor
    public Employee(String name, String fatherName, String dob, String salary, String address,
                    String phone, String email, String education, String designation, String aadhar, String empID) {
        this.name = name;
        this.fatherName = fatherName;
        this.dob = dob;
        this.salary = salary;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.education = education;
        this.designation = designation;
        this.aadhar = aadhar;
        this.empID = empID;
    }

    // Getters
    public String getName() { return name; }
    public String getFatherName() { return fatherName; }
    public String getDob() { return dob; }
    public String getSalary() { return salary; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getEducation() { return education; }
    public String getDesignation() { return designation; }
    public String getAadhar() { return aadhar; }
    public String getEmpID() { return empID; }
}

