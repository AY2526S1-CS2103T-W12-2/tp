package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERYDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDERNUM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNITPRICE;

import java.util.Objects;
import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.ItemDeliveryDay;
import seedu.address.model.person.ItemName;
import seedu.address.model.person.ItemQuantity;
import seedu.address.model.person.ItemUnitPrice;
import seedu.address.model.person.Order;
import seedu.address.model.person.OrderIndex;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Supplier;

/**
 * Updates an order in a specified supplier's list of orders
 */
public class UpdateOrderCommand extends Command {

    public static final String COMMAND_WORD = "updateOrder";
    public static final String COMMAND_LOWER = "updateorder";

    public static final String MESSAGE_EMPTY_LIST = "Empty contact list: No contacts available to update!";
    public static final String MESSAGE_UPDATE_SUCCESS = "Order has been updated successfully";
    public static final String MESSAGE_NO_CHANGE = "At least one of the optional fields must be provided";
    public static final String MESSAGE_NOT_FOUND = "No person found with phone number %1$s.";
    public static final String ERROR_EXTENSION = " Try running 'list' before using the command again.";
    public static final String MESSAGE_NOT_SUPPLIER = "The person with phone number %1$s is not a supplier.";
    public static final String MESSAGE_OUT_OF_BOUNDS = "Index given is out of bounds of supplier's list of orders.";
    public static final String MESSAGE_DUPLICATE_ORDER = "Order with identical values already exists.";
    public static final String MESSAGE_EMPTY_ORDER_LIST = "Supplier currently has no orders.";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates the details of the order identified "
        + "by the index number used in the supplier's order list. "
        + "Existing values will be overwritten by the input values.\n"
        + "Parameters: "
        + PREFIX_PHONE + "PHONE "
        + PREFIX_ORDERNUM + "ORDER_INDEX "
        + "[" + PREFIX_ITEM + "ITEM] "
        + "[" + PREFIX_QUANTITY + "QUANTITY] "
        + "[" + PREFIX_UNITPRICE + "UNITPRICE] "
        + "[" + PREFIX_DELIVERYDAY + "DELIVERYDAY]\n"
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_PHONE + "91234567 "
        + PREFIX_ORDERNUM + "1 "
        + PREFIX_ITEM + "Pens "
        + PREFIX_QUANTITY + "100 "
        + PREFIX_UNITPRICE + "0.90 "
        + PREFIX_DELIVERYDAY + "every Tuesday";

    public final Phone supplierPhone;
    public final OrderIndex orderIndex;
    public final UpdateOrderDescriptor descriptor;


    /**
     * Creates an UpdateOrderCommand object
     * @param supplierPhone The phone number of the specified supplier
     * @param orderIndex The index of the order specified
     * @param descriptor The object holding information regarding the changes
     */
    public UpdateOrderCommand(Phone supplierPhone, OrderIndex orderIndex, UpdateOrderDescriptor descriptor) {
        requireNonNull(descriptor);

        this.supplierPhone = supplierPhone;
        this.orderIndex = orderIndex;
        this.descriptor = descriptor;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person person = null;
        ObservableList<Person> fullList = model.getAddressBook().getPersonList();
        ObservableList<Person> currentList = model.getFilteredPersonList();

        if (fullList.isEmpty()) {
            throw new CommandException(MESSAGE_EMPTY_LIST);
        }

        for (int i = 0; i < currentList.size(); i++) {
            if (currentList.get(i).getPhone().equals(this.supplierPhone)) {
                person = currentList.get(i);
                break;
            }
        }

        if (person == null && fullList.size() != currentList.size()) {
            throw new CommandException(String.format(MESSAGE_NOT_FOUND + ERROR_EXTENSION, supplierPhone));
        }
        if (person == null) {
            throw new CommandException(String.format(MESSAGE_NOT_FOUND, supplierPhone));
        }
        if (!person.getCategory().toString().equals("[Supplier]")) {
            throw new CommandException(String.format(MESSAGE_NOT_SUPPLIER, supplierPhone));
        }

        Supplier supplier = (Supplier) person;
        int orderIndex = Integer.parseInt(this.orderIndex.toString());

        if (supplier.getSize() == 0) {
            throw new CommandException(MESSAGE_EMPTY_ORDER_LIST);
        }
        if (!(orderIndex > 0 && orderIndex <= supplier.getSize())) {
            throw new CommandException(MESSAGE_OUT_OF_BOUNDS);
        }

        if (!this.descriptor.isAnyFieldEdited()) {
            throw new CommandException(MESSAGE_NO_CHANGE);
        }

        Order newOrder = createEditedOrder(supplier.getOrder(orderIndex), this.descriptor);

        if (supplier.hasOrder(newOrder)) {
            throw new CommandException(MESSAGE_DUPLICATE_ORDER);
        }
        supplier.updateOrders(orderIndex, newOrder);

        model.setPerson(person, supplier);

        return new CommandResult(MESSAGE_UPDATE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UpdateOrderCommand)) {
            return false;
        }

