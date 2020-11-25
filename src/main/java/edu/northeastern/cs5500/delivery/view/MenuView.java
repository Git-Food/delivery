package edu.northeastern.cs5500.delivery.view;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.delivery.JsonTransformer;
import edu.northeastern.cs5500.delivery.controller.MenuController;
import edu.northeastern.cs5500.delivery.model.Menu;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class MenuView implements View {

    @Inject
    MenuView() {}

    @Inject JsonTransformer jsonTransformer;

    @Inject MenuController menuController;

    @Override
    public void register() {
        log.info("MenuView > register");

        get(
                "/menu",
                (request, response) -> {
                    log.debug("/menu");
                    response.type("application/json");
                    return menuController.getMenus();
                },
                jsonTransformer);

        get(
                "/menu/:id",
                (request, response) -> {
                    final String paramId = request.params(":id");
                    log.debug("/menu/:id<{}>", paramId);
                    final ObjectId id = new ObjectId(paramId);
                    Menu menu = menuController.getMenu(id);
                    if (menu == null) {
                        halt(404);
                    }
                    response.type("application/json");
                    return menu;
                },
                jsonTransformer);

        post(
                "/menu",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Menu menu = mapper.readValue(request.body(), Menu.class);
                    if (!menu.isValid()) {
                        response.status(400);
                        return "";
                    }

                    // Ignore the user-provided ID if there is one
                    menu.setId(null);
                    menu = menuController.addMenu(menu);

                    response.redirect(String.join("/menu/", menu.getId().toHexString()), 301);
                    return menu;
                });

        put(
                "/menu",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Menu menu = mapper.readValue(request.body(), Menu.class);
                    if (!menu.isValid()) {
                        response.status(400);
                        return "";
                    }

                    menuController.updateMenu(menu);
                    return menu;
                });

        delete(
                "/menu",
                (request, response) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Menu menu = mapper.readValue(request.body(), Menu.class);

                    menuController.deleteMenu(menu.getId());
                    return menu;
                });
    }
}
