import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FitnessAppGUI {
    // Database credentials
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/fitnessDB";
    private static final String JDBC_USER = "root"; // Change if necessary
    private static final String JDBC_PASS = "admin"; // Change to your MySQL password

    public static void main(String[] args) {
        // Create and set up the main window
        JFrame frame = new JFrame("Fitness App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new GridLayout(12, 2, 10, 10)); // Updated for more fields and spacing
        frame.getContentPane().setBackground(new Color(230, 240, 255)); // Soft background color

        // Set up fonts and styles
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        // Create input fields and labels for the new attributes
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        JTextField nameField = new JTextField();
        nameField.setFont(fieldFont);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        JTextField ageField = new JTextField();
        ageField.setFont(fieldFont);

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setFont(labelFont);
        JTextField weightField = new JTextField();
        weightField.setFont(fieldFont);

        JLabel caloriesLabel = new JLabel("Calories Burned (kcal):");
        caloriesLabel.setFont(labelFont);
        JTextField caloriesField = new JTextField();
        caloriesField.setFont(fieldFont);

        JLabel waterLabel = new JLabel("Water Intake (liters):");
        waterLabel.setFont(labelFont);
        JTextField waterField = new JTextField();
        waterField.setFont(fieldFont);

        JLabel stepsLabel = new JLabel("Steps Walked:");
        stepsLabel.setFont(labelFont);
        JTextField stepsField = new JTextField();
        stepsField.setFont(fieldFont);

        JLabel exerciseLabel = new JLabel("Exercise Minutes:");
        exerciseLabel.setFont(labelFont);
        JTextField exerciseField = new JTextField();
        exerciseField.setFont(fieldFont);

        JLabel sleepLabel = new JLabel("Sleep Hours:");
        sleepLabel.setFont(labelFont);
        JTextField sleepField = new JTextField();
        sleepField.setFont(fieldFont);

        // Create buttons for each operation
        JButton addButton = new JButton("Add User");
        JButton viewButton = new JButton("View Users");
        JButton updateButton = new JButton("Update User");
        JButton deleteButton = new JButton("Delete User");

        // Style buttons
        addButton.setFont(labelFont);
        viewButton.setFont(labelFont);
        updateButton.setFont(labelFont);
        deleteButton.setFont(labelFont);

        // Add components to the window in a professional layout
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(ageLabel);
        frame.add(ageField);
        frame.add(weightLabel);
        frame.add(weightField);
        frame.add(caloriesLabel);
        frame.add(caloriesField);
        frame.add(waterLabel);
        frame.add(waterField);
        frame.add(stepsLabel);
        frame.add(stepsField);
        frame.add(exerciseLabel);
        frame.add(exerciseField);
        frame.add(sleepLabel);
        frame.add(sleepField);
        frame.add(addButton);
        frame.add(viewButton);
        frame.add(updateButton);
        frame.add(deleteButton);

        // Show the window
        frame.setVisible(true);

        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser(nameField.getText(), ageField.getText(), weightField.getText(), caloriesField.getText(),
                        waterField.getText(), stepsField.getText(), exerciseField.getText(), sleepField.getText());
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewUsers();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Enter user ID to update:");
                updateUser(id, nameField.getText(), ageField.getText(), weightField.getText(), caloriesField.getText(),
                        waterField.getText(), stepsField.getText(), exerciseField.getText(), sleepField.getText());
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Enter user ID to delete:");
                deleteUser(id);
            }
        });
    }

    // Method to add a user
    private static void addUser(String name, String age, String weight, String calories, String water, String steps,
                                String exercise, String sleep) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO Users (name, age, weight, calories_burned, water_intake, steps_walked, exercise_minutes, sleep_hours) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, Integer.parseInt(age));
            stmt.setDouble(3, Double.parseDouble(weight));
            stmt.setDouble(4, Double.parseDouble(calories));
            stmt.setDouble(5, Double.parseDouble(water));
            stmt.setInt(6, Integer.parseInt(steps));
            stmt.setInt(7, Integer.parseInt(exercise));
            stmt.setDouble(8, Double.parseDouble(sleep));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "User added successfully.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error inserting data: " + e.getMessage());
        }
    }

    // Method to view all users
    private static void viewUsers() {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM Users";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            StringBuilder userList = new StringBuilder("User List:\n");
            while (rs.next()) {
                userList.append("ID: ").append(rs.getInt("id"))
                        .append(", Name: ").append(rs.getString("name"))
                        .append(", Age: ").append(rs.getInt("age"))
                        .append(", Weight: ").append(rs.getDouble("weight"))
                        .append(", Calories Burned: ").append(rs.getDouble("calories_burned"))
                        .append(", Water Intake: ").append(rs.getDouble("water_intake"))
                        .append(", Steps Walked: ").append(rs.getInt("steps_walked"))
                        .append(", Exercise Minutes: ").append(rs.getInt("exercise_minutes"))
                        .append(", Sleep Hours: ").append(rs.getDouble("sleep_hours"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, userList.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage());
        }
    }

    // Method to update a user
    private static void updateUser(String id, String name, String age, String weight, String calories, String water, String steps,
                                   String exercise, String sleep) {
        try (Connection conn = getConnection()) {
            String query = "UPDATE Users SET name = ?, age = ?, weight = ?, calories_burned = ?, water_intake = ?, " +
                           "steps_walked = ?, exercise_minutes = ?, sleep_hours = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, Integer.parseInt(age));
            stmt.setDouble(3, Double.parseDouble(weight));
            stmt.setDouble(4, Double.parseDouble(calories));
            stmt.setDouble(5, Double.parseDouble(water));
            stmt.setInt(6, Integer.parseInt(steps));
            stmt.setInt(7, Integer.parseInt(exercise));
            stmt.setDouble(8, Double.parseDouble(sleep));
            stmt.setInt(9, Integer.parseInt(id));

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "User updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "User not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating data: " + e.getMessage());
        }
    }

    // Method to delete a user
    private static void deleteUser(String id) {
        try (Connection conn = getConnection()) {
            String query = "DELETE FROM Users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(id));

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "User deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "User not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting data: " + e.getMessage());
        }
    }

    // Method to establish a connection with the database
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }
}   