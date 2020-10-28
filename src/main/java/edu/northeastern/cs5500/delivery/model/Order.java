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
    private Double deliveryCharge;
    private Double taxAmount;
    private Double totalPrice;
    private ObjectId customerId;
    private ObjectId businessId;

    /** @return true if this Order is valid */
    @JsonIgnore
    public boolean isValid() {
        return !this.orderItems.isEmpty() && this.customerId != null && this.businessId != null;
    }
}
