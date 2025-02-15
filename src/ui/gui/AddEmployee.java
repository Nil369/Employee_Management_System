package ui.gui;

import dao.EmployeeDAO;
import models.Employee;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Random;

public class AddEmployee extends JFrame implements ActionListener {

    JTextField tname, tfname, taddress, tphone, taadhar, temail, tsalary, tdesignation;
    JLabel tempid;
    JDateChooser tdob;
    JComboBox<String> boxEducation;
    JButton add, back;
    Random ran = new Random();
    int number = ran.nextInt(999999);

    public AddEmployee() {
        getContentPane().setBackground(new Color(232, 240, 255));
        setLayout(null);

        JLabel heading = new JLabel("Add Employee Detail");
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

        JLabel aadhar = new JLabel("Aadhar Number");
        aadhar.setBounds(400, 350, 150, 30);
        // aadhar.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(aadhar);

        taadhar = new JTextField();
        taadhar.setBounds(600, 350, 150, 30);
        add(taadhar);

        JLabel designation = new JLabel("Designation");
        designation.setBounds(50, 350, 150, 30);
        add(designation);

        tdesignation = new JTextField(); // ✅ Initialize tdesignation properly
        tdesignation.setBounds(200, 350, 150, 30);
        add(tdesignation);

        JLabel empid = new JLabel("Employee ID");
        empid.setBounds(50, 400, 150, 30);
        add(empid);

        tempid = new JLabel("" + number);
        tempid.setBounds(200, 400, 150, 30);
        tempid.setForeground(Color.RED);
        add(tempid);

        add = new JButton("Add");
        add.setBounds(450, 550, 150, 40);
        add.addActionListener(this);
        add(add);

        back = new JButton("Back");
        back.setBounds(250, 550, 150, 40);
        back.addActionListener(this);
        add(back);

        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add) {
            try {
                // ✅ Convert Date to "DD-MMM-YYYY" format before passing to Employee model
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String dob = displayFormat.format(tdob.getDate());

                Employee employee = new Employee(
                        tname.getText(),
                        tfname.getText(),
                        dob, // Pass converted date
                        tsalary.getText(),
                        taddress.getText(),
                        tphone.getText(),
                        temail.getText(),
                        (String) boxEducation.getSelectedItem(),
                        tdesignation.getText(),
                        taadhar.getText(),
                        tempid.getText()
                );

                new EmployeeDAO().addEmployee(employee);
                JOptionPane.showMessageDialog(null, "Employee Added Successfully!");
                setVisible(false);
                new Main_class();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error Adding Employee. Please Try Again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            setVisible(false);
            new Main_class();
        }
    }

    public static void main(String[] args) {
        new AddEmployee();
    }
}
