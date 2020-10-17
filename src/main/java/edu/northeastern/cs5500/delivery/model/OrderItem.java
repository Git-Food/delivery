package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

/** Class OrderItem represents an item in an Order. */
@Data
public class OrderItem implements Model {
    /** Constant valid amount for price and quantity of the order */
    private static final double VALID_PRICE = 0.0;
    /** Constant valid for valid amount of items in the item */
    private static final int VALID_AMOUNT = 0;

    private ObjectId id;
    private Double price;
    private Integer quantity;
    private String specialNote;

    /** @return true if this OrderItem is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.price >= VALID_PRICE && this.quantity >= VALID_AMOUNT;
    }
}
