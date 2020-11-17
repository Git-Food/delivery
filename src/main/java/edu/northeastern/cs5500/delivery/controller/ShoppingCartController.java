package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.OrderItem;
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
public class ShoppingCartController {
    private final GenericRepository<ShoppingCart> shoppingCarts;
    private final OrderController orderController;
    private Map<ObjectId, ObjectId> cartToUser;

    @Inject
    ShoppingCartController(
            GenericRepository<ShoppingCart> shoppingCartRepository,
            OrderController orderControllerInstance) {
        shoppingCarts = shoppingCartRepository;
        orderController = orderControllerInstance;
        cartToUser = createCartToUserMap();

        log.info("ShoppingCartController > construct");

        if (shoppingCarts.count() > 0) {
            return;
        }

        log.info("ShoppingCartController > construct > adding default shopping carts");
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

        // Shopping Carts
        ShoppingCart defaultShoppingCart1 = new ShoppingCart();
        defaultShoppingCart1.setId(new ObjectId());
        defaultShoppingCart1.setCustomerId(new ObjectId());
        Map<String, OrderItem> defaultShoppingCartMap1 = new HashMap<>();
        defaultShoppingCartMap1.put(orderItem1.getId().toString(), orderItem1);
        defaultShoppingCartMap1.put(orderItem2.getId().toString(), orderItem2);
        defaultShoppingCart1.setShoppingCart(defaultShoppingCartMap1);
        try {
            addShoppingCart(defaultShoppingCart1);
            // Updates the price and quantity for each shoppingCart
            for (ShoppingCart shoppingCart : shoppingCarts.getAll()) {
                long cartPrice = calculateShoppingCartPrice(shoppingCart);
                int cartQuantity = calculateShoppingCartQuantity(shoppingCart);
                shoppingCart.setTotalPrice(cartPrice);
                shoppingCart.setTotalQuantity(cartQuantity);
                updateShoppingCart(shoppingCart);
            }
        } catch (Exception e) {
            log.error(
                    "ShoppingCartController > construct > adding default shoppingCart > failure?");
            e.printStackTrace();
        }
    }

    /** Return a Map<CustomerUser id, ShoppingCart id> based on a given ShoppingCartRepository. */
    private Map<ObjectId, ObjectId> createCartToUserMap() {
        HashMap<ObjectId, ObjectId> cartToUserMap = new HashMap<>();
        Collection<ShoppingCart> allShoppingCarts = this.shoppingCarts.getAll();
        for (ShoppingCart shoppingCart : allShoppingCarts) {
            // place (customerId : shoppingCartId) in map
            cartToUserMap.put(shoppingCart.getCustomerId(), shoppingCart.getId());
        }

        return cartToUserMap;
    }

    /**
     * Returns a new ShoppingCart object for a given CustomerUser id.
     *
     * @return a new ShoppingCart object for a given CustomerUser id.
     */
    public ShoppingCart createShoppingCart(ObjectId userId) {
        ShoppingCart newCart = new ShoppingCart();
        newCart.setId(new ObjectId());
        newCart.setShoppingCart(new HashMap<String, OrderItem>());
        newCart.setCustomerId(userId);
        return newCart;
    }

    /**
     * Calculate total price for a shoppingCart.
     *
     * @param ShoppingCart shoppingCart to calculate total price from
     * @return totalPrice for the shoppingCart
     */
    public long calculateShoppingCartPrice(ShoppingCart shoppingCart) {
        long totalPrice =
                shoppingCart.getShoppingCart().values().stream()
                        .mapToLong(x -> x.getQuantity() * x.getMenuItem().getPrice())
                        .sum();
        return totalPrice;
    }

    /**
     * Calculate the total quantity for a shoppingCart
     *
     * @param shoppingCart shoppingCart to calculate total quantity from
     * @return totalQuantity total quantity for the shoppingCart
     */
    public int calculateShoppingCartQuantity(ShoppingCart shoppingCart) {
        int totalQuantity = 0;
        for (OrderItem orderItem : shoppingCart.getShoppingCart().values()) {
            totalQuantity += orderItem.getQuantity();
        }
        return totalQuantity;
    }

