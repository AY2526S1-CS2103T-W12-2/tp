package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.category.Category;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Order;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Shift;
import seedu.address.model.person.Staff;
import seedu.address.model.person.Supplier;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    @JsonProperty("name") private final String name;
    @JsonProperty("phone") private final String phone;
    @JsonProperty("email") private final String email;
    @JsonProperty("address") private final String address;
    @JsonProperty("category") private final String category;

    @JsonProperty("orders") private List<JsonAdaptedOrder> orders; // only for Supplier class
    @JsonProperty("shift") private String shift;
    @JsonProperty("numberOfLeaves") private Integer numberOfLeaves;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    public JsonAdaptedPerson(String name, String phone,
             String email, String address, JsonAdaptedCategory category) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.category = (category == null) ? null : category.getCategoryName();
        this.shift = null;
        this.numberOfLeaves = null;
    }

    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty(value = "name") String name,
                             @JsonProperty(value = "phone") String phone,
                             @JsonProperty(value = "email") String email,
                             @JsonProperty(value = "address") String address,
                             @JsonProperty(value = "category") String category
                             ) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.category = category;
        this.orders = null;
        this.shift = null;
        this.numberOfLeaves = null;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        Category sourceCategory = source.getCategory();
        category = sourceCategory.getCategoryName();

        if ("Supplier".equalsIgnoreCase(category) && source instanceof Supplier) {
            this.orders = new ArrayList<>();
            Supplier supplier = (Supplier) source;
            List<Order> orders = supplier.getOrders();
            for (Order order : orders) {
                String deliveryDay = order.getDeliveryDay();
                JsonAdaptedOrder jsonOrder = new JsonAdaptedOrder(order.getItem(),
                        order.getQuantity(),
                        order.getUnitPrice(),
                        deliveryDay);
                this.orders.add(jsonOrder);
            }
        } else if (source instanceof Staff) {
            Staff st = (Staff) source;
            this.shift = st.getShift() == null ? null : st.getShift().getValue();
            this.numberOfLeaves = st.getNumberOfLeaves();
        } else {
            this.orders = null;
            this.shift = null;
            this.numberOfLeaves = null;
        }
    }

    public List<JsonAdaptedOrder> getOrders() {
        return orders;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        }
        if (!Address.isValidAddressCharacters(address)) {
            throw new IllegalValueException(Address.INVALID_CHARACTER_MESSAGE_CONSTRAINTS);
        }
        if (!Address.isValidAddressLength(address)) {
            throw new IllegalValueException(Address.INVALID_ADDRESS_LENGTH_MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (category == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Category.class.getSimpleName()));
        }
        if (!Category.isValidCategoryName(category)) {
            throw new IllegalValueException(Category.MESSAGE_CONSTRAINTS);
        }

        final Category modelCategory = new Category(category);

        if ("Supplier".equalsIgnoreCase(category)) {
            return toSupplierModelType(modelCategory, modelName, modelPhone, modelEmail, modelAddress);
        }

        if ("Staff".equalsIgnoreCase(category)) {
            final String safeShift = (shift == null || shift.isBlank()) ? "AM" : shift;
            final Shift modelShift = new Shift(safeShift);

            final int leaves = (numberOfLeaves == null) ? 14 : numberOfLeaves;
            if (leaves < 0) {
                throw new IllegalValueException("numberOfLeaves cannot be negative");
            }


            Staff staff = new Staff(modelName, modelPhone, modelEmail, modelAddress, modelCategory, modelShift);
            int base = staff.getNumberOfLeaves(); // default is 14
            if (leaves > base) {
                staff.addLeaves(leaves - base);
            } else if (leaves < base) {
                staff.removeLeaves(base - leaves);
            }
            return staff;
        }

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelCategory);
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Supplier} object.
     */
    private Supplier toSupplierModelType(Category modelCategory,
                                       Name modelName,
                                       Phone modelPhone,
                                       Email modelEmail,
                                       Address modelAddress) {
        Supplier supplier = new Supplier(modelName, modelPhone, modelEmail, modelAddress, modelCategory);

        if (this.orders != null) {
            for (JsonAdaptedOrder jsonOrder : this.orders) {
                supplier.addOrder(jsonOrder.toModelType());
            }
        }

        return supplier;
    }
}