        UpdateOrderCommand otherUpdateOrderCommand = (UpdateOrderCommand) other;
        return this.supplierPhone.equals(otherUpdateOrderCommand.supplierPhone)
                && this.orderIndex.equals(otherUpdateOrderCommand.orderIndex)
                && this.descriptor.equals(otherUpdateOrderCommand.descriptor);
    }

    /**
     * Creates a new order based on the existing and new parameters
     * @param toEdit The pre-existing order to be edited
     * @param descriptor The descriptor that contains information on the parameter to be updated
     * @return A new order with the newly updated parameters
     */
    public static Order createEditedOrder(Order toEdit, UpdateOrderDescriptor descriptor) {
        assert toEdit != null;

        ItemName updatedItem = descriptor.getItem().orElse(toEdit.getItem());
        ItemQuantity updatedQuantity = descriptor.getQuantity().orElse(toEdit.getQuantity());
        ItemUnitPrice updatedUnitPrice = descriptor.getUnitPrice().orElse(toEdit.getUnitPrice());
        ItemDeliveryDay updatedDeliveryDay = descriptor.getDeliveryDay().orElse(toEdit.getDeliveryDay());

        return new Order(updatedItem, updatedQuantity, updatedUnitPrice, updatedDeliveryDay);
    }

    /**
     * Represents the information to be edited in an existing order
     */
    public static class UpdateOrderDescriptor {
        private ItemName item;
        private ItemQuantity quantity;
        private ItemUnitPrice unitPrice;
        private ItemDeliveryDay deliveryDay;

        public UpdateOrderDescriptor() {}

        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(item, quantity, unitPrice, deliveryDay);
        }

        public Optional<ItemName> getItem() {
            return Optional.ofNullable(item);
        }

        public Optional<ItemQuantity> getQuantity() {
            return Optional.ofNullable(quantity);
        }

        public Optional<ItemUnitPrice> getUnitPrice() {
            return Optional.ofNullable(unitPrice);
        }

        public Optional<ItemDeliveryDay> getDeliveryDay() {
            return Optional.ofNullable(deliveryDay);
        }

        public void updateItem(ItemName newItem) {
            this.item = newItem;
        }

        public void updateQuantity(ItemQuantity newQuantity) {
            this.quantity = newQuantity;
        }

        public void updateUnitPrice(ItemUnitPrice newUnitPrice) {
            this.unitPrice = newUnitPrice;
        }

        public void updateDeliveryDay(ItemDeliveryDay newDeliveryDay) {
            this.deliveryDay = newDeliveryDay;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof UpdateOrderDescriptor)) {
                return false;
            }

            UpdateOrderDescriptor otherUpdateOrderDescriptor = (UpdateOrderDescriptor) other;

            boolean isItemSame = Objects.equals(this.item, otherUpdateOrderDescriptor.item);
            boolean isQuantitySame = Objects.equals(this.quantity, otherUpdateOrderDescriptor.quantity);
            boolean isUnitPriceSame = Objects.equals(this.unitPrice, otherUpdateOrderDescriptor.unitPrice);
            boolean isDeliveryDaySame = Objects.equals(this.deliveryDay, otherUpdateOrderDescriptor.deliveryDay);

            return isItemSame && isQuantitySame && isUnitPriceSame && isDeliveryDaySame;
        }
    }
}
