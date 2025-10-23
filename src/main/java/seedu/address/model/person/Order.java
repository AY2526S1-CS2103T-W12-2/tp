package seedu.address.model.person;

/**
 * Represents an order made to a supplier
 */
public class Order {

    private final String item;
    private final int quantity;
    private final double unitPrice;
    private final String deliveryDay;

    /**
     * Constructor for order
     * @param item name of item to be ordered
     * @param quantity quantity of item
     * @param unitPrice price of each item
     * @param deliveryDay day to be delivered
     */
    public Order(String item, int quantity, double unitPrice, String deliveryDay) {
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.deliveryDay = deliveryDay;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getDeliveryDay() {
        return deliveryDay;
    }

    @Override
    public String toString() {
        return String.format("%s of %s (at $%s each) to be delivered %s",
                getQuantity(), getItem(), getUnitPrice(), getDeliveryDay());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Order)) {
            return false;
        }

        Order otherOrder = (Order) other;

        if (!(otherOrder.getItem().equals(this.item))) {
            return false;
        }

        if (!(otherOrder.getQuantity() == this.quantity)) {
            return false;
        }

        if (!(otherOrder.getUnitPrice() == this.unitPrice)) {
            return false;
        }

        if (!(otherOrder.getDeliveryDay().equals(this.deliveryDay))) {
            return false;
        }

        return true;

    }
}
