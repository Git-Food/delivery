package edu.northeastern.cs5500.delivery.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import lombok.Data;

@Data
public class Order implements Model{
    /**Constant valid charge amount for the order */
    private static final double VALID_CHARGE = 0.0;
    private ObjectId id;
    private List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private Double taxAmount;
    private Double totalPrice;
    private ObjectId customerId;
    private ObjectId businessId;

    /**@return true if this Order is valid */
    @JsonIgnore
    public boolean isValid() {
        return !this.orderItems.isEmpty() &&
        this.taxAmount > VALID_CHARGE &&
        this.totalPrice > VALID_CHARGE;
    }
}
