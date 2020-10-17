package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import lombok.Data;

/**Class OrderItem represents an item in an Order.
 */
@Data
public class OrderItem implements Model{
    private ObjectId id;
    private Double price;
    private Integer quantity;
    private String specialNote;

    /**
     * @return true if this OrderItem is valid */
    @JsonIgnore
    public boolean isValid(){
        return this.price >= 0 && this.quantity >= 0;
    }
}
