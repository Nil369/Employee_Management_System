package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private DBConnection dbConnection;

    public AdminDAO() {
        dbConnection = new DBConnection();
    }

    // ✅ Create a new admin
    public boolean createAdmin(String username, String password) {
        String query = "INSERT INTO admin (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = dbConnection.connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Edit existing admin password
    public boolean editAdmin(String username, String newPassword) {
        String query = "UPDATE admin SET password=? WHERE username=?";
        try (PreparedStatement stmt = dbConnection.connection.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Delete an admin account
    public boolean deleteAdmin(String username) {
        String query = "DELETE FROM admin WHERE username=?";
        try (PreparedStatement stmt = dbConnection.connection.prepareStatement(query)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Get list of all admins
    public List<String> getAllAdmins() {
        List<String> admins = new ArrayList<>();
        String query = "SELECT username FROM admin";
        try (Statement stmt = dbConnection.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                admins.add(resultSet.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }
}
