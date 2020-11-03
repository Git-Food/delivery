package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.OrderItem;
import edu.northeastern.cs5500.delivery.model.OrderStatus;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class OrderController {
    private final GenericRepository<Order> orders;

    @Inject
    OrderController(GenericRepository<Order> orderRepository) {
        orders = orderRepository;

        log.info("OrderController > construct");

        if (orders.count() > 0) {
            return;
        }

        log.info("OrderController > construct > adding default orders");
        // Menu Items
        ObjectId menuItemObjectId1 = new ObjectId();
        ObjectId menuItemObjectId2 = new ObjectId();
        MenuItem menuItem1 =
                MenuItem.builder()
                        .objectId(menuItemObjectId1)
                        .name("Chicken1")
                        .description("chicken1 description")
                        .note("Spicy sauce included")
                        .build();
        MenuItem menuItem2 =
                MenuItem.builder()
                        .objectId(menuItemObjectId2)
                        .name("Chicken2")
                        .description("chicken2 description")
                        .note("BBQ sauce included")
                        .build();
        // Order Items
        OrderItem orderItem1 =
                OrderItem.builder().id(new ObjectId()).menuItem(menuItem1).quantity(2).build();
        OrderItem orderItem2 =
                OrderItem.builder().id(new ObjectId()).menuItem(menuItem2).quantity(1).build();
        HashMap<ObjectId, OrderItem> order1Items = new HashMap<>();
        HashMap<ObjectId, OrderItem> order2Items = new HashMap<>();
        order1Items.put(menuItemObjectId1, orderItem1);
        order1Items.put(menuItemObjectId2, orderItem2);
        order2Items.put(menuItemObjectId1, orderItem1);
        // Orders
        Order defaultOrder1 =
                Order.builder()
                        .orderItems(order1Items)
                        .customerId(new ObjectId())
                        .businessId(new ObjectId())
                        .orderStatus(OrderStatus.UNDER_REVIEW)
                        .build();
        Order defaultOrder2 =
                Order.builder()
                        .orderItems(order2Items)
                        .customerId(new ObjectId())
                        .businessId(new ObjectId())
                        .orderStatus(OrderStatus.UNDER_REVIEW)
                        .build();
        try {
            addOrder(defaultOrder1);
            addOrder(defaultOrder2);
        } catch (Exception e) {
            log.error("OrderController > construct > adding default orders > failure?");
            e.printStackTrace();
        }
    }

    // TODO take the order id instead of object order.
    // TODO the method will update order's field "totalPrice" or that would be
    // responsibility of shopping cart?
    /**
     * Totals price for an order.
     *
     * @param order Order to calculate total price from
     * @return orderPrice price for the order
     */
    public long calculateOrderPrice(Order order) {
        long orderPrice = 0;
        for (ObjectId id : order.getOrderItems().keySet()) {
            OrderItem currrentItem = order.getOrderItems().get(id);
            orderPrice =
                    orderPrice
                            + (currrentItem.getMenuItem().getPrice() * currrentItem.getQuantity());
        }
        // Update order
        order.setTotalPrice(orderPrice);
        try {
            updateOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return the total price of this order
        return orderPrice;
    }

    // TODO: is this responsibility of the OrderController?
    /**
     * Totals the number of OrderItems in an order
     *
     * @param order Order to total the quantity from
     * @return itemQuantity order item quantity for the order
     */
    public int calculateItemQuantity(Order order) {
        int itemQuantity = 0;
        for (OrderItem item : order.getOrderItems().values()) {
            itemQuantity += item.getQuantity();
        }
        return itemQuantity;
    }

    @Nullable
    public Order getOrder(@Nonnull ObjectId uuid) {
        log.debug("OrderController > getOrder({})", uuid);
        return orders.get(uuid);
    }

    @Nonnull
    public Collection<Order> getOrders() {
        log.debug("OrderController > getOrders()");
        return orders.getAll();
    }

    @Nonnull
    public Order addOrder(@Nonnull Order order) throws Exception {
        log.debug("OrderController > addOrder(...)");
        if (!order.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidOrderException");
        }

        ObjectId id = order.getId();

        if (id != null && orders.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return orders.add(order);
    }

    public void updateOrder(@Nonnull Order order) throws Exception {
        log.debug("OrderController > updateOrder(...)");
        orders.update(order);
    }

    public void deleteOrder(@Nonnull ObjectId id) throws Exception {
        log.debug("OrderController > deleteOrder(...)");
        orders.delete(id);
    }
}
