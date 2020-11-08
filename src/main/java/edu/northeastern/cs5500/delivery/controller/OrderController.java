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
     * Creates an Order based on userId and OrderItem.
     *
     * @param userId ObjectId of user making the Order.
     * @return Order based on userId and OrderItem and OrderItem businessId.
     */
    private Order createOrder(ObjectId userId, OrderItem orderItem) {
        Map<String, OrderItem> orderItems = new HashMap<>();
        orderItems.put(orderItem.getId().toString(), orderItem);
        Order newOrder = new Order();
        newOrder.setId(new ObjectId());
        newOrder.setCustomerId(userId);
        newOrder.setBusinessId(orderItem.getBusinessId());
        newOrder.setOrderItems(orderItems);
        return newOrder;
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
        for (String id : order.getOrderItems().keySet()) {
            OrderItem currrentItem = order.getOrderItems().get(id);
            orderPrice =
                    orderPrice
                            + (currrentItem.getMenuItem().getPrice() * currrentItem.getQuantity());
        }
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

    /**
     * Sets mofified fields of the Order and updates the order in the repository
     *
     * @param order Order object being updated
     * @param totalPrice new totalPrice of the Order
     * @param totalOrderItemQuantity new totalOrderItemQuantity of the Order
     * @param orderItems new orderItems Map of the Order
     * @throws Exception TODO (shh) create sepcific exception
     */
    public void updateOrder(
            @Nonnull Order order,
            @Nonnull long totalPrice,
            @Nonnull int totalOrderItemQuantity,
            Map<String, OrderItem> orderItems)
            throws Exception {
        log.debug("OrderController > updateOrder(Order, long, int, Map)");
        order.setTotalPrice(totalPrice);
        order.setTotalOrderItemQuantity(totalOrderItemQuantity);
        order.setOrderItems(orderItems);
        orders.update(order);
    }

    public void deleteOrder(@Nonnull ObjectId id) throws Exception {
        log.debug("OrderController > deleteOrder(...)");
        orders.delete(id);
    }

    /**
     * Return modified Order object after adding an OrderItem to an existing Order, or creates a
     * brand new Order based on OrderItem.
     *
     * @param userId userId from the CustomerUser model
     * @param orderItemToAdd OrderItem object being added to an existing or new Order
     * @param shoppingCartController ShoppingCartController object
     * @throws Exception TODO (shh) create specific exception
     * @return Order object containing newly added OrderItem
     */
    public Order addOrderItem(
            ObjectId userId,
            OrderItem orderItemToAdd,
            ShoppingCartController shoppingCartController)
            throws Exception {
        log.debug("OrderController > addOrderItem(...)");
        ShoppingCart activeShoppingCart = shoppingCartController.getShoppingCartByUser(userId);

        if (!activeShoppingCart.isEmpty()) {
            // add orderItem to existing Order where Cart is NOT emtpy
            Order updatedCurrentOrder =
                    addOrderItemToExistingOrder(userId, orderItemToAdd, activeShoppingCart);
            if (updatedCurrentOrder != null) {
                shoppingCartController.addOrderToShoppingCart(
                        updatedCurrentOrder, activeShoppingCart);
                return updatedCurrentOrder;
            }
        }
        // Create a new Order when Cart is Empty OR there is no matching existing Order
        Order newOrder = addOrderItemToNewOrder(userId, orderItemToAdd, activeShoppingCart);
        shoppingCartController.addOrderToShoppingCart(newOrder, activeShoppingCart);
        return newOrder;
    }

    /**
     * Returns modified Order object after adding an OrderItem to an existing order
     *
     * @param userId userId of the CustomerUser model
     * @param orderItemToAdd OrderItem object being added to an existing or new Order
     * @param activeShoppingCart ShoppingCart object of the given CustomerUser
     * @return Order object containing newly added OrderItem, or null if no matching Order was
     *     found.
     * @throws Exception TODO (shh) create specicific exception
     */
    private Order addOrderItemToExistingOrder(
            ObjectId userId, OrderItem orderItemToAdd, ShoppingCart activeShoppingCart)
            throws Exception {
        // Iterates through all Orders in the given ShoppingCart to find a matching Order.
        for (Map.Entry<String, Order> entry : activeShoppingCart.getShoppingCart().entrySet()) {
            if (entry.getValue().getBusinessId().equals(orderItemToAdd.getBusinessId())) {
                Order currentOrder = entry.getValue();
                Map<String, OrderItem> currentOrderItems = currentOrder.getOrderItems();
                currentOrderItems.put(orderItemToAdd.getId().toString(), orderItemToAdd);
                long newOrderPrice = calculateOrderPrice(currentOrder);
                int newOrderItemQuantity = calculateItemQuantity(currentOrder);
                try {
                    updateOrder(
                            currentOrder, newOrderPrice, newOrderItemQuantity, currentOrderItems);
                } catch (Exception e) {
                    log.error(
                            "OrderController > addOrderItemToExistingOrder > adding new order > failure?");
                    e.printStackTrace();
                }
                entry.setValue(currentOrder);
                return currentOrder;
            }
        }
        return null;
    }

    /**
     * Returns Order object when adding an OrderItem to a newly created Order.
     *
     * @param userId userId of the CustomerUser model
     * @param orderItemToAdd OrderItem object being added to an existing or new Order
     * @param activeShoppingCart ShoppingCart object of the given CustomerUser
     * @return Order object containing newly added OrderItem
     */
    private Order addOrderItemToNewOrder(
            ObjectId userId, OrderItem orderItemToAdd, ShoppingCart activeShoppingCart) {
        Order newOrder = createOrder(userId, orderItemToAdd);
        try {
            addOrder(newOrder);
        } catch (Exception e) {
            log.error("OrderController > addOrderItemToNewOrder > adding new order > failure?");
            e.printStackTrace();
        }
        return newOrder;
    }
}
