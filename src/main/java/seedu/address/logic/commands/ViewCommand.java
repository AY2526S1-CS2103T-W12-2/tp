package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Views a person's details in a popup given their phone number.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views a person's details by phone number.\n"
        + "Parameters: " + PREFIX_PHONE + "PHONE\n"
        + "Example: " + COMMAND_WORD + " " + PREFIX_PHONE + "91234567";
    public static final String ERROR_EXTENSION = " Try running 'list' before using the command again.";
    public static final String MESSAGE_EMPTY_LIST = "Empty contact list: No contacts available to update!";

    private final Phone targetPhone;

    /**
     * Creates a ViewCommand to view a person by phone number.
     *
     * @param phone the phone number of the person to view
     */
    public ViewCommand(Phone phone) {
        requireNonNull(phone);
        this.targetPhone = phone;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ObservableList<Person> fullList = model.getAddressBook().getPersonList();
        if (fullList.isEmpty()) {
            throw new CommandException(MESSAGE_EMPTY_LIST);
        }

        Optional<Person> match = model.getFilteredPersonList().stream()
                .filter(p -> p.getPhone().equals(targetPhone))
                .findFirst();

        if (!match.isPresent() && fullList.size() != model.getFilteredPersonList().size()) {
            throw new CommandException(String.format(Messages.MESSAGE_PERSON_NOT_FOUND + ERROR_EXTENSION, targetPhone));
        }

        if (!match.isPresent()) {
            throw new CommandException(String.format(Messages.MESSAGE_PERSON_NOT_FOUND, targetPhone));
        }

        Person found = match.get();
        return new CommandResult(String.format("Viewing: %s", found.getName()), false, false, true, found);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ViewCommand)) {
            return false;
        }
        ViewCommand o = (ViewCommand) other;
        return targetPhone.equals(o.targetPhone);
    }
}
