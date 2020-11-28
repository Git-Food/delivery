package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.OrderController;
import edu.northeastern.cs5500.delivery.model.Order;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class OrderView implements View {
    @Inject
    OrderView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject OrderController orderController;

    @Override
    public void register() {
        log.info("OrderView > register");

        get(
                "/order",
                (request, response) -> {
                    log.debug("/order");
                    response.type("application/json");
                    return orderController.getOrders();
                },
                jsonTransformer);

        get(
                "/order/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/order/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Order order = orderController.getOrder(id);
                    if (order == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return order;
                },
                jsonTransformer);

        /** API to view orderHistory of a given User */
        get(
                "/orderhistory",
                (request, response) -> {
                    log.debug("/orderHistory");
                    // Get orderHistory from userId
                    String userId = request.queryParams("userId");
                    final ObjectId id = new ObjectId(userId);
                    response.type("application/json");
                    return orderController.getOrdersByUser(id);
                },
                jsonTransformer);

        /** API to view orderStatus of order */
        get(
                "/orderstatus",
                (request, response) -> {
                    log.debug("/orderHistory");
                    // Get orderHistory from userId
                    String orderId = request.queryParams("orderId");
                    final ObjectId id = new ObjectId(orderId);
                    response.type("application/json");
                    return orderController.getOrder(id);
                },
                jsonTransformer);

        post(
                "/order",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Order order = mapper.readValue(request.body(), Order.class);
                    if (!order.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    order.setId(null);
                    order = orderController.addOrder(order);

                    response.redirect("/order/" + order.getId().toHexString(), 301);
                    return order;
                });

        put(
                "/order",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Order order = mapper.readValue(request.body(), Order.class);
                    if (!order.isValid()) {
                        response.status(400);
                        return "";
                    }

                    orderController.updateOrder(order);
                    return order;
                },
                jsonTransformer);

        delete(
                "/order",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Order order = mapper.readValue(request.body(), Order.class);

                    orderController.deleteOrder(order.getId());
                    return order;
                },
                jsonTransformer);
    }
}
