package seedu.address.testutil;

import seedu.address.model.category.Category;
import seedu.address.model.person.Person;
import seedu.address.model.person.Supplier;

/**
 * A utility class to help with building {@link Supplier} objects.
 * Inherits from {@link PersonBuilder}.
 */
public class SupplierBuilder extends PersonBuilder {

    private String item = "Chicken";

    /**
     * Creates a default {@code SupplierBuilder} with default item Chicken.
     */
    public SupplierBuilder() {
        super();
    }

    /**
     * Creates a {@code SupplierBuilder} using an existing {@code Person} to copy fields.
     * The item defaults to Chicken.
     *
     * @param personToCopy The person to copy fields from.
     */
    public SupplierBuilder(Person personToCopy) throws IllegalArgumentException {
        super(personToCopy);

        // Defensive check: only allow Person with Supplier category
        if (personToCopy instanceof Supplier) {
            Supplier supplier = (Supplier) personToCopy;
            this.category = new Category("Supplier");
        } else if (personToCopy.getCategory().categoryName.equalsIgnoreCase("Supplier")) {
            this.item = "Chicken";
            this.category = new Category("Supplier");
        } else {
            throw new IllegalArgumentException("Person to copy is not a Supplier");
        }
    }

    @Override
    public Supplier build() {
        return new Supplier(name, phone, email, address, category);
    }

    @Override
    public SupplierBuilder withName(String name) {
        super.withName(name);
        return this;
    }

    @Override
    public SupplierBuilder withPhone(String phone) {
        super.withPhone(phone);
        return this;
    }

    @Override
    public SupplierBuilder withEmail(String email) {
        super.withEmail(email);
        return this;
    }

    @Override
    public SupplierBuilder withAddress(String address) {
        super.withAddress(address);
        return this;
    }

    @Override
    public SupplierBuilder withCategory(String category) {
        super.withCategory("Supplier"); // always Supplier
        return this;
    }
}
