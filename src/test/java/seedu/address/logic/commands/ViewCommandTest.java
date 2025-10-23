package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSON_NOT_FOUND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Phone;
/**
 * Contains integration tests (interaction with the Model) for {@code ViewCommand}.
 */
public class ViewCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        Phone firstPhone = new Phone("91234567");
        Phone secondPhone = new Phone("98765432");

        ViewCommand viewFirstCommand = new ViewCommand(firstPhone);
        ViewCommand viewSecondCommand = new ViewCommand(secondPhone);

        // same object -> returns true
        assertTrue(viewFirstCommand.equals(viewFirstCommand));

        // same values -> returns true
        ViewCommand viewFirstCommandCopy = new ViewCommand(firstPhone);
        assertTrue(viewFirstCommand.equals(viewFirstCommandCopy));

        // different types -> returns false
        assertFalse(viewFirstCommand.equals(1));

        // null -> returns false
        assertFalse(viewFirstCommand.equals(null));

        // different phone -> returns false
        assertFalse(viewFirstCommand.equals(viewSecondCommand));
    }

    @Test
    public void constructor_nullPhone_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ViewCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        Phone validPhone = CARL.getPhone();
        ViewCommand viewCommand = new ViewCommand(validPhone);
        assertThrows(NullPointerException.class, () -> viewCommand.execute(null));
    }

    @Test
    public void execute_validPhoneExistingPerson_success() {
        Phone validPhone = CARL.getPhone();
        ViewCommand viewCommand = new ViewCommand(validPhone);

        String expectedMessage = String.format("Viewing: %s", CARL.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        CommandResult expectedResult = new CommandResult(expectedMessage, false, false, true, CARL);
        assertCommandSuccess(viewCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_validPhoneStaffPerson_success() {
        // CARL is a Staff in TypicalPersons
        Phone validPhone = CARL.getPhone();
        ViewCommand viewCommand = new ViewCommand(validPhone);

        String expectedMessage = String.format("Viewing: %s", CARL.getName());
        CommandResult expectedResult = new CommandResult(expectedMessage, false, false, true, CARL);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(viewCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_invalidPhoneNonexistentPerson_failure() {
        Phone nonExistentPhone = new Phone("81234567");
        ViewCommand viewCommand = new ViewCommand(nonExistentPhone);

        assertCommandFailure(viewCommand, model,
            String.format(MESSAGE_PERSON_NOT_FOUND, nonExistentPhone));
    }
}

