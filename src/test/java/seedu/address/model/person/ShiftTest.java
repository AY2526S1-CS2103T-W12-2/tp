package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Shift}.
 */
public class ShiftTest {

    @Test
    public void constructor_validShift_success() {
        Shift amShift = new Shift("AM");
        assertEquals("AM", amShift.getValue());

        Shift pmShift = new Shift("PM");
        assertEquals("PM", pmShift.getValue());

        // lower case input
        Shift amLower = new Shift("am");
        assertEquals("AM", amLower.getValue());

        Shift pmLower = new Shift("pm");
        assertEquals("PM", pmLower.getValue());

        // spaces are trimmed
        Shift amWithSpaces = new Shift("  AM  ");
        assertEquals("AM", amWithSpaces.getValue());
    }

    @Test
    public void constructor_invalidShift_throwsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> new Shift("A"));

        assertThrows(IllegalArgumentException.class, ()
                -> new Shift("PMM"));

        assertThrows(IllegalArgumentException.class, ()
                -> new Shift(""));

        assertThrows(IllegalArgumentException.class, ()
                -> new Shift(" "));

        assertThrows(NullPointerException.class, ()
                -> new Shift(null));
    }

    @Test
    public void equals_andHashCode() {
        Shift am1 = new Shift("AM");
        Shift am2 = new Shift("am");
        Shift pm = new Shift("PM");

        // reflexive
        assertTrue(am1.equals(am1));

        // same value
        assertTrue(am1.equals(am2));
        assertEquals(am1.hashCode(), am2.hashCode());

        // different value
        assertFalse(am1.equals(pm));
        assertFalse(pm.equals(am1));

        // null and different type
        assertFalse(am1.equals(null));
        assertFalse(am1.equals("AM"));
    }

    @Test
    public void toString_returnsValue() {
        Shift amShift = new Shift("AM");
        Shift pmShift = new Shift("PM");

        assertEquals("AM", amShift.toString());
        assertEquals("PM", pmShift.toString());
    }
}
