package seedu.address.model.person;

/**
 * enum based on the different categories of addresses
 */
public enum Category {
    CUSTOMER("Customer"),
    SUPPLIER("Supplier"),
    STAFF("Staff");

    public static final String MESSAGE_CONSTRAINTS =
            "Unknown Category (Customer | Supplier | Staff)";

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Takes input and return corresponding category, null if no categories match
     * @param input Category to be matched from string
     * @return corresponding category
     */
    public static Category fromString(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }

        String lowerCase = input.trim().toLowerCase();
        for (Category c: values()) {
            if (c.displayName.toLowerCase().equals(lowerCase)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
