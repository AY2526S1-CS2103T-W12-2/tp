package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_STAFF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.StaffBuilder;

/**
 * Unit tests for {@link Staff}.
 */
public class StaffTest {

    @Test
    public void isSameStaff() {
        Staff aliceStaff = new StaffBuilder(ALICE).build();

        // same object -> returns true
        assertTrue(aliceStaff.isSameStaff(aliceStaff));

        // null -> returns false
        assertFalse(aliceStaff.isSameStaff(null));

        // same name, all other attributes different -> returns true
        Staff editedAlice = new StaffBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .withCategories(VALID_CATEGORY_STAFF)
                .withShift("PM")
                .build();
        assertTrue(aliceStaff.isSameStaff(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new StaffBuilder(ALICE)
                .withName(VALID_NAME_BOB)
                .build();
        assertFalse(aliceStaff.isSameStaff(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Staff editedBob = new StaffBuilder(BOB)
                .withName(VALID_NAME_BOB.toLowerCase())
                .build();
        assertFalse(new StaffBuilder(BOB).build().isSameStaff(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new StaffBuilder(BOB)
                .withName(nameWithTrailingSpaces)
                .build();
        assertFalse(new StaffBuilder(BOB).build().isSameStaff(editedBob));
    }

    @Test
    public void equals() {
        Staff aliceStaff = new StaffBuilder(ALICE).build();
        Staff bobStaff = new StaffBuilder(BOB).build();

        // same values -> returns true
        Staff aliceCopy = new StaffBuilder(ALICE).build();
        assertTrue(aliceStaff.equals(aliceCopy));

        // same object -> returns true
        assertTrue(aliceStaff.equals(aliceStaff));

        // null -> returns false
        assertFalse(aliceStaff.equals(null));

        // different type -> returns false
        assertFalse(aliceStaff.equals(5));

        // different staff -> returns false
        assertFalse(aliceStaff.equals(bobStaff));

        // different name -> returns false
        Staff editedAlice = new StaffBuilder(ALICE)
                .withName(VALID_NAME_BOB)
                .build();
        assertFalse(aliceStaff.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new StaffBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .build();
        assertFalse(aliceStaff.equals(editedAlice));

        // different email -> returns false
        editedAlice = new StaffBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        assertFalse(aliceStaff.equals(editedAlice));

        // different address -> returns false
        editedAlice = new StaffBuilder(ALICE)
                .withAddress(VALID_ADDRESS_BOB)
                .build();
        assertFalse(aliceStaff.equals(editedAlice));

        // different categories -> returns false
        editedAlice = new StaffBuilder(ALICE)
                .withCategories(VALID_CATEGORY_STAFF)
                .build();
        assertFalse(aliceStaff.equals(editedAlice));

        // different shift -> returns false
        editedAlice = new StaffBuilder(ALICE)
                .withShift("PM")
                .build();
        assertFalse(aliceStaff.equals(editedAlice));

        // different leaves -> returns false
        editedAlice = new StaffBuilder(ALICE).build();
        editedAlice.removeLeaves(1);
        assertFalse(aliceStaff.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        Staff aliceStaff = new StaffBuilder(ALICE).build();
        String expected = "Staff{name=" + aliceStaff.getName()
                + ", phone=" + aliceStaff.getPhone()
                + ", email=" + aliceStaff.getEmail()
                + ", address=" + aliceStaff.getAddress()
                + ", category=" + aliceStaff.getCategory()
                + ", shift=" + aliceStaff.getShift()
                + ", numberOfLeaves=" + aliceStaff.getNumberOfLeaves() + "}";
        assertEquals(expected, aliceStaff.toString());
    }

    @Test
    public void shiftAndLeavesMethods() {
        Staff staff = new StaffBuilder(ALICE).build();

        // Default shift is AM
        assertEquals("AM", staff.getShift().toString());

        // Change shift using string
        staff.setShift("PM");
        assertEquals("PM", staff.getShift().toString());

        // Change shift using Shift object
        staff.setShift(new Shift("AM"));
        assertEquals("AM", staff.getShift().toString());

        // Leaves - default 14
        assertEquals(14, staff.getNumberOfLeaves());

        // Remove some leaves successfully
        assertTrue(staff.removeLeaves(5));
        assertEquals(9, staff.getNumberOfLeaves());

        // Add leaves
        staff.addLeaves(3);
        assertEquals(12, staff.getNumberOfLeaves());

        // Attempt to remove more leaves than available
        assertFalse(staff.removeLeaves(20));
        assertEquals(12, staff.getNumberOfLeaves());

        // Attempt to remove negative leaves should throw exception
        Exception removeException = assertThrows(IllegalArgumentException.class, ()
                -> staff.removeLeaves(-1));
        assertEquals("Number of leaves to remove must be positive.", removeException.getMessage());

        // Attempt to add negative leaves should throw exception
        Exception addException = assertThrows(IllegalArgumentException.class, ()
                -> staff.addLeaves(-1));
        assertEquals("Number of leaves to add must be positive.", addException.getMessage());
    }

    @Test
    public void builderWithShiftWorks() {
        Staff staffPM = new StaffBuilder(ALICE)
                .withShift("PM")
                .build();
        assertEquals("PM", staffPM.getShift().toString());

        Staff staffAM = new StaffBuilder(ALICE)
                .withShift("AM")
                .build();
        assertEquals("AM", staffAM.getShift().toString());
    }
}
