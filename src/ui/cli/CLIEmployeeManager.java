package ui.cli;

import dao.AdminDAO;
import dao.EmployeeDAO;
import db.DBConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
import javax.swing.*;
import javax.swing.text.*;
import models.Employee;
import ui.ModeSelection;

public class CLIEmployeeManager extends JFrame {
    private JTextPane terminal;
    private JScrollPane scrollPane;
    private EmployeeDAO employeeDAO;
    private AdminDAO adminDAO;
    private DBConnection dbConnection;
    private boolean isLoggedIn = false;
    private String loggedInUser = null;
    private StyledDocument doc;
    private SimpleAttributeSet defaultStyle;
    private int promptPosition = 0;

    public CLIEmployeeManager() {
        employeeDAO = new EmployeeDAO();
        adminDAO = new AdminDAO();
        dbConnection = new DBConnection();

        setTitle("CLI Employee Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);

        terminal = new JTextPane();
        terminal.setEditable(true);
        terminal.setBackground(Color.BLACK);
        terminal.setForeground(Color.WHITE);
        terminal.setFont(new Font("Monospaced", Font.PLAIN, 18));

        doc = terminal.getStyledDocument();
        defaultStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(defaultStyle, Color.WHITE);
        StyleConstants.setFontFamily(defaultStyle, "Monospaced");

        scrollPane = new JScrollPane(terminal);
        add(scrollPane, BorderLayout.CENTER);

        terminal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (terminal.getCaretPosition() < promptPosition) {
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processCommand();
                    e.consume();
                }
            }
        });

        setVisible(true);
        appendToTerminal("Welcome to CLI Employee Management System!\n");
        appendToTerminal("Type 'help' for a list of commands.\n\n");
        showPrompt();
    }

    private void appendToTerminal(String message) {
        try {
            doc.insertString(doc.getLength(), message, defaultStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void showPrompt() {
        appendToTerminal("EMS_Terminal>>> ");
        promptPosition = doc.getLength();
        terminal.setCaretPosition(promptPosition);
    }

    private void processCommand() {
        try {
            int length = doc.getLength();
            String command = doc.getText(promptPosition, length - promptPosition).trim();
            appendToTerminal("\n"); // Move to next line
            executeCommand(command);
            showPrompt();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void executeCommand(String command) {
        if (command.equalsIgnoreCase("help")) {
            showHelp();
        } else if (command.startsWith("login")) {
            login(command);
        } else if (!isLoggedIn) {
            appendToTerminal("⚠ You must log in first. Use: login <username> <password>\n");
        } else if (command.startsWith("sudo create admin")) {
            createAdmin(command);
        } else if (command.startsWith("sudo edit admin")) {
            editAdmin(command);
        } else if (command.startsWith("sudo rm admin")) {
            deleteAdmin(command);
        } else if (command.startsWith("sudo view admin")) {
            viewAdmin();
        } else if (command.startsWith("view employees")) {
            viewAllEmployees();
        } else if (command.startsWith("view employee ")) {
            viewEmployeeById(command);
        } else if (command.startsWith("add employee ")) {
            addEmployee(command);
        } else if (command.startsWith("update employee ")) {
            updateEmployee(command);
        } else if (command.startsWith("delete employee ")) {
            deleteEmployee(command);
        } else if (command.startsWith("exit()")) {
            appendToTerminal("Exiting EMS Terminal...\n");
            setVisible(false);
            new ModeSelection().showDialog();
            
        } else {
            appendToTerminal("Unknown command. Type 'help' for available commands.\n");
        }
    }

    private void showHelp() {
        appendToTerminal("\nAvailable Commands:\n");
        appendToTerminal("  login <username> <password> - Login as admin\n");
        appendToTerminal("  sudo create admin <user> <pass> - Create new admin\n");
        appendToTerminal("  sudo edit admin <user> <newpass> - Change admin password\n");
        appendToTerminal("  sudo rm admin <user>        - Delete admin account\n");
        appendToTerminal("  sudo view admin             - View all admin accounts\n");
        appendToTerminal("  view employees                    - View all employees\n");
        appendToTerminal("  view employee <empID>             - View employee details\n");
        appendToTerminal("  add employee <name> <salary>      - Add a new employee\n");
        appendToTerminal("  update employee <empID> <salary>  - Update employee salary\n");
        appendToTerminal("  delete employee <empID>           - Delete an employee\n");
        appendToTerminal("  exit()                      - Exit CLI version\n\n");
    }

    private void login(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 3) {
            appendToTerminal("Usage: login <username> <password>\n");
            return;
        }
    
        String username = parts[1];
        String password = parts[2];
    
        try {
        
            String query = "SELECT 'superadmin' AS role FROM login WHERE username = ? AND password = ? " +
                        "UNION " +
                        "SELECT 'admin' AS role FROM admin WHERE username = ? AND password = ?";
    
            PreparedStatement stmt = dbConnection.connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, username);
            stmt.setString(4, password);
    
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                isLoggedIn = true;
                loggedInUser = username;
                if(isLoggedIn) StyleConstants.setForeground(defaultStyle, Color.GREEN);
                appendToTerminal("✅ Login successful! Welcome, " + username + "\n");
            } else {
                appendToTerminal("❌ Invalid credentials. Try again.\n");
            }
        } catch (SQLException e) {
            appendToTerminal("⚠ Error logging in.\n");
            e.printStackTrace();
        }
    }
    
    private void createAdmin(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 4) {
            appendToTerminal("Usage: sudo create admin <username> <password>\n");
            return;
        }
        String username = parts[3];
        String password = parts[4];

        boolean success = adminDAO.createAdmin(username, password);
        appendToTerminal(success ? "✅ Admin " + username + " created successfully.\n" : "❌ Error creating admin.\n");
    }

    private void editAdmin(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 4) {
            appendToTerminal("Usage: sudo edit admin <username> <newpassword>\n");
            return;
        }
        String username = parts[3];
        String newPassword = parts[4];

        boolean success = adminDAO.editAdmin(username, newPassword);
        appendToTerminal(success ? "✅ Admin password updated.\n" : "❌ Admin not found.\n");
    }

    private void deleteAdmin(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 3) {
            appendToTerminal("Usage: sudo rm admin <username>\n");
            return;
        }
        String username = parts[3];

        boolean success = adminDAO.deleteAdmin(username);
        appendToTerminal(success ? "✅ Admin " + username + " deleted successfully.\n" : "❌ Admin not found.\n");
    }

    private void viewAdmin() {
        List<String> admins = adminDAO.getAllAdmins();
        if (admins.isEmpty()) {
            appendToTerminal("❌ No admins found.\n");
        } else {
            appendToTerminal("\n✅ List of Admins:\n");
            for (String admin : admins) {
                appendToTerminal("  - " + admin + "\n");
            }
            appendToTerminal("\n");
        }
    }


    // +++++++ Employee ++++++++++
    private void viewAllEmployees() {
        try {
            ResultSet rs = employeeDAO.getAllEmployees();
            while (rs.next()) {
                appendToTerminal("\nID: " + rs.getString("empID") + " | Name: " + rs.getString("name") + " | Salary: " + rs.getString("salary")+"\n");
            }
        } catch (SQLException e) {
            appendToTerminal("Error fetching employee data.\n");
        }
    }

    private void viewEmployeeById(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 3) {
            appendToTerminal("Usage: view employee <empID>\n");
            return;
        }
        String empID = parts[2];
        try {
            ResultSet rs = employeeDAO.getEmployeeById(empID);
            if (rs.next()) {
                appendToTerminal("\nID: " + rs.getString("empID") + " | Name: " + rs.getString("name") + " | Salary: " + rs.getString("salary")+"\n");
            } else {
                appendToTerminal("Employee not found.\n");
            }
        } catch (SQLException e) {
            appendToTerminal("Error retrieving employee data.\n");
        }
    }

    private void addEmployee(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 4) {
            appendToTerminal("Usage: add employee <name> <salary>\n");
            return;
        }
        String name = parts[2];
        String salary = parts[3];
        String empID = "EMP" + System.currentTimeMillis(); // Generate ID
        Employee emp = new Employee(name, "Unknown", "2000-01-01", salary, "Unknown", "0000000000", "unknown@gmail.com", "Unknown", "Unknown", "000000000000", empID);
        employeeDAO.addEmployee(emp);
        appendToTerminal("✅ Employee " + name + " added successfully.\n");
    }

    private void updateEmployee(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 4) {
            appendToTerminal("Usage: update employee <empID> <salary>\n");
            return;
        }
        String empID = parts[2];
        String salary = parts[3];

        try {
            Employee existingEmployee = new Employee(empID, "Updated Name", "1990-01-01", salary, "Updated Address", "1234567890", "email@example.com", "Degree", "Updated Role", "123456789012", empID);
            employeeDAO.updateEmployee(existingEmployee);
            appendToTerminal("✅ Employee salary updated.\n");
        } catch (Exception e) {
            appendToTerminal("Error updating employee salary.\n");
        }
    }

    private void deleteEmployee(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 3) {
            appendToTerminal("Usage: delete employee <empID>\n");
            return;
        }
        String empID = parts[2];
        employeeDAO.deleteEmployee(empID);
        appendToTerminal("✅ Employee deleted successfully.\n");
    }


    public static void main(String[] args) {
        new CLIEmployeeManager();
    }
}
