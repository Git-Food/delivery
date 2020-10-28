package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Menu;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class MenuController {
    private final GenericRepository<Menu> menus;

    @Inject
    MenuController(GenericRepository<Menu> menuRepository) {
        menus = menuRepository;

        log.info("MenuController > construct");

        if (menus.count() > 0) {
            return;
        }

        log.info("MenuController > construct > adding default menus");
        final HashMap<ObjectId, MenuItem> defaultMenuItem1 = new HashMap<ObjectId, MenuItem>();
        ObjectId menuItemObjectId1 = new ObjectId();
        ObjectId menuItemObjectId2 = new ObjectId();
        defaultMenuItem1.put(
                menuItemObjectId1,
                MenuItem.builder()
                        .objectId(menuItemObjectId1)
                        .name("menu item 1 name")
                        .description("menu item 1 description")
                        .note("menu item note 1")
                        .build());
        defaultMenuItem1.put(
                menuItemObjectId2,
                MenuItem.builder()
                        .objectId(menuItemObjectId2)
                        .name("menu item 2 name")
                        .description("menu item 2 description")
                        .note("menu item note 2")
                        .build());
        final Menu defaultMenu1 =
                Menu.builder()
                        .name("menu name1")
                        .description("menu description1")
                        .menuItems(defaultMenuItem1)
                        .build();
        final Menu defaultMenu2 =
                Menu.builder()
                        .name("menu name2")
                        .description("menu description2")
                        .menuItems(new HashMap<ObjectId, MenuItem>())
                        .build();

        try {
            addMenu(defaultMenu1);
            addMenu(defaultMenu2);
        } catch (Exception e) {
            log.error("MenuController > construct > adding default menus > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Menu getMenu(@Nonnull ObjectId uuid) {
        log.debug("MenuController > getMenu({})", uuid);
        return menus.get(uuid);
    }

    @Nonnull
    public Collection<Menu> getMenus() {
        log.debug("MenuController > getMenus()");
        return menus.getAll();
    }

    @Nonnull
    public Menu addMenu(@Nonnull Menu menu) throws Exception {
        log.debug("MenuController > addMenu(...)");
        if (!menu.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidMenuException");
        }

        ObjectId id = menu.getId();

        if (id != null && menus.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return menus.add(menu);
    }

    public void updateMenu(@Nonnull Menu menu) throws Exception {
        log.debug("MenuController > updateMenu(...)");
        menus.update(menu);
    }

    public void deleteMenu(@Nonnull ObjectId id) throws Exception {
        log.debug("MenuController > deleteMenu(...)");
        menus.delete(id);
    }
}
