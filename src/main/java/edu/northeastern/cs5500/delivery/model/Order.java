package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Order implements Model {
    private ObjectId id;
    private Map<String, OrderItem> orderItems = new HashMap<>();
    private OrderStatus orderStatus;
    private long deliveryCharge;
    private long taxAmount;
    private long totalPrice;
    private int totalOrderItemQuantity;
    private String customerId;
    private ObjectId businessId;
    private ObjectId driverId;
    private LocalDate orderDate;

    /** @return true if this Order is valid */
    @JsonIgnore
    public boolean isValid() {
        return !orderItems.isEmpty() && customerId != null && businessId != null;
    }
}
