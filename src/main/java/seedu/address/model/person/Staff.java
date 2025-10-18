package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.model.category.Category;

/**
 * Represents a staff member in the address book.
 * Each staff has a name, contact details, category, shift (AM/PM), and number of leaves.
 */
public class Staff extends Person {

    private int numberOfLeaves = 14; // Default 14 leaves
    private Shift shift; // AM or PM shift (modifiable)

    /**
     * Constructs a {@code Staff} object with all required fields.
     *
     * @param name     Name of the staff
     * @param phone    Phone number of the staff
     * @param email    Email address of the staff
     * @param address  Residential address of the staff
     * @param category Category of the staff
     * @param shift    Work shift of the staff ("AM" or "PM")
     */
    public Staff(Name name, Phone phone, Email email, Address address, Category category, Shift shift) {
        super(name, phone, email, address, category);
        requireAllNonNull(name, phone, email, address, category, shift);
        this.shift = shift;
    }

    /**
     * Returns the staff's current shift.
     *
     * @return The current {@code Shift} of the staff.
     */
    public Shift getShift() {
        return shift;
    }

    /**
     * Updates the staff's shift to a new value using a Shift object.
     *
     * @param newShift The new shift (non-null).
     */
    public void setShift(Shift newShift) {
        requireAllNonNull(newShift);
        this.shift = newShift;
    }

    /**
     * Updates the staff's shift using a string value ("AM" or "PM").
     *
     * @param newShift The new shift string ("AM" or "PM").
     */
    public void setShift(String newShift) {
        requireAllNonNull(newShift);
        this.shift = new Shift(newShift); // validates automatically
    }

    /**
     * Returns the remaining number of leaves for the staff.
     *
     * @return The number of available leaves.
     */
    public int getNumberOfLeaves() {
        return numberOfLeaves;
    }

    /**
     * Deducts leaves if available.
     * Returns true if successful, false if not enough leaves.
     *
     * @param leavesToRemove Number of leaves to remove.
     * @return true if removal successful, false otherwise.
     */
    public boolean removeLeaves(int leavesToRemove) {
        if (leavesToRemove <= 0) {
            throw new IllegalArgumentException("Number of leaves to remove must be positive.");
        }
        if (leavesToRemove > numberOfLeaves) {
            return false;
        }
        numberOfLeaves -= leavesToRemove;
        return true;
    }

    /**
     * Adds leaves (for example, resetting annual leave).
     *
     * @param leavesToAdd Number of leaves to add.
     */
    public void addLeaves(int leavesToAdd) {
        if (leavesToAdd <= 0) {
            throw new IllegalArgumentException("Number of leaves to add must be positive.");
        }
        numberOfLeaves += leavesToAdd;
    }

    /**
     * Returns true if both staff members have the same name.
     * This defines a weaker notion of equality between two staff.
     *
     * @param otherPerson Another {@code Staff} object.
     * @return true if both have the same name.
     */
    public boolean isSameStaff(Staff otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Staff otherStaff)) {
            return false;
        }

        return otherStaff.getName().equals(getName())
                && otherStaff.getPhone().equals(getPhone())
                && otherStaff.getEmail().equals(getEmail())
                && otherStaff.getAddress().equals(getAddress())
                && otherStaff.getCategory().equals(getCategory())
                && otherStaff.getShift().equals(getShift())
                && otherStaff.getNumberOfLeaves() == getNumberOfLeaves();
    }

    @Override
    public String toString() {
        return "Staff{name=" + getName()
                + ", phone=" + getPhone()
                + ", email=" + getEmail()
                + ", address=" + getAddress()
                + ", category=" + getCategory()
                + ", shift=" + shift
                + ", numberOfLeaves=" + numberOfLeaves + "}";
    }
}
