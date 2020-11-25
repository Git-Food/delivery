package edu.northeastern.cs5500.delivery.repository;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCursor;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
import java.util.ArrayList;
import java.util.Collection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoDBOrderRepository extends MongoDBRepository<Order> implements OrderRepository {

    public MongoDBOrderRepository(MongoDBService mongoDBService) {
        super(Order.class, mongoDBService);
        // create index for customerId field
        collection.createIndex(new Document("curstomerId", 1));
    }

    /**
     * Returns a collection of a User's orders.
     *
     * @param userId User's id.
     * @return Collection of Orders based on given CustomerUser ObjectId.
     */
    @Override
    public Collection<Order> getOrdersByUserId(ObjectId userId) {
        Collection<Order> userOrderHistory = new ArrayList<>();
        MongoCursor<Order> orders = collection.find(eq("customerId", userId)).iterator();
        try {
            while (orders.hasNext()) {
                userOrderHistory.add(orders.next());
            }
        } finally {
            orders.close();
        }
        return userOrderHistory;
    }
}