    @Nullable
    public ShoppingCart getShoppingCart(@Nonnull ObjectId uuid) {
        log.debug("ShoppingCartController > getShoppingCart({})", uuid);
        return shoppingCarts.get(uuid);
    }

    /**
     * Return a ShoppingCart belonging to a CustomerUser identified by id.
     *
     * @param userId ObjectId of a given CustomerUser
     * @return ShoppingCart based on CusterUser id.
     */
    @Nullable
    public ShoppingCart getShoppingCartByUser(@Nonnull ObjectId userId) {
        log.debug("ShoppingCartController > getShoppingCartByUser({})", userId);
        return getShoppingCart(cartToUser.get(userId));
    }

    @Nonnull
    public Collection<ShoppingCart> getShoppingCarts() {
        log.debug("ShoppingCartController > getShoppingCarts()");
        return shoppingCarts.getAll();
    }

    @Nonnull
    public ShoppingCart addShoppingCart(@Nonnull ShoppingCart shoppingCart) throws Exception {
        log.debug("ShoppingCartController > addShoppingCart(...)");
        // All shopping carts are valid, no isValid() method
        // if (!shoppingCart.isValid()) {
        // TODO: replace with a real invalid object exception
        // probably not one exception per object type though...
        // throw new Exception("InvalidShoppingCartException");
        // }

        ObjectId id = shoppingCart.getId();

        if (id != null && shoppingCarts.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }
        this.cartToUser.put(shoppingCart.getCustomerId(), shoppingCart.getId());
        return shoppingCarts.add(shoppingCart);
    }

    /**
     * Creates an Order based on OrderItem contents of ShoppingCart processes payment, and submits
     * the created Order.
     *
     * @param shoppingCart ShoppingCart from which an Order is created
     * @throws Exception
     */
    // TODO(shh) Incorporate payments interface???
    public void checkout(ShoppingCart shoppingCart) throws Exception {
        log.debug("OrderController > checkout(...)");
        orderController.submitOrder(shoppingCart);
        clearShoppingCart(shoppingCart);
    }

    /**
     * Clears the provided ShoppingCart object of all OrderItems.
     *
     * @param shoppingCart ShoppingCart object to be cleared
     * @throws Exception TODO (shh) create custom exception
     */
    public void clearShoppingCart(ShoppingCart shoppingCart) throws Exception {
        log.debug("ShoppingCartController > clearShoppingCart(...)");
        Map<String, OrderItem> newShoppingCartMap = new HashMap<>();
        shoppingCart.setShoppingCart(newShoppingCartMap);
        updateShoppingCart(shoppingCart, 0, 0);
    }

    /**
     * Adds the provided OrderItem to the provided ShopppingCart and updates the ShoppingCart.
     *
     * @param orderItemToAdd OrderItem object to be added to the provided ShoppingCart
     * @param shoppingCart ShoppingCart object into which the given OrderItem is added
     * @throws Exception TODO (shh) create custome exception
     */
    /*TODO Impement logic when adding an OrderItem that has an businesId that does not match
      the existing Order Items...
      Do we raise an exception?
      Do we automatically clear out the cart and add the chosen OrderItem?
    */
    public void addOrderItem(OrderItem orderItemToAdd, ShoppingCart shoppingCart) throws Exception {
        log.debug("ShoppingCartController > addOrderItem(...)");
        Map<String, OrderItem> activeShoppingCart = shoppingCart.getShoppingCart();
        activeShoppingCart.put(orderItemToAdd.getId().toString(), orderItemToAdd);
        shoppingCart.setShoppingCart(activeShoppingCart);
        long cartPrice = calculateShoppingCartPrice(shoppingCart);
        int cartQuantity = calculateShoppingCartQuantity(shoppingCart);
        updateShoppingCart(shoppingCart, cartPrice, cartQuantity);
    }

