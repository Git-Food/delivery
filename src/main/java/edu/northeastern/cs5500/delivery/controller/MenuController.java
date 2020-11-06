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
        ObjectId menuItemObjectId1 = new ObjectId();
        ObjectId menuItemObjectId2 = new ObjectId();
        MenuItem defaultMenuItem1 = new MenuItem();
        defaultMenuItem1.setObjectId(menuItemObjectId1);
        defaultMenuItem1.setName("Chicken1");
        defaultMenuItem1.setDescription("chicken1 description");
        defaultMenuItem1.setPrice(2);
        defaultMenuItem1.setNote("Spicy sauce included");
        MenuItem defaultMenuItem2 = new MenuItem();
        defaultMenuItem2.setObjectId(menuItemObjectId2);
        defaultMenuItem2.setName("Beef1");
        defaultMenuItem2.setDescription("beef1 description");
        defaultMenuItem2.setPrice(3);
        defaultMenuItem2.setNote("BBQ sauce included");
        HashMap<ObjectId, MenuItem> defaultMenuItems1 = new HashMap<>();
        defaultMenuItems1.put(menuItemObjectId1, defaultMenuItem1);
        HashMap<ObjectId, MenuItem> defaultMenuItems2 = new HashMap<>();
        defaultMenuItems2.put(menuItemObjectId2, defaultMenuItem2);
        final Menu defaultMenu1 =
                Menu.builder()
                        .name("menu name1")
                        .description("menu description1")
                        .menuItems(defaultMenuItems1)
                        .build();
        final Menu defaultMenu2 =
                Menu.builder()
                        .name("menu name2")
                        .description("menu description2")
                        .menuItems(defaultMenuItems2)
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
