package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddressCharacters(String)}
 * and {@link #isValidAddressLength(String)}.
 */
public class Address {

    public static final String INVALID_CHARACTER_MESSAGE_CONSTRAINTS = "Invalid characters found"
            + " - only letters, apostrophes, hyphens, hashes and spaces are allowed";

    public static final String INVALID_ADDRESS_LENGTH_MESSAGE_CONSTRAINTS = "Length of Address does not match criteria"
            + " - Address must be between 2 - 100 characters";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Only letters, apostrophes, hyphens, hashes and spaces are allowed
     */
    public static final String CHARACTER_VALIDATION_REGEX = "[a-zA-Z0-9'#\\-,;.][a-zA-Z0-9'#\\-,;. ]*$";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Must be between 2 - 100 characters inclusive.
     */
    public static final String ADDRESS_LENGTH_VALIDATION_REGEX = "^.{2,100}$";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        checkArgument(isValidAddressCharacters(address), INVALID_ADDRESS_LENGTH_MESSAGE_CONSTRAINTS);
        checkArgument(isValidAddressLength(address), INVALID_CHARACTER_MESSAGE_CONSTRAINTS);
        value = address;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidAddressCharacters(String test) {
        return test.matches(CHARACTER_VALIDATION_REGEX);
    }

    public static boolean isValidAddressLength(String test) {
        return test.matches(ADDRESS_LENGTH_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Address)) {
            return false;
        }

        Address otherAddress = (Address) other;
        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
