package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;


/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Unknown Category (Customer | Supplier | Staff)";
    private static final String[] VALID_VALUES = {"Customer", "Supplier", "Staff"};


    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name. (Customer, Supplier, or Staff)
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        if (!isValidTagName(tagName)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.tagName = normalize(tagName);
    }

    /**
     * Returns true if a given string is a valid category tag name.
     * Case-insensitive check.
     */
    public static boolean isValidTagName(String test) {
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

    /** Converts input to Title Case form ("customer" → "Customer"). */
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
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
