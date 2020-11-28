package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.redirect;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.ShoppingCartController;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.OrderItem;
import edu.northeastern.cs5500.delivery.model.ShoppingCart;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import spark.Redirect.Status;

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
                "/addorderitem",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();

                    MenuItem menuItem =
                            mapper.readValue(request.queryParams("menuitem"), MenuItem.class);
                    String note = request.queryParams("specialnote");
                    Integer quantity = Integer.valueOf(request.queryParams("quantity"));

                    // Build orderItem
                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(new ObjectId());
                    orderItem.setBusinessId(menuItem.getBusinessId());
                    orderItem.setMenuItem(menuItem);
                    orderItem.setQuantity(quantity);
                    orderItem.setSpecialNote(note);

                    // Get shopping cart from userId
                    String userId = request.queryParams("userid");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Add orderItem
                    shoppingCartController.addOrderItem(orderItem, shoppingCart);

                    return shoppingCart;
                },
                jsonTransformer);

        /** API to remove orderItem from a shoppingCart */
        put(
                "/removeorderitem",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    OrderItem orderItem =
                            mapper.readValue(request.queryParams("orderitem"), OrderItem.class);

                    // Get shopping cart from userId
                    String userId = request.queryParams("userid");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Remove orderItem
                    shoppingCartController.removeOrderItem(orderItem, shoppingCart);

                    return shoppingCart;
                },
                jsonTransformer);

        /** API to increment quantity from OrderItem already in ShoppingCart */
        put(
                "/incrementorderitem",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    OrderItem orderItem =
                            mapper.readValue(request.queryParams("orderitem"), OrderItem.class);

                    // Get shopping cart from userId
                    String userId = request.queryParams("userid");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Increment quantity orderItem
                    shoppingCartController.incrementOrderItemQuantity(orderItem, shoppingCart);

                    return shoppingCart;
                },
                jsonTransformer);

        /** API to decrement quantity from OrderItem already in ShoppingCart */
        put(
                "/decrementorderitem",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    OrderItem orderItem =
                            mapper.readValue(request.queryParams("orderitem"), OrderItem.class);

                    // Get shopping cart from userId
                    String userId = request.queryParams("userid");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Decrement quantity orderItem
                    shoppingCartController.decrementOrderItemQuantity(orderItem, shoppingCart);

                    return shoppingCart;
                },
                jsonTransformer);

        /** API to clear Shopping Cart. */
        put(
                "/clearshoppingcart",
                (request, response) -> {
                    // Get shopping cart from userId
                    String userId = request.queryParams("userid");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Clear shopping cart
                    shoppingCartController.clearShoppingCart(shoppingCart);

                    return shoppingCart;
                },
                jsonTransformer);

        /** API to checkout. */
        post(
                "/checkout",
                (request, response) -> {
                    // Get shopping cart from userId
                    String userId = request.queryParams("userid");
                    final ObjectId id = new ObjectId(userId);
                    ShoppingCart shoppingCart = shoppingCartController.getShoppingCartByUser(id);

                    // Checkout shopping cart
                    shoppingCartController.checkout(shoppingCart);

                    // Redirect to orderStatus view
                    // TODO(pcd) Implement orderStatus view
                    redirect.any(
                            "/checkout",
                            "/orderhistory?userid=" + id.toHexString(),
                            Status.MOVED_PERMANENTLY);

                    return shoppingCart;
                });

        get(
                "/shoppingcart",
                (request, response) -> {
                    log.debug("/shoppingCart");
                    response.type("application/json");
                    return shoppingCartController.getShoppingCarts();
                },
                jsonTransformer);

        get(
                "/shoppingcart/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/shoppingcart/:id<{}>", paramId);
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
                "/shoppingcart",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    ShoppingCart shoppingCart =
                            mapper.readValue(request.body(), ShoppingCart.class);

                    // Ignore the user-provided ID if there is one
                    shoppingCart.setId(null);
                    shoppingCart = shoppingCartController.addShoppingCart(shoppingCart);

                    response.redirect("/shoppingCart/" + shoppingCart.getId().toHexString(), 301);
                    return shoppingCart;
                });

        put(
                "/shoppingcart",
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
                },
                jsonTransformer);

        delete(
                "/shoppingcart",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    ShoppingCart shoppingCart =
                            mapper.readValue(request.body(), ShoppingCart.class);

                    shoppingCartController.deleteShoppingCart(shoppingCart.getId());
                    return shoppingCart;
                },
                jsonTransformer);
    }
}
