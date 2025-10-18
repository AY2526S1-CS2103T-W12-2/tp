package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Staff's work shift (AM or PM).
 * Immutable and must be either "AM" or "PM".
 */
public class Shift {

    public static final String MESSAGE_CONSTRAINTS =
            "Shift must be either 'AM' or 'PM' (case-insensitive).";

    private final String value;

    /**
     * Constructs a {@code Shift}.
     *
     * @param shift A valid shift (AM or PM)
     */
    public Shift(String shift) {
        requireNonNull(shift);
        String normalized = shift.trim().toUpperCase();

        if (!normalized.equals("AM") && !normalized.equals("PM")) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Shift
                && value.equals(((Shift) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
