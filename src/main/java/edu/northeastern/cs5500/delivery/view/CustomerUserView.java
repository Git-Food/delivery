// package edu.northeastern.cs5500.delivery.view;

// public class CustomerUserView implements View{

// }

package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.CustomerUserController;
import edu.northeastern.cs5500.delivery.model.CustomerUser;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class CustomerUserView implements View {

    @Inject
    CustomerUserView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject CustomerUserController customerUserController;

    @Override
    public void register() {
        log.info("CustomerUserView > register");

        get(
                "/customeruser",
                (request, response) -> {
                    log.debug("/customeruser");
                    response.type("application/json");
                    return customerUserController.getCustomerUsers();
                },
                jsonTransformer);

        get(
                "/customeruser/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/customeruser/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    CustomerUser customerUser = customerUserController.getCustomerUser(id);
                    if (customerUser == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return customerUser;
                },
                jsonTransformer);

        post(
                "/customeruser",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    CustomerUser customerUser =
                            mapper.readValue(request.body(), CustomerUser.class);
                    if (!customerUser.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    customerUser.setId(null);
                    customerUser = customerUserController.addCustomerUser(customerUser);

                    response.redirect(
                            String.join("/customeruser/", customerUser.getId().toHexString()), 301);
                    return customerUser;
                });

        put(
                "/customeruser",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    CustomerUser customerUser =
                            mapper.readValue(request.body(), CustomerUser.class);
                    if (!customerUser.isValid()) {
                        response.status(400);
                        return "";
                    }

                    customerUserController.updateCustomerUser(customerUser);
                    return customerUser;
                });

        delete(
                "/customeruser",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    CustomerUser customerUser =
                            mapper.readValue(request.body(), CustomerUser.class);

                    customerUserController.deleteCustomerUser(customerUser.getId());
                    return customerUser;
                });
    }
}
