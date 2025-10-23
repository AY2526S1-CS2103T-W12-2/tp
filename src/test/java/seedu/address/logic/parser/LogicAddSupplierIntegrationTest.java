package seedu.address.logic.parser;

// import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

// import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.category.Category;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Supplier;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.UserPrefsStorage;

// import seedu.address.model.ModelManager;
// import seedu.address.model.UserPrefs;

public class LogicAddSupplierIntegrationTest {

    @TempDir
    Path tempDir;

    /**
     * Setup for future integration with Supplier test utils
     *
     * @BeforeEach
     *     public void setUp() {
     *         model = new ModelManager(, );
     *     }
     */

    @Test
    public void execute_addSupplier_createsSupplierInstance() throws Exception {
        Path testAddress = tempDir.resolve("addressbook.json");
        Path testPref = tempDir.resolve("prefs.json");

        AddressBookStorage testAddressBookStorage = new JsonAddressBookStorage(testAddress);
        UserPrefsStorage testUserPrefsStorage = new JsonUserPrefsStorage(testPref);
        StorageManager storageManager = new StorageManager(testAddressBookStorage, testUserPrefsStorage);

        Model model = new ModelManager();
        Logic logic = new LogicManager(model, storageManager);

        String testInput = "add n/John p/91234567 e/johnDoe@email.com a/Chinatown c/Supplier";

        CommandResult result = logic.execute(testInput);

        String feedback = result.getFeedbackToUser().toLowerCase();
        assertTrue(feedback.contains("added"),
                "Expected success message to contain 'added' but was " + feedback);

        Person created = model.getFilteredPersonList().get(0);
        Person expected = new Supplier(new Name("John"),
                new Phone("91234567"),
                new Email("johnDoe@email.com"),
                new Address("Chinatown"),
                new Category("Supplier")
        );

        assertEquals(1, model.getFilteredPersonList().size(), "Exactly one person should be added");
        assertTrue(created instanceof Supplier, "Created person must be a Supplier instance");
        assertEquals(created, expected);
    }
}
