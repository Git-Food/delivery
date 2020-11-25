package edu.northeastern.cs5500.delivery.repository;

import edu.northeastern.cs5500.delivery.model.Order;
import java.util.Collection;
import javax.annotation.Nonnull;
import org.bson.types.ObjectId;

public interface OrderRepository extends GenericRepository<Order> {

    /**
     * Returns a collection of a User's orders.
     *
     * @param userId User's id.
     * @return Collection of Orders based on given CustomerUser ObjectId.
     */
    public Collection<Order> getOrdersByUserId(@Nonnull ObjectId userId);
}