    /**
     * Removes the provided OrderItem completely from the provided ShopppingCart regarless of
     * OrderItem quantity and updates the ShoppingCart.
     *
     * @param orderItemToAdd OrderItem object to be removed from the provided ShoppingCart
     * @param shoppingCart ShoppingCart object from which the given OrderItem is removed from
     * @throws Exception TODO (shh) create custom exception
     */
    public void removeOrderItem(OrderItem orderItemToRemove, ShoppingCart shoppingCart)
            throws Exception {
        log.debug("ShoppingCartController > removeOrderItem(...)");
        Map<String, OrderItem> activeShoppingCart = shoppingCart.getShoppingCart();
        activeShoppingCart.remove(orderItemToRemove.getId().toString());
        shoppingCart.setShoppingCart(activeShoppingCart);
        long cartPrice = calculateShoppingCartPrice(shoppingCart);
        int cartQuantity = calculateShoppingCartQuantity(shoppingCart);
        updateShoppingCart(shoppingCart, cartPrice, cartQuantity);
    }

    /**
     * Increments the OrderItem quantity of the provided orderItem within the given ShoppingCart by
     * 1.
     *
     * @param orderItem OrderItem whose quantity is being incremented by 1
     * @param shoppingCart Shopping Cart in which OrderItem quantity is being incremented
     * @throws Exception TODO (shh) create custom exception
     */
    public void incrementOrderItemQuantity(OrderItem orderItem, ShoppingCart shoppingCart)
            throws Exception {
        log.debug("ShoppingCartController > incrementOrderItem(...)");
        Map<String, OrderItem> activeShoppingCart = shoppingCart.getShoppingCart();
        OrderItem currentItem = activeShoppingCart.get(orderItem.getId().toString());
        currentItem.setQuantity(currentItem.getQuantity() + 1);
        activeShoppingCart.put(orderItem.getId().toString(), currentItem);
        shoppingCart.setShoppingCart(activeShoppingCart);
        long cartPrice = calculateShoppingCartPrice(shoppingCart);
        int cartQuantity = calculateShoppingCartQuantity(shoppingCart);
        updateShoppingCart(shoppingCart, cartPrice, cartQuantity);
    }

    /**
     * Decrements the OrderItem quantity of the provided orderItem within the given ShoppingCart by
     * 1.
     *
     * @param orderItem OrderItem whose quantity is being decremented by 1
     * @param shoppingCart Shopping Cart in which OrderItem quantity is being decremented
     * @throws Exception TODO (shh) create custom exception
     */
    public void decrementOrderItemQuantity(OrderItem orderItem, ShoppingCart shoppingCart)
            throws Exception {
        log.debug("ShoppingCartController > decrementOrderItem(...)");
        Map<String, OrderItem> activeShoppingCart = shoppingCart.getShoppingCart();
        OrderItem currentItem = activeShoppingCart.get(orderItem.getId().toString());
        if (currentItem.getQuantity() <= 1) {
            removeOrderItem(orderItem, shoppingCart);
        } else {
            currentItem.setQuantity(currentItem.getQuantity() - 1);
            activeShoppingCart.put(orderItem.getId().toString(), currentItem);
            shoppingCart.setShoppingCart(activeShoppingCart);
            long cartPrice = calculateShoppingCartPrice(shoppingCart);
            int cartQuantity = calculateShoppingCartQuantity(shoppingCart);
            updateShoppingCart(shoppingCart, cartPrice, cartQuantity);
        }
    }

    public void updateShoppingCart(@Nonnull ShoppingCart shoppingCart) throws Exception {
        log.debug("ShoppingCartController > updateShoppingCart(...)");
        shoppingCarts.update(shoppingCart);
    }

    /**
     * Updates the provided ShoppingCart in the repository given an updated cartPrice and
     * cartItemQuantity.
     *
     * @param shoppingCart ShoppingCart object to be updated in the repository
     * @param cartPrice New ShoppingCart price to be set and updated
     * @param cartItemQuantity New ShoppingCart item quantity to be set and updated
     * @throws Exception TODO (shh) create custom exception
     */
    public void updateShoppingCart(ShoppingCart shoppingCart, long cartPrice, int cartItemQuantity)
            throws Exception {
        log.debug("ShoppingCartController > updateShoppingCart(ShoppingCart, long, int)");
        shoppingCart.setTotalPrice(cartPrice);
        shoppingCart.setTotalQuantity(cartItemQuantity);
        updateShoppingCart(shoppingCart);
    }

    public void deleteShoppingCart(@Nonnull ObjectId id) throws Exception {
        log.debug("ShoppingCartController > deleteShoppingCart(...)");
        this.cartToUser.remove(id);
        shoppingCarts.delete(id);
    }
}
