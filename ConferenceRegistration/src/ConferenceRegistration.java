import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class ConferenceRegistration extends JFrame {
    private JTextField idField, nameField, emailField, mobileField, countryField;
    private JTextArea addressArea;
    private JComboBox<String> genderCombo, designationCombo;
    private JButton addButton, editButton, deleteButton, displayButton, searchButton, exitButton;

    public ConferenceRegistration() {
        setTitle("Conference Registration");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 2));

        // input fields
        add(new JLabel("ID (for Edit/Delete/Search):"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Gender:"));
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        add(genderCombo);

        add(new JLabel("Designation:"));
        designationCombo = new JComboBox<>(new String[]{"General", "Student"});
        add(designationCombo);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Mobile:"));
        mobileField = new JTextField();
        add(mobileField);

        add(new JLabel("Country:"));
        countryField = new JTextField();
        add(countryField);

        add(new JLabel("Address:"));
        addressArea = new JTextArea();
        add(new JScrollPane(addressArea));

        // buttons
        addButton = new JButton("Add");
        add(addButton);

        editButton = new JButton("Edit");
        add(editButton);

        deleteButton = new JButton("Delete");
        add(deleteButton);

        displayButton = new JButton("Display All");
        add(displayButton);

        searchButton = new JButton("Search by ID");
        add(searchButton);

        exitButton = new JButton("Exit");
        add(exitButton);

        // button actions
        addButton.addActionListener(this::addRegistration);
        editButton.addActionListener(this::editRegistration);
        deleteButton.addActionListener(this::deleteRegistration);
        displayButton.addActionListener(this::displayRegistrations);
        searchButton.addActionListener(this::searchRegistration);
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void addRegistration(ActionEvent e) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO registrations (name, gender, designation, email, mobile, country, address, fee) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nameField.getText());
            stmt.setString(2, (String) genderCombo.getSelectedItem());
            stmt.setString(3, (String) designationCombo.getSelectedItem());
            stmt.setString(4, emailField.getText());
            stmt.setString(5, mobileField.getText());
            stmt.setString(6, countryField.getText());
            stmt.setString(7, addressArea.getText());


            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration Successful. Fee: " + (fee * 74) + " INR");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editRegistration(ActionEvent e) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "UPDATE registrations SET name=?, gender=?, designation=?, email=?, mobile=?, country=?, address=?, fee=? WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nameField.getText());
            stmt.setString(2, (String) genderCombo.getSelectedItem());
            stmt.setString(3, (String) designationCombo.getSelectedItem());
            stmt.setString(4, emailField.getText());
            stmt.setString(5, mobileField.getText());
            stmt.setString(6, countryField.getText());
            stmt.setString(7, addressArea.getText());
            stmt.setInt(9, Integer.parseInt(idField.getText()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Record Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Record Not Found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteRegistration(ActionEvent e) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "DELETE FROM registrations WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(idField.getText()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Record Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Record Not Found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void displayRegistrations(ActionEvent e) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM registrations";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            JTextArea displayArea = new JTextArea();
            displayArea.append("ID\tName\tGender\tDesignation\tEmail\tMobile\tCountry\tAddress\tFee\n");
            while (rs.next()) {
                displayArea.append(rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("gender") + "\t" +
                        rs.getString("designation") + "\t" +
                        rs.getString("email") + "\t" +
                        rs.getString("mobile") + "\t" +
                        rs.getString("country") + "\t" +
                        rs.getString("address") + "\t" +
                        rs.getInt("fee") + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(displayArea), "All Registrations", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void searchRegistration(ActionEvent e) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM registrations WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(idField.getText()));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                genderCombo.setSelectedItem(rs.getString("gender"));
                designationCombo.setSelectedItem(rs.getString("designation"));
                emailField.setText(rs.getString("email"));
                mobileField.setText(rs.getString("mobile"));
                countryField.setText(rs.getString("country"));
                addressArea.setText(rs.getString("address"));
                JOptionPane.showMessageDialog(this, "Record Found!");
            } else {
                JOptionPane.showMessageDialog(this, "Record Not Found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConferenceRegistration().setVisible(true));
    }
}


    // Display Action
    private class DisplayAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (Connection connection = DatabaseUtil.getConnection()) {
                String sql = "SELECT * FROM registrations";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                JTextArea displayArea = new JTextArea();
                displayArea.append("ID\tName\tGender\tDesignation\tEmail\tMobile\tCountry\tAddress\tFee\n");
                while (rs.next()) {
                    displayArea.append(rs.getInt("id") + "\t" +
                            rs.getString("name") + "\t" +
                            rs.getString("gender") + "\t" +
                            rs.getString("designation") + "\t" +
                            rs.getString("email") + "\t" +
                            rs.getString("mobile") + "\t" +
                            rs.getString("country") + "\t" +
                            rs.getString("address") + "\t" +
                            rs.getInt("fee") + "\n");
                }
                JOptionPane.showMessageDialog(null, new JScrollPane(displayArea), "Registration Records", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConferenceRegistration().setVisible(true));
    }
}

// Utility Class for Database Connection
class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/conference_registration";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
