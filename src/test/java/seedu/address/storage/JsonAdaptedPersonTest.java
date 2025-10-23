package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.category.Category;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Order;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Supplier;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = "A";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_CATEGORY = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_CATEGORY = BENSON.getCategory().getCategoryName();

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_CATEGORY);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null,
                VALID_PHONE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_CATEGORY);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_CATEGORY);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME,
                null,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_CATEGORY);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_CATEGORY);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME,
                VALID_PHONE,
                null,
                VALID_ADDRESS,
                VALID_CATEGORY);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_CATEGORY);
        String expectedMessage = Address.INVALID_ADDRESS_LENGTH_MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddressCharacters_throwsIllegalValueException() {
        String invalidAddressWithCharacters = "123 Main Street @0505";
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME,
                        VALID_PHONE,
                        VALID_EMAIL,
                        invalidAddressWithCharacters,
                        VALID_CATEGORY);

        String expectedMessage = Address.INVALID_CHARACTER_MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME,
                VALID_PHONE,
                VALID_EMAIL,
                null,
                VALID_CATEGORY);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidCategories_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, INVALID_CATEGORY);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toJsonAdaptedPerson_supplierWithOrders_success() {
        // create a supplier with 1 order
        Supplier supplier = new Supplier(new Name("Fiona Kunz"),
                new Phone("94824271"),
                new Email("lydia@example.com"),
                new Address("little tokyo"),
                new Category("Supplier"));

        // add an order
        supplier.addOrder(new Order("Laptop", 10, 1000.0, "2025-10-23"));

        // convert to JsonAdaptedPerson
        JsonAdaptedPerson adaptedPerson = new JsonAdaptedPerson(supplier);

        // check that JsonAdaptedOrder list is created correctly
        assertEquals(1, adaptedPerson.getOrders().size());
        JsonAdaptedOrder jsonOrder = adaptedPerson.getOrders().get(0);
        assertEquals("Laptop", jsonOrder.getItem());
        assertEquals(10, jsonOrder.getQuantity());
        assertEquals(1000.0, jsonOrder.getUnitPrice());
        assertEquals("2025-10-23", jsonOrder.getDeliveryDay());
    }

    @Test
    public void toModelType_supplierWithOrders_success() throws Exception {
        Supplier supplier = new Supplier(new Name("George Best"),
                new Phone("94824421"),
                new Email("anna@example.com"),
                new Address("4th street"),
                new Category("Supplier"));

        supplier.addOrder(new Order("Mouse", 5, 50.0, "2025-10-24"));

        JsonAdaptedPerson adaptedPerson = new JsonAdaptedPerson(supplier);
        Person modelPerson = adaptedPerson.toModelType();

        assertTrue(modelPerson instanceof Supplier);
        Supplier modelSupplier = (Supplier) modelPerson;
        assertEquals(1, modelSupplier.getOrders().size());
        assertEquals("Mouse", modelSupplier.getOrders().get(0).getItem());
        assertEquals(5, modelSupplier.getOrders().get(0).getQuantity());
        assertEquals(50.0, modelSupplier.getOrders().get(0).getUnitPrice());
        assertEquals("2025-10-24", modelSupplier.getOrders().get(0).getDeliveryDay());
    }
}
