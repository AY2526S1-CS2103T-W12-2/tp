package seedu.address.testutil;

import seedu.address.model.person.Person;
import seedu.address.model.person.Shift;
import seedu.address.model.person.Staff;

/**
 * A utility class to help with building {@link Staff} objects.
 * Inherits from {@link PersonBuilder}.
 */
public class StaffBuilder extends PersonBuilder {

    private Shift shift;

    /**
     * Creates a default {@code StaffBuilder} with default shift AM.
     */
    public StaffBuilder() {
        super();
        this.shift = new Shift("AM"); // Default shift
    }

    /**
     * Creates a {@code StaffBuilder} using an existing {@code Person} to copy fields.
     * The shift defaults to AM.
     *
     * @param personToCopy The person to copy fields from.
     */
    public StaffBuilder(Person personToCopy) {
        super(personToCopy);
        this.shift = new Shift("AM");
    }

    /**
     * Sets the shift for the staff being built.
     *
     * @param shiftString either "AM" or "PM"
     * @return this builder
     */
    public StaffBuilder withShift(String shiftString) {
        this.shift = new Shift(shiftString);
        return this;
    }

    @Override
    public Staff build() {
        return new Staff(name, phone, email, address, category, shift);
    }

    @Override
    public StaffBuilder withName(String name) {
        super.withName(name);
        return this;
    }

    @Override
    public StaffBuilder withPhone(String phone) {
        super.withPhone(phone);
        return this;
    }

    @Override
    public StaffBuilder withEmail(String email) {
        super.withEmail(email);
        return this;
    }

    @Override
    public StaffBuilder withAddress(String address) {
        super.withAddress(address);
        return this;
    }

    @Override
    public StaffBuilder withCategories(String category) {
        super.withCategories(category);
        return this;
    }
}
