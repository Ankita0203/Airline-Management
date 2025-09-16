import java.awt.*;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;

public class AirlineManagement extends JFrame {

    private final JComboBox<Flight> flightList;
    private final DefaultComboBoxModel<Flight> flightModel = new DefaultComboBoxModel<>();
    private final JTextField nameField;
    private final JSpinner dateSpinner;
    private final JTextArea output;

    public AirlineManagement() {
        super("Airline Management");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Booking Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        // Flight combo box
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Choose Flight:"), gbc);
        flightList = new JComboBox<>(flightModel);
        gbc.gridx = 1;
        inputPanel.add(flightList, gbc);

        // Date picker using JSpinner
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Date:"), gbc);

        Date today = new Date();
        SpinnerDateModel model = new SpinnerDateModel(today, today, null, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(model);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        inputPanel.add(dateSpinner, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton loadFlightsBtn = new JButton("Load Flights");
        loadFlightsBtn.addActionListener(_ignored -> loadFlights());
        buttonPanel.add(loadFlightsBtn);

        JButton bookBtn = new JButton("Book");
        bookBtn.addActionListener(_ignored -> book());
        buttonPanel.add(bookBtn);

        // Output area
        output = new JTextArea(5, 35);
        output.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(output);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadFlights() {
        flightModel.removeAllElements();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT flight_id, flight_code FROM flights")) {

            boolean hasData = false;
            while (rs.next()) {
                int id = rs.getInt("flight_id");
                String code = rs.getString("flight_code");
                flightModel.addElement(new Flight(id, code));
                hasData = true;
            }
            output.setText(hasData ? "Flights loaded successfully." : "No flights found.");
        } catch (SQLException ex) {
            output.setText("Error loading flights: " + ex.getMessage());
        }
    }

    private void book() {
        try (Connection c = DBConnection.getConnection()) {
            String name = nameField.getText().trim();
            Flight selectedFlight = (Flight) flightList.getSelectedItem();
            if (name.isEmpty() || selectedFlight == null) {
                output.setText("Please enter a name and select a flight.");
                return;
            }

            // Use java.util.Date for spinner value
            java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
            java.util.Date today = new java.util.Date();
            if (selectedDate.before(today)) {
                output.setText("Cannot select a past date.");
                return;
            }

            // Convert to java.sql.Date for database
            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

            String sql = "INSERT INTO bookings (passenger_name, flight_id, travel_date) VALUES (?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, selectedFlight.getId());
                ps.setDate(3, sqlDate);
                ps.executeUpdate();
                output.setText("Booking confirmed for " + name + " on " + sqlDate);
            }
        } catch (SQLException ex) {
            output.setText("Error booking flight: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AirlineManagement::new);
    }
}

// Flight class
class Flight {
    private final int id;
    private final String code;
    public Flight(int id, String code) { this.id = id; this.code = code; }
    public int getId() { return id; }
    @Override public String toString() { return code; }
}

// DBConnection class
class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/airline?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "MYSQL@2004";
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new SQLException("MySQL JDBC Driver not found.", e); }
        return DriverManager.getConnection(url, user, password);
    }
}
