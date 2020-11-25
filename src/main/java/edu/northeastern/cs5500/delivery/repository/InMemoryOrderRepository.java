package edu.northeastern.cs5500.delivery.repository;

import edu.northeastern.cs5500.delivery.model.Order;
import java.util.Collection;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

public class InMemoryOrderRepository extends InMemoryRepository<Order> implements OrderRepository {

    /**
     * Returns a collection of a User's orders.
     *
     * @param userId User's id.
     * @return Collection of Orders based on given CustomerUser ObjectId.
     */
    @Override
    public Collection<Order> getOrdersByUserId(ObjectId id) {
        Collection<Order> orders = getAll();
        return orders.stream()
                .filter(x -> x.getCustomerId().equals(id))
                .collect(Collectors.toList());
    }
}
