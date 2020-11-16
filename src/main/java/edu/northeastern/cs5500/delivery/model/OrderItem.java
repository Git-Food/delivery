package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

/** Class OrderItem represents an item in an Order. */
@Data
public class OrderItem implements Model {
    /** Constant valid for valid amount of items in the item */
    private static final int VALID_QUANTITY = 0;

    private ObjectId id;
    private ObjectId businessId; // businessId from the BusinessUser model
    private MenuItem menuItem;
    private int quantity;
    private String specialNote;

    /** @return true if this OrderItem is valid */
    @JsonIgnore
    public boolean isValid() {
        return menuItem != null && quantity >= VALID_QUANTITY;
    }
}
