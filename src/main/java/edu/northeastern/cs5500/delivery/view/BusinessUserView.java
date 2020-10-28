package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.BusinessUserController;
import edu.northeastern.cs5500.delivery.model.BusinessUser;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class BusinessUserView implements View {

    @Inject
    BusinessUserView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject BusinessUserController businessUserController;

    @Override
    public void register() {
        log.info("BusinessUserView > register");

        get(
                "/businessuser",
                (request, response) -> {
                    log.debug("/businessuser");
                    response.type("application/json");
                    return businessUserController.getBusinessUsers();
                },
                jsonTransformer);

        get(
                "/businessuser/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/businessuser/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    BusinessUser customerUser = businessUserController.getBusinessUser(id);
                    if (customerUser == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return customerUser;
                },
                jsonTransformer);

        post(
                "/businessuser",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    BusinessUser customerUser =
                            mapper.readValue(request.body(), BusinessUser.class);
                    if (!customerUser.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    customerUser.setId(null);
                    customerUser = businessUserController.addBusinessUser(customerUser);

                    response.redirect(
                            String.format("/businessuser/{}", customerUser.getId().toHexString()),
                            301);
                    return customerUser;
                });

        put(
                "/businessuser",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    BusinessUser customerUser =
                            mapper.readValue(request.body(), BusinessUser.class);
                    if (!customerUser.isValid()) {
                        response.status(400);
                        return "";
                    }

                    businessUserController.updateBusinessUser(customerUser);
                    return customerUser;
                });

        delete(
                "/businessuser",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    BusinessUser customerUser =
                            mapper.readValue(request.body(), BusinessUser.class);

                    businessUserController.deleteBusinessUser(customerUser.getId());
                    return customerUser;
                });
    }
}
