package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import lombok.Data;

/**Class OrderItem represents an item in an Order.
 */
@Data
public class OrderItem implements Model{
    /**Constant valid amount for price and quantity of the order */
    private static final int VALID_AMOUNT = 0;
    private ObjectId id;
    private Double price;
    private Integer quantity;
    private String specialNote;

    /**
     * @return true if this OrderItem is valid */
    @JsonIgnore
    public boolean isValid(){
        return this.price >= VALID_AMOUNT && this.quantity >= VALID_AMOUNT;
    }
}
