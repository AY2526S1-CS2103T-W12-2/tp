package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_SUPPLIER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.Supplier;

/**
 * A utility class containing a list of {@code Supplier} objects to be used in tests.
 */
public class TypicalSuppliers {

    public static final Supplier ALICE = new SupplierBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253").withCategory("Supplier")
            .withItem("Apples").build();
    public static final Supplier BENSON = new SupplierBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25").withEmail("johnd@example.com")
            .withPhone("98765432").withCategory("Supplier")
            .build();
    public static final Supplier CARL = new SupplierBuilder().withName("Carl Kurz")
            .withPhone("95352563").withEmail("heinz@example.com")
            .withAddress("wall street").withCategory("Supplier")
            .withItem("Eggs").build();
    public static final Supplier DANIEL = new SupplierBuilder().withName("Daniel Meier")
            .withPhone("87652533").withEmail("cornelia@example.com")
            .withAddress("10th street").withCategory("Supplier")
            .build();
    public static final Supplier ELLE = new SupplierBuilder().withName("Elle Meyer")
            .withPhone("94822241").withEmail("werner@example.com")
            .withAddress("michegan ave").withCategory("Supplier")
            .build();
    public static final Supplier FIONA = new SupplierBuilder().withName("Fiona Kunz")
            .withPhone("94824271").withEmail("lydia@example.com")
            .withAddress("little tokyo").withCategory("Supplier")
            .withItem("Sirloin").build();
    public static final Supplier GEORGE = new SupplierBuilder().withName("George Best")
            .withPhone("94824421").withEmail("anna@example.com")
            .withAddress("4th street").withCategory("Supplier")
            .withItem("Potatoes").build();

    // Manually added
    public static final Supplier HOON = new SupplierBuilder().withName("Hoon Meier")
            .withPhone("84824241").withEmail("stefan@example.com")
            .withAddress("little india").withCategory("Supplier")
            .build();
    public static final Supplier IDA = new SupplierBuilder().withName("Ida Mueller")
            .withPhone("84821311").withEmail("hans@example.com")
            .withAddress("chicago ave").withCategory("Supplier")
            .build();

    // Manually added - Supplier's details found in {@code CommandTestUtil}
    public static final Supplier AMY = new SupplierBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
            .withCategory(VALID_CATEGORY_SUPPLIER)
            .build();
    public static final Supplier BOB = new SupplierBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
            .withCategory(VALID_CATEGORY_SUPPLIER).withItem("Apple")
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalSuppliers() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalSuppliers()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalSuppliers() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
