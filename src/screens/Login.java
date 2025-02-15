package screens;

import db.DBConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import ui.ModeSelection;
import ui.gui.Main_class;

public class Login extends JFrame implements ActionListener {

    JTextField tusername;
    JPasswordField tpassword;
    JButton login, back;

    public Login() {
        
        JLabel username = new JLabel("Username");
        username.setBounds(40, 20, 100, 30);
        add(username);

        tusername = new JTextField();
        tusername.setBounds(150, 20, 150, 30);
        add(tusername);

        JLabel password = new JLabel("Password");
        password.setBounds(40, 70, 100, 30);
        add(password);

        tpassword = new JPasswordField();
        tpassword.setBounds(150, 70, 150, 30);
        add(tpassword);

        login = new JButton("LOGIN");
        login.setBounds(150, 140, 150, 30);
        login.setBackground(Color.black);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        add(login);

        back = new JButton("BACK");
        back.setBounds(150, 180, 150, 30);
        back.setBackground(Color.black);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        ImageIcon i11 = new ImageIcon(ClassLoader.getSystemResource("icons/login.jpg"));
        Image i22 = i11.getImage().getScaledInstance(250, 400, Image.SCALE_DEFAULT);
        ImageIcon i33 = new ImageIcon(i22);
        JLabel imgg = new JLabel(i33);
        imgg.setBounds(350, 0, 250, 400);
        add(imgg);

        setSize(600, 400);
        setTitle("Login");
        // setLocation(650, 350);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == login) {
        try {
            String username = tusername.getText();
            String password = new String(tpassword.getPassword()); // Use getPassword() for security

            DBConnection conn = new DBConnection();

            // Single query to check both login and admin tables
            String query = "SELECT 'superadmin' AS role FROM login WHERE username = ? AND password = ? " +
                        "UNION " +
                        "SELECT 'admin' AS role FROM admin WHERE username = ? AND password = ?";

            PreparedStatement stmt = conn.connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, username);
            stmt.setString(4, password);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                JOptionPane.showMessageDialog(null, "Login successful as " + role + "!");
                setVisible(false);
                new Main_class();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error occurred.");
        }
    } else if (e.getSource() == back) {
        setVisible(false);
        new ModeSelection().showDialog();
    }
}

    public static void main(String[] args) {
        new Login();
    }
}
