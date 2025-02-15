package ui.gui;

import dao.EmployeeDAO;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.List;

public class View_Employee extends JFrame implements ActionListener {

    JTable table;
    Choice choiceEMP;
    JButton searchbtn, print, update, back;
    EmployeeDAO employeeDAO;

    public View_Employee() {
        employeeDAO = new EmployeeDAO(); // Initialize DAO

        getContentPane().setBackground(new Color(230, 239, 255));
        setLayout(null);

        JLabel search = new JLabel("Search by Employee ID");
        search.setBounds(20, 20, 150, 20);
        add(search);

        choiceEMP = new Choice();
        choiceEMP.setBounds(180, 20, 150, 20);
        add(choiceEMP);

        // ✅ Fetch Employee IDs with error handling
        List<String> empIds = employeeDAO.getAllEmployeeIds();
        if (empIds == null || empIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees found in the database!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            for (String empID : empIds) {
                choiceEMP.add(empID);
            }
        }

        table = new JTable();
        loadEmployeeTable(); // Load all employee data

        JScrollPane jp = new JScrollPane(table);
        jp.setBounds(0, 100, 900, 600);
        add(jp);

        searchbtn = new JButton("Search");
        searchbtn.setBounds(20, 70, 80, 20);
        searchbtn.addActionListener(this);
        add(searchbtn);

        print = new JButton("Print");
        print.setBounds(120, 70, 80, 20);
        print.addActionListener(this);
        add(print);

        update = new JButton("Update");
        update.setBounds(220, 70, 80, 20);
        update.addActionListener(this);
        add(update);

        back = new JButton("Back");
        back.setBounds(320, 70, 80, 20);
        back.addActionListener(this);
        add(back);

        setSize(900, 700);
        setLocationRelativeTo(null);  // ✅ Centers the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

    }

    private void loadEmployeeTable() {
        try {
            ResultSet resultSet = employeeDAO.getAllEmployees();
            if (resultSet != null && resultSet.isBeforeFirst()) { // ✅ Check if there are results
                table.setModel(DbUtils.resultSetToTableModel(resultSet));
            } else {
                JOptionPane.showMessageDialog(this, "No employee records found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employee data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchbtn) {
            try {
                String selectedEmpID = choiceEMP.getSelectedItem();
                ResultSet resultSet = employeeDAO.getEmployeeById(selectedEmpID);

                if (resultSet != null && resultSet.isBeforeFirst()) { // ✅ Ensure ResultSet is NOT empty
                    table.setModel(DbUtils.resultSetToTableModel(resultSet));
                } else {
                    JOptionPane.showMessageDialog(this, "Employee not found!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching employee details!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == print) {
            try {
                table.print();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error printing table!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == update) {
            if (choiceEMP.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "No employee selected for update!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            setVisible(false);
            new UpdateEmployee(choiceEMP.getSelectedItem());
        } else {
            setVisible(false);
            new Main_class();
        }
    }

    public static void main(String[] args) {
        new View_Employee();
    }
}
