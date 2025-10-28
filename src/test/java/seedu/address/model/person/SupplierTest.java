package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_STAFF;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.TypicalSuppliers.ALICE;
import static seedu.address.testutil.TypicalSuppliers.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.model.category.Category;
import seedu.address.testutil.SupplierBuilder;

public class SupplierTest {

    @Test
    public void testaddOrder() {
        Phone testPhone = new Phone("82192309");
        Name testName = new Name("Grace Chan");
        Email testEmail = new Email("grace01@gmail.com");
        Address testAddress = new Address("Block 416 Bukit Batok Drive");
        Category testCat = new Category("Supplier");
        Supplier test = new Supplier(testName, testPhone, testEmail, testAddress, testCat);
        Order test1 = new Order(new ItemName("Computer"), new ItemQuantity("5"), new ItemUnitPrice("100.00"),
                "every Tuesday");
        Order test2 = new Order(new ItemName("Computer"), new ItemQuantity("10"), new ItemUnitPrice("120.00"),
                "every Wednesday");
        test.addOrder(test1);
        test.addOrder(test2);
        String expected = "5 of Computer (at $100.00 each) to be delivered every Tuesday"
              + '\n' + "10 of Computer (at $120.00 each) to be delivered every Wednesday";
        assertEquals(expected, test.listOrders());
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Supplier editedAlice = new SupplierBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withCategory(VALID_CATEGORY_STAFF).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same phone number, all other attributes different -> returns true
        editedAlice = new SupplierBuilder(ALICE).withName(VALID_NAME_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withCategory(VALID_CATEGORY_STAFF).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, different phone number, all other attributes same -> returns false
        editedAlice = new SupplierBuilder(ALICE).withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Supplier editedBob = new SupplierBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns true
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new SupplierBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new SupplierBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new SupplierBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new SupplierBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new SupplierBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new SupplierBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Supplier.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress()
                + ", category=" + ALICE.getCategory() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
