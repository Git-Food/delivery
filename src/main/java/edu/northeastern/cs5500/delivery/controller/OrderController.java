package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.OrderItem;
import edu.northeastern.cs5500.delivery.model.OrderStatus;
import edu.northeastern.cs5500.delivery.model.ShoppingCart;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setObjectId(menuItemObjectId1);
        menuItem1.setName("Chicken1");
        menuItem1.setDescription("chicken1 description");
        menuItem1.setPrice(2);
        menuItem1.setNote("Spicy sauce included");
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setObjectId(menuItemObjectId2);
        menuItem2.setName("Beef1");
        menuItem2.setDescription("beef1 description");
        menuItem2.setPrice(3);
        menuItem2.setNote("BBQ sauce included");
        // Order Items
        ObjectId orderItemId1 = new ObjectId();
        ObjectId orderItemId2 = new ObjectId();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(orderItemId1);
        orderItem1.setMenuItem(menuItem1);
        orderItem1.setQuantity(2);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(orderItemId2);
        orderItem2.setMenuItem(menuItem2);
        orderItem2.setQuantity(3);
        HashMap<String, OrderItem> order1Items = new HashMap<>();
        HashMap<String, OrderItem> order2Items = new HashMap<>();
        order1Items.put(orderItemId1.toString(), orderItem1);
        order1Items.put(orderItemId2.toString(), orderItem2);
        order2Items.put(orderItemId1.toString(), orderItem1);
        // Orders
        Order defaultOrder1 = new Order();
        defaultOrder1.setId(new ObjectId());
        defaultOrder1.setOrderItems(order1Items);
        defaultOrder1.setCustomerId(new ObjectId());
        defaultOrder1.setBusinessId(new ObjectId());
        defaultOrder1.setOrderStatus(OrderStatus.UNDER_REVIEW);
        Order defaultOrder2 = new Order();
        defaultOrder2.setId(new ObjectId());
        defaultOrder2.setOrderItems(order2Items);
        defaultOrder2.setCustomerId(new ObjectId());
        defaultOrder2.setBusinessId(new ObjectId());
        defaultOrder2.setOrderStatus(OrderStatus.UNDER_REVIEW);
        try {
            addOrder(defaultOrder1);
            addOrder(defaultOrder2);
            // Updates the price and quantity for each order
            for (Order order : orders.getAll()) {
                calculateOrderPrice(order);
                calculateItemQuantity(order);
            }
        } catch (Exception e) {
            log.error("OrderController > construct > adding default orders > failure?");
            e.printStackTrace();
        }
    }

    /**
     * Creates an Order based on the provided ShoppingCart.
     *
     * @param shoppingCart ShoppingCart from which an Order is created
     * @return Order based on the provided ShoppingCart contents.
     */
    private Order createOrder(ShoppingCart shoppingCart) {
        Map<String, OrderItem> orderItems = shoppingCart.getShoppingCart();
        Map.Entry<String, OrderItem> entry = orderItems.entrySet().iterator().next();
        Order newOrder = new Order();
        newOrder.setId(new ObjectId());
        newOrder.setCustomerId(shoppingCart.getCustomerId());
        newOrder.setBusinessId(entry.getValue().getBusinessId());
        newOrder.setOrderItems(orderItems);
        newOrder.setOrderStatus(OrderStatus.UNDER_REVIEW);
        newOrder.setTotalOrderItemQuantity(shoppingCart.getTotalQuantity());
        newOrder.setTotalPrice(shoppingCart.getTotalPrice());
        return newOrder;
    }

    // TODO: remove? As this is now calculated by ShoppingCart?
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
        log.debug("OrderController > calculateOrderPrice(...)");
        long orderPrice = 0;
        for (String id : order.getOrderItems().keySet()) {
            OrderItem currrentItem = order.getOrderItems().get(id);
            orderPrice =
                    orderPrice
                            + (currrentItem.getMenuItem().getPrice() * currrentItem.getQuantity());
        }
        return orderPrice;
    }

    // TODO: remove? As this is now calculated by ShoppingCart?
    // TODO: is this responsibility of the OrderController?
    /**
     * Totals the number of OrderItems in an order
     *
     * @param order Order to total the quantity from
     * @return itemQuantity order item quantity for the order
     */
    public int calculateItemQuantity(Order order) {
        log.debug("OrderController > calculateItemQuanity(...)");
        int totalOrderItemQuantity = 0;
        for (OrderItem item : order.getOrderItems().values()) {
            totalOrderItemQuantity += item.getQuantity();
        }
        return totalOrderItemQuantity;
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

    /**
     * Creates an Order based on the provided ShoppingCart contents and adds the new Order to the
     * Order repository.
     *
     * @param shoppingCart Non empty ShoppingCart whose contents are used to create a new Order
     * @throws Exception TODO (shh) create a custom exception
     */
    // TODO: Return newOrder?
    public void submitOrder(ShoppingCart shoppingCart) throws Exception {
        log.debug("OrderController > submitOrder(...)");
        Order newOrder = createOrder(shoppingCart);
        addOrder(newOrder);
    }
}
