package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    @Test
    public void isValidAddressCharacters() {
        // null address
        assertThrows(NullPointerException.class, () -> Address.isValidAddressCharacters(null));

        // invalid addresses
        assertFalse(Address.isValidAddressCharacters("")); // empty string
        assertFalse(Address.isValidAddressCharacters(" ")); // spaces only
        assertFalse(Address.isValidAddressCharacters(" 123 Main St")); // leading whitespace
        assertFalse(Address.isValidAddressCharacters("@Home")); // symbol not allowed
        assertFalse(Address.isValidAddressCharacters("123%Street")); // % not allowed
        assertFalse(Address.isValidAddressCharacters("Main$Street")); // $ not allowed
        assertFalse(Address.isValidAddressCharacters("!Hill Road")); // ! not allowed
        assertFalse(Address.isValidAddressCharacters("Blk_12 Road")); // _ not allowed

        // valid addresses
        assertTrue(Address.isValidAddressCharacters("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidAddressCharacters("-")); // one character
        assertTrue(Address.isValidAddressCharacters("Leng Inc; 1234 Market St;"
                + "San Francisco CA 2349879; USA")); // long address
        assertTrue(Address.isValidAddressCharacters("A")); // single valid character
        assertTrue(Address.isValidAddressCharacters("Zion Street 123")); // alphanumeric ok
    }

    @Test
    public void isValidAddressLength() {
        // null address
        assertThrows(NullPointerException.class, () -> Address.isValidAddressLength(null));

        // invalid addresses
        assertFalse(Address.isValidAddressLength("")); // empty string
        assertFalse(Address.isValidAddressLength(" ")); // spaces only
        assertFalse(Address.isValidAddressLength("A")); // too short (1 char)
        assertFalse(Address.isValidAddressLength("A".repeat(101))); // too long (101 chars)

        // valid addresses
        assertTrue(Address.isValidAddressLength("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidAddressLength("AB")); // one character
        assertTrue(Address.isValidAddressLength("A".repeat(100))); // max valid length (100 chars)
        assertTrue(Address.isValidAddressLength("Leng Inc; 1234 Market St; "
                + "San Francisco CA 2349879; USA")); // long address
    }

    @Test
    public void equals() {
        Address address = new Address("Valid Address");

        // same values -> returns true
        assertTrue(address.equals(new Address("Valid Address")));

        // same object -> returns true
        assertTrue(address.equals(address));

        // null -> returns false
        assertFalse(address.equals(null));

        // different types -> returns false
        assertFalse(address.equals(5.0f));

        // different values -> returns false
        assertFalse(address.equals(new Address("Other Valid Address")));
    }
}
