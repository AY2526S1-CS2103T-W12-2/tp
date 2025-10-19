package seedu.address.model.category;

import static java.util.Objects.requireNonNull;


/**
 * Represents a Category in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidCategoryName(String)}
 */
public class Category {

    public static final String MESSAGE_CONSTRAINTS = "Unknown Category (Customer | Supplier | Staff)";
    private static final String[] VALID_VALUES = {"Customer", "Supplier", "Staff"};

    public final String categoryName;

    /**
     * Constructs a {@code Category}.
     *
     * @param categoryName A valid tag name. (Customer, Supplier, or Staff)
     */
    public Category(String categoryName) {
        requireNonNull(categoryName);
        if (!isValidCategoryName(categoryName)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.categoryName = normalize(categoryName);
    }

    /**
     * Getter for category name
     * @return categoryName as String
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Returns true if a given string is a valid category tag name.
     * Case-insensitive check.
     */
    public static boolean isValidCategoryName(String test) {
        if (test == null || test.trim().isEmpty()) {
            return false;
        }
        String lower = test.trim().toLowerCase();
        for (String valid : VALID_VALUES) {
            if (valid.toLowerCase().equals(lower)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts input to Title Case form ("customer" → "Customer").
     * */
    private static String normalize(String input) {
        String lower = input.trim().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Category)) {
            return false;
        }

        Category otherCategory = (Category) other;
        return categoryName.equals(otherCategory.categoryName);
    }

    @Override
    public int hashCode() {
        return categoryName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + categoryName + ']';
    }

}
