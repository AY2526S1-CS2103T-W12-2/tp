package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("911")); // less than 8 digits
        assertFalse(Phone.isValidPhone("12345678")); // starts with invalid digit
        assertFalse(Phone.isValidPhone("8123456")); // 7 digits only
        assertFalse(Phone.isValidPhone("912345678")); // 9 digits
        assertFalse(Phone.isValidPhone("91234abc")); // alphabets
        assertFalse(Phone.isValidPhone("9123 5678")); // spaces within digits

        // valid phone numbers
        assertTrue(Phone.isValidPhone("91234567")); // starts with 9
        assertTrue(Phone.isValidPhone("81234567")); // starts with 8
        assertTrue(Phone.isValidPhone("61234567")); // starts with 6
    }

    @Test
    public void equals() {
        Phone phone = new Phone("91234567");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("91234567")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("81234567")));
    }
}
