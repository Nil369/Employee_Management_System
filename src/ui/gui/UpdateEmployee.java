package ui.gui;

import dao.EmployeeDAO;
import models.Employee;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class UpdateEmployee extends JFrame implements ActionListener {

    JTextField tname, tfname, taddress, tphone, taadhar, temail, tsalary, tdesignation;
    JLabel tempid;
    JDateChooser tdob;
    JComboBox<String> boxEducation;
    JButton update, back;
    String empID;
    EmployeeDAO employeeDAO;

    public UpdateEmployee(String empID) {
        this.empID = empID;
        employeeDAO = new EmployeeDAO();

        getContentPane().setBackground(new Color(252, 251, 235));
        setLayout(null);

        JLabel heading = new JLabel("Update Employee Details");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 25));
        add(heading);

        JLabel name = new JLabel("Name");
        name.setBounds(50, 150, 150, 30);
        add(name);

        tname = new JTextField();
        tname.setBounds(200, 150, 150, 30);
        add(tname);

        JLabel fname = new JLabel("Father's Name");
        fname.setBounds(400, 150, 150, 30);
        add(fname);

        tfname = new JTextField();
        tfname.setBounds(600, 150, 150, 30);
        add(tfname);

        JLabel dob = new JLabel("Date Of Birth");
        dob.setBounds(50, 200, 150, 30);
        add(dob);

        tdob = new JDateChooser();
        tdob.setBounds(200, 200, 150, 30);
        add(tdob);

        JLabel salary = new JLabel("Salary");
        salary.setBounds(400, 200, 150, 30);
        add(salary);

        tsalary = new JTextField();
        tsalary.setBounds(600, 200, 150, 30);
        add(tsalary);

        JLabel address = new JLabel("Address");
        address.setBounds(50, 250, 150, 30);
        add(address);

        taddress = new JTextField();
        taddress.setBounds(200, 250, 150, 30);
        add(taddress);

        JLabel phone = new JLabel("Phone");
        phone.setBounds(400, 250, 150, 30);
        add(phone);

        tphone = new JTextField();
        tphone.setBounds(600, 250, 150, 30);
        add(tphone);

        JLabel email = new JLabel("Email");
        email.setBounds(50, 300, 150, 30);
        add(email);

        temail = new JTextField();
        temail.setBounds(200, 300, 150, 30);
        add(temail);

        JLabel education = new JLabel("Education");
        education.setBounds(400, 300, 150, 30);
        add(education);

        String[] items = {"BBA", "B.Tech", "BCA", "BA", "BSC", "B.COM", "MBA", "MCA", "MA", "MTech", "MSC", "PHD"};
        boxEducation = new JComboBox<>(items);
        boxEducation.setBounds(600, 300, 150, 30);
        add(boxEducation);

        JLabel designation = new JLabel("Designation");
        designation.setBounds(50, 350, 150, 30);
        add(designation);

        tdesignation = new JTextField();
        tdesignation.setBounds(200, 350, 150, 30);
        add(tdesignation);

        JLabel aadhar = new JLabel("Aadhar Number");  // ✅ Added Aadhar label
        aadhar.setBounds(400, 350, 150, 30);
        add(aadhar);

        taadhar = new JTextField(); // ✅ Initialize taadhar before use
        taadhar.setBounds(600, 350, 150, 30);
        add(taadhar);

        JLabel empid = new JLabel("Employee ID");
        empid.setBounds(50, 400, 150, 30);
        add(empid);

        tempid = new JLabel(empID);
        tempid.setBounds(200, 400, 150, 30);
        tempid.setForeground(Color.RED);
        add(tempid);

        update = new JButton("Update");
        update.setBounds(450, 550, 150, 40);
        update.addActionListener(this);
        add(update);

        back = new JButton("Back");
        back.setBounds(250, 550, 150, 40);
        back.addActionListener(this);
        add(back);

        setSize(900, 700);
        setLocation(300, 50);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        loadEmployeeData(); // ✅ Load employee details after initialization
    }

    private void loadEmployeeData() {
        try {
            ResultSet rs = employeeDAO.getEmployeeById(empID);
            if (rs.next()) {
                tname.setText(rs.getString("name"));
                tfname.setText(rs.getString("father_name"));
                tdob.setDate(rs.getDate("dob"));
                tsalary.setText(rs.getString("salary"));
                taddress.setText(rs.getString("address"));
                tphone.setText(rs.getString("phone"));
                temail.setText(rs.getString("email"));
                boxEducation.setSelectedItem(rs.getString("education"));
                tdesignation.setText(rs.getString("designation"));
                taadhar.setText(rs.getString("aadhar")); // ✅ No more NullPointerException
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == update) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // ✅ MySQL format
            String formattedDob = "";

            // Convert Date
            java.util.Date selectedDate = tdob.getDate();
            if (selectedDate != null) {
                formattedDob = sdf.format(selectedDate); // ✅ Convert to `yyyy-MM-dd`
            }

            // Create Employee object with formatted DOB
            Employee emp = new Employee(
                    tname.getText(),
                    tfname.getText(),
                    formattedDob, // ✅ Use converted date
                    tsalary.getText(),
                    taddress.getText(),
                    tphone.getText(),
                    temail.getText(),
                    (String) boxEducation.getSelectedItem(),
                    tdesignation.getText(),
                    taadhar.getText(),
                    empID
            );

            employeeDAO.updateEmployee(emp);
            JOptionPane.showMessageDialog(null, "Employee Updated Successfully");
            setVisible(false);
            new View_Employee();
        } else {
            setVisible(false);
            new View_Employee();
        }
    }

    public static void main(String[] args) {
        new UpdateEmployee("EMP001");
    }
}
