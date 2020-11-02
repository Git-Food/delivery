package edu.northeastern.cs5500.delivery.model;

import java.util.Map;

import org.bson.types.ObjectId;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShoppingCart implements Model {
    private ObjectId id;
    private Map<ObjectId, Order> shoppingCart;
    private Integer itemCount;
    private Long price;
}
