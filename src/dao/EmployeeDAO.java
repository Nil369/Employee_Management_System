package dao;

import db.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import models.Employee;

public class EmployeeDAO {
    private DBConnection dbConnection;

    public EmployeeDAO() {
        dbConnection = new DBConnection();
    }
    
    // Insert Employee into the database
    public void addEmployee(Employee emp) {
        String query = "INSERT INTO employee (name, father_name, dob, salary, address, phone, email, education, designation, aadhar, empID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dbConnection.connection.prepareStatement(query)) {
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getFatherName());
            
            // Convert DOB to YYYY-MM-DD format before inserting into MySQL
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");  // Example: 10-Feb-2025
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Required format
            try {
                String formattedDob;
                formattedDob = outputFormat.format(inputFormat.parse(emp.getDob()));
                stmt.setString(3, formattedDob);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            stmt.setString(4, emp.getSalary());
            stmt.setString(5, emp.getAddress());
            stmt.setString(6, emp.getPhone());
            stmt.setString(7, emp.getEmail());
            stmt.setString(8, emp.getEducation());
            stmt.setString(9, emp.getDesignation());
            stmt.setString(10, emp.getAadhar());
            stmt.setString(11, emp.getEmpID());

            stmt.executeUpdate();
            System.out.println("Employee added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllEmployees() {
        try {
            String query = "SELECT * FROM employee"; 
            return dbConnection.statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    // Get Employee by ID
    public ResultSet getEmployeeById(String empID) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM employee WHERE empID = ?";
            PreparedStatement stmt = dbConnection.connection.prepareStatement(query);
            stmt.setString(1, empID);
            resultSet = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;

    }
    // Method to get all Employee IDs
    public List<String> getAllEmployeeIds() {
        List<String> empIds = new ArrayList<>();
        String query = "SELECT empID FROM employee";

        try {
            ResultSet resultSet = dbConnection.statement.executeQuery(query);
            while (resultSet.next()) {
                empIds.add(resultSet.getString("empID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empIds;
    }

    // Update Employee Details
    public void updateEmployee(Employee emp) {
        String query = "UPDATE employee SET name=?, father_name=?, dob=?, salary=?, address=?, phone=?, email=?, education=?, designation=?, aadhar=? WHERE empID=?";
    
        try (PreparedStatement stmt = dbConnection.connection.prepareStatement(query)) {
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getFatherName());
            
            // Ensuring correct date format
            stmt.setDate(3, java.sql.Date.valueOf(emp.getDob())); 
    
            stmt.setString(4, emp.getSalary());
            stmt.setString(5, emp.getAddress());
            stmt.setString(6, emp.getPhone());
            stmt.setString(7, emp.getEmail());
            stmt.setString(8, emp.getEducation());
            stmt.setString(9, emp.getDesignation());
            stmt.setString(10, emp.getAadhar());
            stmt.setString(11, emp.getEmpID());
    
            stmt.executeUpdate();
            System.out.println("Employee updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // Delete Employee by ID
    public void deleteEmployee(String empID) {
        String query = "DELETE FROM employee WHERE empID=?";
        try (PreparedStatement stmt = dbConnection.connection.prepareStatement(query)) {
            stmt.setString(1, empID);
            stmt.executeUpdate();
            System.out.println("Employee deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
