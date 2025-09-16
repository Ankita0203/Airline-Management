public class Flight {

    private final int id;      // final because flight ID shouldn't change
    private final String code; // final because flight code shouldn't change

    // Constructor
    public Flight(int id, String code) {
        this.id = id;
        this.code = code;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Getter for flight code (optional)
    public String getCode() {
        return code;
    }

    // Display flight code in JComboBox
    @Override
    public String toString() {
        return code;
    }
}
