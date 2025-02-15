package ui;

import java.awt.*;
import javax.swing.*;
import screens.Login;
import ui.cli.CLIEmployeeManager;

public class ModeSelection extends JFrame {
    private JDialog dialog;

    public void showDialog() {
        try {
            // Set look and feel to system default
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Enable anti-aliasing for better text rendering
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            dialog = new JDialog(this, "Select Application Mode", true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.WHITE); // Background color

            JLabel label = new JLabel("Choose Interface Mode:", SwingConstants.CENTER);
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            panel.add(label);

            // GUI Mode Button
            JButton guiButton = new JButton("1. GUI Mode");
            guiButton.setFont(new Font("Arial", Font.BOLD, 16));
            guiButton.setForeground(Color.BLACK);
            guiButton.setFocusPainted(false);
            guiButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.add(guiButton);

            // CLI Mode Button
            JButton cliButton = new JButton("2. CLI Mode");
            cliButton.setFont(new Font("Arial", Font.BOLD, 16));
            cliButton.setForeground(Color.BLACK);
            cliButton.setFocusPainted(false);
            cliButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.add(cliButton);

            
            // Button Actions
            guiButton.addActionListener(e -> {
                dialog.dispose();
                new Login(); // Open GUI Login
            });

            cliButton.addActionListener(e -> {
                dialog.dispose();
                startCLI(); // Open CLI Mode
            });

            dialog.add(panel);
            dialog.pack();
            dialog.setSize(500,300);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setResizable(false);

            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        } catch (Exception e) {
            System.err.println("Error showing mode selector: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startCLI() {
        System.out.println("CLI Mode Selected. Starting CLI...");
        // Implement CLI logic here
        new CLIEmployeeManager();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModeSelection().showDialog());
    }
}
