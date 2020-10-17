package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Order implements Model {
    /** Constant valid charge amount for the order */
    private static final double VALID_CHARGE = 0.0;
    /** Constant to keep the delivery fee as % (percentage) */
    private static final double DELIVERY_FEE = 0.03;

    private ObjectId id;
    private List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private Double deliveryCharge;
    private Double taxAmount;
    private Double totalPrice;
    private ObjectId customerId;
    private ObjectId businessId;

    /** @return true if this Order is valid */
    @JsonIgnore
    public boolean isValid() {
        return !this.orderItems.isEmpty()
                && this.deliveryCharge >= DELIVERY_FEE
                && this.taxAmount >= VALID_CHARGE
                && this.totalPrice >= VALID_CHARGE;
    }
}
