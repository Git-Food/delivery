package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.ShoppingCart;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    @Inject
    ShoppingCartController(
            GenericRepository<ShoppingCart> shoppingCartRepository,
            OrderController orderControllerInstance) {
        shoppingCarts = shoppingCartRepository;
        orderController = orderControllerInstance;

        log.info("ShoppingCartController > construct");

        if (shoppingCarts.count() > 0) {
            return;
        }

        log.info("ShoppingCartController > construct > adding default shopping carts");
        // Menu Items
        ShoppingCart defaultShoppingCart1 = new ShoppingCart();
        defaultShoppingCart1.setId(new ObjectId());
        defaultShoppingCart1.setCustomerId(new ObjectId());
        defaultShoppingCart1.setShoppingCart(
                orderController.getOrders().stream()
                        .collect(Collectors.toMap(x -> x.getId().toString(), Function.identity())));
        try {
            addShoppingCart(defaultShoppingCart1);
            // Updates the price and quantity for each shoppingCart
            for (ShoppingCart shoppingCart : shoppingCarts.getAll()) {
                calculateShoppingCartPrice(shoppingCart);
                calculateShoppingCartQuantity(shoppingCart);
            }
        } catch (Exception e) {
            log.error(
                    "ShoppingCartController > construct > adding default shoppingCart > failure?");
            e.printStackTrace();
        }
    }

    /**
     * Calculate total price for a shoppingCart.
     *
     * @param ShoppingCart shoppingCart to calculate total price from
     * @return totalPrice for the shoppingCart
     */
    public long calculateShoppingCartPrice(ShoppingCart shoppingCart) {
        long totalPrice = 0;
        for (Order order : shoppingCart.getShoppingCart().values()) {
            totalPrice += orderController.calculateOrderPrice(order);
        }
        // Update shoppingCart
        shoppingCart.setTotalPrice(totalPrice);
        try {
            updateShoppingCart(shoppingCart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return the total price of this shoppingCart
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
        for (Order order : shoppingCart.getShoppingCart().values()) {
            totalQuantity += orderController.calculateItemQuantity(order);
        }

        shoppingCart.setTotalQuantity(totalQuantity);
        try {
            updateShoppingCart(shoppingCart);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalQuantity;
    }

    @Nullable
    public ShoppingCart getShoppingCart(@Nonnull ObjectId uuid) {
        log.debug("ShoppingCartController > getShoppingCart({})", uuid);
        return shoppingCarts.get(uuid);
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

    public void updateShoppingCart(@Nonnull ShoppingCart shoppingCart) throws Exception {
        log.debug("ShoppingCartController > updateShoppingCart(...)");
        shoppingCarts.update(shoppingCart);
    }

    public void deleteShoppingCart(@Nonnull ObjectId id) throws Exception {
        log.debug("ShoppingCartController > deleteShoppingCart(...)");
        shoppingCarts.delete(id);
    }
}
