package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.DriverUserController;
import edu.northeastern.cs5500.delivery.model.DriverUser;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class DriverUserView implements View {

    @Inject
    DriverUserView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject DriverUserController driversController;

    @Override
    public void register() {
        log.info("DriverUserView > register");

        get(
                "/driver",
                (request, response) -> {
                    log.debug("/driver");
                    response.type("application/json");
                    return driversController.getDrivers();
                },
                jsonTransformer);

        get(
                "/driver/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/driver/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    System.out.println(id);
                    DriverUser driver = driversController.getDriver(id);
                    if (driver == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return driver;
                },
                jsonTransformer);

        post(
                "/driver",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    DriverUser driver = mapper.readValue(request.body(), DriverUser.class);
                    if (!driver.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    driver.setId(null);
                    driver = driversController.addDriver(driver);

                    response.redirect("/driver/" + driver.getId().toHexString(), 301);
                    return driver;
                });

        put(
                "/driver",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    DriverUser driver = mapper.readValue(request.body(), DriverUser.class);
                    if (!driver.isValid()) {
                        response.status(400);
                        return "";
                    }

                    driversController.updateDriver(driver);
                    return driver;
                });

        delete(
                "/driver",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    DriverUser driver = mapper.readValue(request.body(), DriverUser.class);

                    driversController.deleteDriver(driver.getId());
                    return driver;
                });
    }
}
