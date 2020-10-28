package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

/** Class OrderItem represents an item in an Order. */
@Data
@Builder
public class OrderItem implements Model {
    /** Constant valid for valid amount of items in the item */
    private static final int VALID_AMOUNT = 0;

    private ObjectId id;
    private MenuItem menuItem;
    private Integer quantity;
    private String specialNote;

    /** @return true if this OrderItem is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.menuItem != null && this.quantity >= VALID_AMOUNT;
    }
}
