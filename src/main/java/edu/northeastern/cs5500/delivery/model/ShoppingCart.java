package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ShoppingCart implements Model {
    private ObjectId id;
    private String customerId;
    private Map<String, OrderItem> orderItems;
    private int totalQuantity;
    private long totalPrice;

    @JsonIgnore
    public boolean isEmpty() {
        return orderItems.isEmpty();
    }
}
