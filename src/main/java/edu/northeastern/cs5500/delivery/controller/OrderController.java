package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.OrderItem;
import edu.northeastern.cs5500.delivery.model.OrderStatus;
import edu.northeastern.cs5500.delivery.model.ShoppingCart;
import edu.northeastern.cs5500.delivery.repository.OrderRepository;
import java.util.ArrayList;
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
    private final OrderRepository orders;
    private Map<ObjectId, ArrayList<ObjectId>> userToOrders;

    @Inject
    OrderController(OrderRepository orderRepository) {
        orders = orderRepository;
        userToOrders = createUserToOrdersMap();

        log.info("OrderController > construct");

        if (orders.count() > 0) {
            return;
        }

        log.info("OrderController > construct > adding default orders");
        // Menu Items
        ObjectId menuItemObjectId1 = new ObjectId();
        ObjectId menuItemObjectId2 = new ObjectId();
        ObjectId menuItemObjectId3 = new ObjectId();
        ObjectId businessId1 = new ObjectId();
        ObjectId businessId2 = new ObjectId();
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setObjectId(menuItemObjectId1);
        menuItem1.setName("Chicken1");
        menuItem1.setDescription("chicken1 description");
        menuItem1.setPrice(10);
        menuItem1.setNote("Spicy sauce included");
        menuItem1.setBusinessId(businessId1);
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setObjectId(menuItemObjectId2);
        menuItem2.setName("Beef1");
        menuItem2.setDescription("beef1 description");
        menuItem2.setPrice(3);
        menuItem2.setNote("BBQ sauce included");
        menuItem2.setBusinessId(businessId2);
        MenuItem menuItem3 = new MenuItem();
        menuItem3.setObjectId(menuItemObjectId3);
        menuItem3.setName("Pizza1");
        menuItem3.setDescription("Pizza1 description");
        menuItem3.setPrice(5);
        menuItem3.setNote("Double cheese included");
        menuItem3.setBusinessId(businessId2);
        // Order Items
        ObjectId orderItemId1 = new ObjectId();
        ObjectId orderItemId2 = new ObjectId();
        ObjectId orderItemId3 = new ObjectId();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(orderItemId1);
        orderItem1.setMenuItem(menuItem1);
        orderItem1.setBusinessId(businessId1);
        orderItem1.setQuantity(2);
        orderItem1.setSpecialNote("Special Note OrderItem1");
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(orderItemId2);
        orderItem2.setMenuItem(menuItem2);
        orderItem2.setBusinessId(businessId2);
        orderItem2.setQuantity(3);
        orderItem2.setSpecialNote("Special Note OrderItem2");
        OrderItem orderItem3 = new OrderItem();
        orderItem3.setId(orderItemId3);
        orderItem3.setMenuItem(menuItem3);
        orderItem3.setBusinessId(businessId2);
        orderItem3.setQuantity(4);
        orderItem3.setSpecialNote("Special Note OrderItem3");
        HashMap<String, OrderItem> order1Items = new HashMap<>();
        HashMap<String, OrderItem> order2Items = new HashMap<>();
        order1Items.put(orderItemId1.toString(), orderItem1);
        order2Items.put(orderItemId2.toString(), orderItem2);
        order2Items.put(orderItemId3.toString(), orderItem3);
        // Orders
        Order defaultOrder1 = new Order();
        defaultOrder1.setId(new ObjectId());
        defaultOrder1.setOrderItems(order1Items);
        defaultOrder1.setCustomerId(new ObjectId());
        defaultOrder1.setBusinessId(orderItem1.getBusinessId());
        defaultOrder1.setOrderStatus(OrderStatus.UNDER_REVIEW);
        defaultOrder1.setTotalOrderItemQuantity(2);
        defaultOrder1.setTotalPrice(20);
        Order defaultOrder2 = new Order();
        defaultOrder2.setId(new ObjectId());
        defaultOrder2.setOrderItems(order2Items);
        defaultOrder2.setCustomerId(new ObjectId());
        defaultOrder2.setBusinessId(orderItem2.getBusinessId());
        defaultOrder2.setOrderStatus(OrderStatus.UNDER_REVIEW);
        defaultOrder2.setTotalOrderItemQuantity(7);
        defaultOrder2.setTotalPrice(29);
        try {
            addOrder(defaultOrder1);
            addOrder(defaultOrder2);
        } catch (Exception e) {
            log.error("OrderController > construct > adding default orders > failure?");
            e.printStackTrace();
        }
    }

    /** Return a Map<CustomerUserId, ArrayList<OrderId>>> based on a the Orders Repository. */
    private Map<ObjectId, ArrayList<ObjectId>> createUserToOrdersMap() {
        log.debug("OrderController > createUserToOrdersMap()");
        HashMap<ObjectId, ArrayList<ObjectId>> userToOrdersMap = new HashMap<>();
        Collection<Order> allOrders = this.orders.getAll();
        for (Order order : allOrders) {
            ArrayList<ObjectId> currentUserOrders =
                    userToOrdersMap.getOrDefault(order.getCustomerId(), new ArrayList<ObjectId>());
            currentUserOrders.add(order.getId());
            userToOrdersMap.put(order.getCustomerId(), currentUserOrders);
        }

        return userToOrdersMap;
    }

    /**
     * Returns a collection of a User's orders.
     *
     * @param userId ObjectId of a given CustomerUser
     * @return Collection of Orders based on given CustomerUser ObjectId.
     */
    public Collection<Order> getOrdersByUser(@Nonnull ObjectId userId) {
        log.debug("OrderController > getOrderHistory()");
        return orders.getOrdersByUserId(userId);
    }

    /**
     * Creates an Order based on the provided ShoppingCart.
     *
     * @param shoppingCart ShoppingCart from which an Order is created
     * @return Order based on the provided ShoppingCart contents.
     */
    private Order createOrder(ShoppingCart shoppingCart) {
        Map<String, OrderItem> orderItems = shoppingCart.getOrderItems();
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
     * @return new Order created based on ShoppingCart object OrderItem contents.
     * @throws Exception TODO (shh) create a custom exception
     */
    public Order submitOrder(ShoppingCart shoppingCart) throws Exception {
        log.debug("OrderController > submitOrder(...)");
        Order newOrder = createOrder(shoppingCart);
        return addOrder(newOrder);
    }
}
