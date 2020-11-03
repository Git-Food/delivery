package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.ShoppingCartController;
import edu.northeastern.cs5500.delivery.model.ShoppingCart;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class ShoppingCartView implements View {
    @Inject
    ShoppingCartView() {
    }

    @Inject
    JsonTransformer jsonTransformer;

    @Inject
    ShoppingCartController shoppingCartController;

    @Override
    public void register() {
        log.info("ShoppingCartView > register");

        get("/shoppingCart", (request, response) -> {
            log.debug("/shoppingCart");
            response.type("application/json");
            return shoppingCartController.getShoppingCarts();
        }, jsonTransformer);

        get("/order/:id", (request, response) -> {
            final String paramId = request.params(":id");
            log.debug("/order/:id<{}>", paramId);
            final ObjectId id = new ObjectId(paramId);
            ShoppingCart shoppingCart = shoppingCartController.getShoppingCart(id);
            if (shoppingCart == null) {
                halt(404);
            }
            response.type("application/json");
            return shoppingCart;
        }, jsonTransformer);

        post("/shoppingCart", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            ShoppingCart shoppingCart = mapper.readValue(request.body(), ShoppingCart.class);
            // There is currently no invalid shoppingCart
            // if (!shoppingCart.isValid()) {
            // response.status(400);
            // return "";
            // }

            // Ignore the user-provided ID if there is one
            shoppingCart.setId(null);
            shoppingCart = shoppingCartController.addShoppingCart(shoppingCart);

            response.redirect(String.format("/shoppingCart/{}", shoppingCart.getId().toHexString()), 301);
            return shoppingCart;
        });

        put("/shoppingCart", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            ShoppingCart shoppingCart = mapper.readValue(request.body(), ShoppingCart.class);
            // There is currently no invalid shoppingCart
            // if (!order.isValid()) {
            // response.status(400);
            // return "";
            // }

            shoppingCartController.updateShoppingCart(shoppingCart);
            return shoppingCart;
        });

        delete("/shoppingCart", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            ShoppingCart shoppingCart = mapper.readValue(request.body(), ShoppingCart.class);

            shoppingCartController.deleteShoppingCart(shoppingCart.getId());
            return shoppingCart;
        });
    }
}
