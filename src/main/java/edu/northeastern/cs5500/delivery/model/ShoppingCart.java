package edu.northeastern.cs5500.delivery.model;

import java.util.Map;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ShoppingCart implements Model {
    private ObjectId id;
    private ObjectId customerId;
    private Map<String, Order> shoppingCart;
    private int totalQuantity;
    private long totalPrice;

    public boolean isEmpty() {
        return shoppingCart.isEmpty();
    }
}
