package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.OrderItem;
import edu.northeastern.cs5500.delivery.model.ShoppingCart;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingCartControllerTest {
    ShoppingCartController shoppingCarts;
    OrderController orders;

    @BeforeEach
    void init() {
        orders = new OrderController(new InMemoryRepository<Order>());
        shoppingCarts = new ShoppingCartController(new InMemoryRepository<ShoppingCart>(), orders);
    }

    ShoppingCart createShoppingCart() {
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
        ObjectId businessId = new ObjectId();
        ObjectId orderItemId1 = new ObjectId();
        ObjectId orderItemId2 = new ObjectId();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(orderItemId1);
        orderItem1.setMenuItem(menuItem1);
        orderItem1.setQuantity(2);
        orderItem1.setBusinessId(businessId);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(orderItemId2);
        orderItem2.setMenuItem(menuItem2);
        orderItem2.setQuantity(3);
        orderItem2.setBusinessId(businessId);

        // Shopping Cart
        ShoppingCart defaultShoppingCart1 = new ShoppingCart();
        defaultShoppingCart1.setId(new ObjectId());
        defaultShoppingCart1.setCustomerId(new ObjectId());
        Map<String, OrderItem> defaultShoppingCartMap1 = new HashMap<>();
        defaultShoppingCartMap1.put(orderItem1.getId().toString(), orderItem1);
        defaultShoppingCartMap1.put(orderItem2.getId().toString(), orderItem2);
        defaultShoppingCart1.setOrderItems(defaultShoppingCartMap1);

        return defaultShoppingCart1;
    }

    ShoppingCart createEmptyCart() {
        // Shopping Cart
        ShoppingCart defaultShoppingCart1 = new ShoppingCart();
        defaultShoppingCart1.setId(new ObjectId());
        defaultShoppingCart1.setCustomerId(new ObjectId());
        Map<String, OrderItem> defaultShoppingCartMap1 = new HashMap<>();
        defaultShoppingCart1.setOrderItems(defaultShoppingCartMap1);

        return defaultShoppingCart1;
    }

    OrderItem createOrderItem() {
        // Menu Items
        ObjectId menuItemObjectId1 = new ObjectId();
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setObjectId(menuItemObjectId1);
        menuItem1.setName("Taco");
        menuItem1.setDescription("taco description");
        menuItem1.setPrice(5);
        menuItem1.setNote("Beef taco");
        // OrderItem
        ObjectId businessId = new ObjectId();
        ObjectId orderItemId1 = new ObjectId();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(orderItemId1);
        orderItem1.setMenuItem(menuItem1);
        orderItem1.setQuantity(2);
        orderItem1.setBusinessId(businessId);

        return orderItem1;
    }

    OrderItem createSingleOrderItem() {
        // Menu Items
        ObjectId menuItemObjectId1 = new ObjectId();
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setObjectId(menuItemObjectId1);
        menuItem1.setName("Taco");
        menuItem1.setDescription("taco description");
        menuItem1.setPrice(5);
        menuItem1.setNote("Beef taco");
        // OrderItem
        ObjectId businessId = new ObjectId();
        ObjectId orderItemId1 = new ObjectId();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(orderItemId1);
        orderItem1.setMenuItem(menuItem1);
        orderItem1.setQuantity(1);
        orderItem1.setBusinessId(businessId);

        return orderItem1;
    }

    @Test
    void testCreateShoppingCart() {
        ObjectId userId = new ObjectId();
        ShoppingCart newCart = shoppingCarts.createShoppingCart(userId);
        Assertions.assertTrue(newCart.isEmpty());
    }

    @Test
    void testCalculateShoppingCartPrice() {
        ShoppingCart shoppingCart = createShoppingCart();
        long expectedPrice = 13l;
        long calculatedPrice = shoppingCarts.calculateShoppingCartPrice(shoppingCart);
        Assertions.assertEquals(expectedPrice, calculatedPrice);
    }

    @Test
    void testCalculateShoppingQuantity() {
        ShoppingCart shoppingCart = createShoppingCart();
        int expectedQuantity = 5;
        int calculatedQuantity = shoppingCarts.calculateShoppingCartQuantity(shoppingCart);
        Assertions.assertEquals(expectedQuantity, calculatedQuantity);
    }

    @Test
    void testGetShoppingCart() throws Exception {
        ShoppingCart shoppingCart = createShoppingCart();
        ObjectId id = shoppingCart.getId();
        shoppingCarts.addShoppingCart(shoppingCart);
        Assertions.assertEquals(shoppingCart, shoppingCarts.getShoppingCart(id));
    }

    @Test
    void testGetShoppingCartByUser() throws Exception {
        ShoppingCart shoppingCart = createShoppingCart();
        ObjectId id = shoppingCart.getCustomerId();
        shoppingCarts.addShoppingCart(shoppingCart);
        Assertions.assertEquals(shoppingCart, shoppingCarts.getShoppingCartByUser(id));
    }

    @Test
    void testGetShoppingCarts() throws Exception {
        ShoppingCart shoppingCart1 = createShoppingCart();
        ShoppingCart shoppingCart2 = createShoppingCart();
        shoppingCarts.addShoppingCart(shoppingCart1);
        shoppingCarts.addShoppingCart(shoppingCart2);
        Collection<ShoppingCart> returnValue = shoppingCarts.getShoppingCarts();
        for (ShoppingCart cart : returnValue) {
            Assertions.assertFalse(cart.isEmpty());
        }
    }

    @Test
    void testAddShoppingCart() throws Exception {
        ShoppingCart shoppingCart = createShoppingCart();
        shoppingCarts.addShoppingCart(shoppingCart);
        Collection<ShoppingCart> carts = shoppingCarts.getShoppingCarts();
        carts.contains(shoppingCart);
    }

    @Test
    void testAddShoppingCartDuplicate() throws Exception {
        ShoppingCart shoppingCart = createShoppingCart();
        shoppingCarts.addShoppingCart(shoppingCart);
        Assertions.assertThrows(Exception.class, () -> {
            shoppingCarts.addShoppingCart(shoppingCart);
        });
    }

    @Test
    void testCheckoutAndClearCart() throws Exception {
        ShoppingCart shoppingCart1 = createShoppingCart();
        shoppingCarts.checkout(shoppingCart1);
        Assertions.assertTrue(shoppingCart1.getOrderItems().isEmpty());
        Assertions.assertTrue(shoppingCart1.getTotalPrice() == 0);
        Assertions.assertTrue(shoppingCart1.getTotalQuantity() == 0);
    }

    @Test
    void testAddOrderItem() throws Exception {
        ShoppingCart shoppingCart1 = createEmptyCart();
        OrderItem item1 = createOrderItem();
        shoppingCarts.addOrderItem(item1, shoppingCart1);
        Assertions.assertTrue(shoppingCart1.getOrderItems().containsValue(item1));
    }

    @Test
    void testRemoveOrderItem() throws Exception {
        ShoppingCart shoppingCart1 = createEmptyCart();
        OrderItem item1 = createOrderItem();
        shoppingCarts.addOrderItem(item1, shoppingCart1);
        Assertions.assertTrue(shoppingCart1.getOrderItems().containsValue(item1));
        shoppingCarts.removeOrderItem(item1, shoppingCart1);
        Assertions.assertFalse(shoppingCart1.getOrderItems().containsValue(item1));
    }

    @Test
    void testIncrementOrderItemQuantity() throws Exception {
        ShoppingCart shoppingCart1 = createEmptyCart();
        OrderItem item1 = createOrderItem();
        int originalQuantity = item1.getQuantity();
        shoppingCarts.addOrderItem(item1, shoppingCart1);
        shoppingCarts.incrementOrderItemQuantity(item1, shoppingCart1);
        int newQuantity = shoppingCart1.getOrderItems().get(item1.getId().toString()).getQuantity();
        Assertions.assertEquals(originalQuantity + 1, newQuantity);
    }

    @Test
    void testDecrementOrderItemQuantity() throws Exception {
        ShoppingCart shoppingCart1 = createEmptyCart();
        OrderItem item1 = createOrderItem();
        int originalQuantity = item1.getQuantity();
        shoppingCarts.addOrderItem(item1, shoppingCart1);
        shoppingCarts.decrementOrderItemQuantity(item1, shoppingCart1);
        int newQuantity = shoppingCart1.getOrderItems().get(item1.getId().toString()).getQuantity();
        Assertions.assertEquals(originalQuantity - 1, newQuantity);
    }

    @Test
    void testDecrementOrderItemQuantityRemove() throws Exception {
        ShoppingCart shoppingCart1 = createEmptyCart();
        OrderItem item1 = createSingleOrderItem();
        shoppingCarts.addOrderItem(item1, shoppingCart1);
        shoppingCarts.decrementOrderItemQuantity(item1, shoppingCart1);
        Assertions.assertTrue(shoppingCart1.isEmpty());
    }

    @Test
    void testUpdateShoppingCart() throws Exception {
        ShoppingCart shoppingCart1 = createShoppingCart();
        ShoppingCart shoppingCart2 = createEmptyCart();
        shoppingCarts.addShoppingCart(shoppingCart2);
        shoppingCart2.setOrderItems(shoppingCart1.getOrderItems());
        shoppingCarts.updateShoppingCart(shoppingCart2);
        Assertions.assertEquals(shoppingCarts.getShoppingCart(shoppingCart2.getId()).getOrderItems(),
                shoppingCart1.getOrderItems());
    }

    @Test
    void testCannotUpdateShoppingCart() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            shoppingCarts.updateShoppingCart(null);
        });
    }

    @Test
    void testUpdateShoppingCartPriceQuantity() throws Exception {
        ShoppingCart shoppingCart1 = createShoppingCart();
        ShoppingCart shoppingCart2 = createEmptyCart();
        long price = shoppingCart1.getTotalPrice();
        int quantity = shoppingCart2.getTotalQuantity();
        shoppingCarts.addShoppingCart(shoppingCart2);
        shoppingCart2.setOrderItems(shoppingCart1.getOrderItems());
        shoppingCarts.updateShoppingCart(shoppingCart2);
        Assertions.assertEquals(shoppingCarts.getShoppingCart(shoppingCart2.getId()).getTotalPrice(), price);
        Assertions.assertEquals(shoppingCarts.getShoppingCart(shoppingCart2.getId()).getTotalQuantity(), quantity);
    }

    @Test
    void testDelete() throws Exception {
        ShoppingCart shoppingCart1 = createShoppingCart();
        shoppingCarts.addShoppingCart(shoppingCart1);
        Assertions.assertTrue(shoppingCarts.getShoppingCarts().contains(shoppingCart1));
        shoppingCarts.deleteShoppingCart(shoppingCart1.getId());
        Assertions.assertFalse(shoppingCarts.getShoppingCarts().contains(shoppingCart1));
    }
}
