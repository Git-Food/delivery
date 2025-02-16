package edu.northeastern.cs5500.delivery.model;

import java.time.LocalDate;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUser extends AbstractUser {
    private Map<ObjectId, Order> favoriteOrders;
    private LocalDate birthday;
}
