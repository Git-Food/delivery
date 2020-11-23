package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.ShoppingCartController;
import edu.northeastern.cs5500.delivery.model.OrderItem;
import edu.northeastern.cs5500.delivery.model.ShoppingCart;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class ShoppingCartView implements View {
    @Inject
    ShoppingCartView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject ShoppingCartController shoppingCartController;

    @Override
    public void register() {
        log.info("ShoppingCartView > register");

        /** API to add orderItem into a shoppingCart */
        put(
                "/addOrderItem",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    OrderItem orderItem =
                            mapper.readValue(request.queryParams("orderItem"), OrderItem.class);

                    // Get shopping cart from userId
                    String userId = request.queryParams("userId");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // If shopping cart for the user doesn't exist
                    if (shoppingCart == null) {
                        shoppingCart = shoppingCartController.createShoppingCart(id);
                        shoppingCartController.addShoppingCart(shoppingCart);
                    }

                    // Add orderItem
                    shoppingCartController.addOrderItem(orderItem, shoppingCart);

                    return shoppingCart;
                });

        /** API to remove orderItem from a shoppingCart */
        put(
                "/removeOrderItem",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    OrderItem orderItem =
                            mapper.readValue(request.queryParams("orderItem"), OrderItem.class);

                    // Get shopping cart from userId
                    String userId = request.queryParams("userId");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Remove orderItem
                    shoppingCartController.removeOrderItem(orderItem, shoppingCart);

                    return shoppingCart;
                });

        /** API to increment quantity from OrderItem already in ShoppingCart */
        put(
                "/incrementOrderItem",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    OrderItem orderItem =
                            mapper.readValue(request.queryParams("orderItem"), OrderItem.class);

                    // Get shopping cart from userId
                    String userId = request.queryParams("userId");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Increment quantity orderItem
                    shoppingCartController.incrementOrderItemQuantity(orderItem, shoppingCart);

                    return shoppingCart;
                });

        /** API to decrement quantity from OrderItem already in ShoppingCart */
        put(
                "/decrementOrderItem",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    OrderItem orderItem =
                            mapper.readValue(request.queryParams("orderItem"), OrderItem.class);

                    // Get shopping cart from userId
                    String userId = request.queryParams("userId");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Decrement quantity orderItem
                    shoppingCartController.decrementOrderItemQuantity(orderItem, shoppingCart);

                    return shoppingCart;
                });

        /** API to clear Shopping Cart. */
        put(
                "/clearShoppingCart",
                (request, response) -> {
                    // Get shopping cart from userId
                    String userId = request.queryParams("userId");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Clear shopping cart
                    shoppingCartController.clearShoppingCart(shoppingCart);

                    return shoppingCart;
                });

        /** API to checkout. */
        post(
                "/checkout",
                (request, response) -> {
                    // Get shopping cart from userId
                    String userId = request.queryParams("userId");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Checkout shopping cart
                    shoppingCartController.checkout(shoppingCart);

                    // Redirect to orderStatus view
                    // TODO(pcd) Implement orderStatus view
                    response.redirect("/orderStatus");

                    return shoppingCart;
                });

        get(
                "/shoppingCart",
                (request, response) -> {
                    log.debug("/shoppingCart");
                    response.type("application/json");
                    return shoppingCartController.getShoppingCarts();
                },
                jsonTransformer);

        get(
                "/shoppingCart/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/shoppingCart/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCart(id);
                    // There is currently no invalid shoppingCart
                    // if (shoppingCart == null) {
                    // halt(404);
                    // }
                    response.type("application/json");
                    return shoppingCart;
                },
                jsonTransformer);

        post(
                "/shoppingCart",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    ShoppingCart shoppingCart =
                            mapper.readValue(request.body(), ShoppingCart.class);
                    // There is currently no invalid shoppingCart
                    // if (!shoppingCart.isValid()) {
                    // response.status(400);
                    // return "";
                    // }

                    // Ignore the user-provided ID if there is one
                    shoppingCart.setId(null);
                    shoppingCart = shoppingCartController.addShoppingCart(shoppingCart);

                    response.redirect(
                            String.format("/shoppingCart/{}", shoppingCart.getId().toHexString()),
                            301);
                    return shoppingCart;
                });

        put(
                "/shoppingCart",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    ShoppingCart shoppingCart =
                            mapper.readValue(request.body(), ShoppingCart.class);
                    // There is currently no invalid shoppingCart
                    // if (!order.isValid()) {
                    // response.status(400);
                    // return "";
                    // }

                    shoppingCartController.updateShoppingCart(shoppingCart);
                    return shoppingCart;
                });

        delete(
                "/shoppingCart",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    ShoppingCart shoppingCart =
                            mapper.readValue(request.body(), ShoppingCart.class);

                    shoppingCartController.deleteShoppingCart(shoppingCart.getId());
                    return shoppingCart;
                });
    }
}
