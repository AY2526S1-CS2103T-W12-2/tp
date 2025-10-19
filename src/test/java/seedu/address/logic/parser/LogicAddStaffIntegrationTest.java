package seedu.address.logic.parser;

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
import seedu.address.model.person.Person;
import seedu.address.model.person.Staff;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class LogicAddStaffIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    void execute_addStaff_createsStaffInstance() throws Exception {
        Path addr = tempDir.resolve("addressbook.json");
        Path prefs = tempDir.resolve("prefs.json");
        StorageManager storage = new StorageManager(
                new JsonAddressBookStorage(addr),
                new JsonUserPrefsStorage(prefs));

        Model model = new ModelManager(); // empty state
        Logic logic = new LogicManager(model, storage);

        String input = "add n/Alex Tan p/81234567 e/alex@example.com "
                + "a/1 Fusionopolis Way, Singapore c/staff";

        CommandResult result = logic.execute(input);

        String feedback = result.getFeedbackToUser().toLowerCase();
        assertTrue(feedback.contains("added"), "Expected success message to contain 'added' but was: " + feedback);

        assertEquals(1, model.getFilteredPersonList().size(), "Exactly one person should be added");
        Person created = model.getFilteredPersonList().get(0);
        assertTrue(created instanceof Staff, "Created person must be a Staff instance");
        assertEquals("Alex Tan", created.getName().fullName);
    }
}

