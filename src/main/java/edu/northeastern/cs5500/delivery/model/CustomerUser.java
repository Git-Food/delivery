package edu.northeastern.cs5500.delivery.model;

import java.util.Date;
import java.util.Map;

import org.bson.types.ObjectId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUser extends AbstractUser {
    private Map<ObjectId, Order> favoriteOrders;
    private Date birthday;
}
