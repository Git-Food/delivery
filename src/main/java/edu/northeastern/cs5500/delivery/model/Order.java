package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class Order implements Model {
    private ObjectId id;
    private Map<ObjectId, OrderItem> orderItems;
    private OrderStatus orderStatus;
    private long deliveryCharge;
    private long taxAmount;
    private long totalPrice;
    private ObjectId customerId;
    private ObjectId businessId;
    private ObjectId driverId;

    /** @return true if this Order is valid */
    @JsonIgnore
    public boolean isValid() {
        return !orderItems.isEmpty() && customerId != null && businessId != null;
    }
}
