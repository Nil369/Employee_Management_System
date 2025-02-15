package screens;

import java.awt.*;
import javax.swing.*;
import ui.ModeSelection;

public class Splash extends JFrame {

    public Splash() {
         // Remove title bar
        setUndecorated(true);
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/splash_screen.gif"));
        Image i2 = i1.getImage().getScaledInstance(1170, 650, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 1170, 650);
        
        // Heading Label
        JLabel heading = new JLabel("Employee Management System");
        heading.setBounds(200, 60, 1000, 80); 
        heading.setFont(new Font("Arial Black", Font.BOLD, 40)); 
        heading.setForeground(Color.BLACK); 

        image.add(heading); // Add heading inside the image label
        add(image); // Add the image label to the frame

        JLabel footer = new JLabel("Made by Akash Halder");
        footer.setBounds(300, 500, 1000, 80); 
        footer.setFont(new Font("Arial Black", Font.BOLD, 40)); 
        footer.setForeground(Color.BLACK); 

        image.add(footer); // Add heading inside the image label
        add(image); // Add the image label to the frame

        setSize(1170, 650);
        // setLocation(450, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        

        try {
            Thread.sleep(2500);
            setVisible(false);
            // new Login();
            new ModeSelection().showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Splash();
    }
}
