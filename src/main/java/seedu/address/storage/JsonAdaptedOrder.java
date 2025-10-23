package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.model.person.Order;

/**
 * Jackson-friendly version of {@link Order}.
 */
public class JsonAdaptedOrder {

    private final String item;
    private final int quantity;
    private final double unitPrice;
    private final String deliveryDay; // store as a string for JSON

    /**
     * Constructs a {@code JsonAdaptedOrder} with the given order details.
     */
    @JsonCreator
    public JsonAdaptedOrder(@JsonProperty("item") String item,
                            @JsonProperty("quantity") int quantity,
                            @JsonProperty("unitPrice") double unitPrice,
                            @JsonProperty("deliveryDay") String deliveryDay) {
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.deliveryDay = deliveryDay;
    }

    /**
     * Converts a given {@code Order} into this class for Jackson use
     */
    public JsonAdaptedOrder(Order source) {
        this.item = source.getItem();
        this.quantity = source.getQuantity();
        this.unitPrice = source.getUnitPrice();
        this.deliveryDay = source.getDeliveryDay();
    }

    @JsonValue
    public String getItem() {
        return item;
    }

    @JsonValue
    public int getQuantity() {
        return quantity;
    }

    @JsonValue
    public double getUnitPrice() {
        return unitPrice;
    }

    @JsonValue
    public String getDeliveryDay() {
        return deliveryDay;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Order} object.
     */
    public Order toModelType() {
        return new Order(item, quantity, unitPrice, deliveryDay);
    }
}
