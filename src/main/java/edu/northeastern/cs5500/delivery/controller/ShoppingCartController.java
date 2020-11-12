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
        cartToUser = createCartToUserMap(shoppingCartRepository);

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

        // Shopping Carts
        ShoppingCart defaultShoppingCart1 = new ShoppingCart();
        ShoppingCart defaultShoppingCart2 = new ShoppingCart();
        defaultShoppingCart1.setId(new ObjectId());
        defaultShoppingCart1.setCustomerId(new ObjectId());
        Map<String, OrderItem> defaultShoppingCartMap1 = new HashMap<>();
        defaultShoppingCartMap1.put(orderItem1.getId().toString(), orderItem1);
        defaultShoppingCartMap1.put(orderItem2.getId().toString(), orderItem2);
        defaultShoppingCart1.setShoppingCart(defaultShoppingCartMap1);
        try {
            addShoppingCart(defaultShoppingCart1);
            addShoppingCart(defaultShoppingCart2);
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
    private Map<ObjectId, ObjectId> createCartToUserMap(
            GenericRepository<ShoppingCart> shoppingCartRepository) {
        HashMap<ObjectId, ObjectId> cartToUserMap = new HashMap<>();
        Collection<ShoppingCart> allShoppingCarts = shoppingCartRepository.getAll();
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
        long totalPrice = 0;
        for (OrderItem orderItem : shoppingCart.getShoppingCart().values()) {
            totalPrice += orderItem.getMenuItem().getPrice();
        }
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

        return shoppingCarts.add(shoppingCart);
    }

    public void addOrderItem(OrderItem orderItemToAdd, ShoppingCart shoppingCart) throws Exception {
        log.debug("ShoppingCartController > addOrderItem(...)");
        Map<String, OrderItem> activeShoppingCart = shoppingCart.getShoppingCart();
        activeShoppingCart.put(orderItemToAdd.getId().toString(), orderItemToAdd);
        shoppingCart.setShoppingCart(activeShoppingCart);
        updateShoppingCart(shoppingCart);
    }

    public void updateShoppingCart(@Nonnull ShoppingCart shoppingCart) throws Exception {
        log.debug("ShoppingCartController > updateShoppingCart(...)");
        shoppingCarts.update(shoppingCart);
    }

    public void deleteShoppingCart(@Nonnull ObjectId id) throws Exception {
        log.debug("ShoppingCartController > deleteShoppingCart(...)");
        shoppingCarts.delete(id);
    }
}
