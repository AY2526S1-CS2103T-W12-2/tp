package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Objects;
import java.util.ArrayList;
import java.util.stream.Collectors;

//import seedu.address.commons.util.ToStringBuilder;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.category.Category;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Supplier extends Person {

    // Identity fields
    private final String item;
    private final ArrayList<Order> orders;
    /**
     * Every field must be present and not null.
     * @param item item that the supplier supplies, not the order itself.
     */
    public Supplier(Name name, Phone phone, Email email, Address address, Category category, String item) {
        super(name, phone, email, address, category);
        requireAllNonNull(name, phone, email, address, category);
        this.item = item;
        this.orders = new ArrayList<>();
    }

    /**
     * Returns the item supplied by this supplier.
     * @return the item string, or empty string if no item is set
     */
    public String getItem() {
        return this.item == null ? "" : this.item;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameSupplier(Supplier otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Supplier)) {
            return false;
        }

        Supplier otherPerson = (Supplier) other;
        return super.equals(other) && otherPerson.item.equals(this.item);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", super.getName())
                .add("phone", super.getPhone())
                .add("email", super.getEmail())
                .add("address", super.getAddress())
                .add("category", super.getCategory())
                .add("item", this.item)
                .toString();
    }

    /**
     * Takes in a new order to be placed and adds it to the list
     * @param newOrder Order to be placed
     */
    public void addOrder(Order newOrder) {
        orders.add(newOrder);
    }

    /**
     * Returns a string content the orders placed under supplier
     * @return String of orders in list
     */
    public String listOrders() {
        return orders.stream()
                .map(Order::toString)
                .collect(Collectors.joining("\n"));
    }

    public int getSize() {
        return this.orders.size();
    }

    public Order getOrder(int index) {
        return this.orders.get(index - 1);
    }

    public void updateOrders(int index, Order newOrder) {
        this.orders.set(index - 1, newOrder);
    }


}
