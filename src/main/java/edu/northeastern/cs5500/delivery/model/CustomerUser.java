package edu.northeastern.cs5500.delivery.model;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class CustomerUser extends AbstractUser {
    private List<Order> favoriteOrders;
    private Date birthday;
}
