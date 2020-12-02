package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Menu;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.PostalAddress;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class RestaurantController {
    private final GenericRepository<Restaurant> restaurants;

    @Inject
    RestaurantController(GenericRepository<Restaurant> restaurantRepository) {
        restaurants = restaurantRepository;

        log.info("RestaurantController > construct");

        if (restaurants.count() > 0) {
            return;
        }

        log.info("RestaurantController > construct > adding default restaurants");

        // Build Menu
        ObjectId menuItemObjectId1 = new ObjectId();
        ObjectId menuItemObjectId2 = new ObjectId();
        MenuItem defaultMenuItem1 = new MenuItem();
        defaultMenuItem1.setId(menuItemObjectId1);
        defaultMenuItem1.setName("Chicken1");
        defaultMenuItem1.setDescription("chicken1 description");
        defaultMenuItem1.setPrice(2);
        defaultMenuItem1.setNote("Spicy sauce included");
        defaultMenuItem1.setBusinessId(new ObjectId());
        MenuItem defaultMenuItem2 = new MenuItem();
        defaultMenuItem2.setId(menuItemObjectId2);
        defaultMenuItem2.setName("Beef1");
        defaultMenuItem2.setDescription("beef1 description");
        defaultMenuItem2.setPrice(3);
        defaultMenuItem2.setNote("BBQ sauce included");
        defaultMenuItem2.setBusinessId(new ObjectId());

        ArrayList<MenuItem> defaultMenuItems1 = new ArrayList<MenuItem>();
        defaultMenuItems1.add(defaultMenuItem1);

        final Menu defaultMenu1 = new Menu();
        defaultMenu1.setName("menu name1");
        defaultMenu1.setDescription("menu description1");
        defaultMenu1.setMenuItems(defaultMenuItems1);

        ArrayList<MenuItem> defaultMenuItems2 = new ArrayList<MenuItem>();
        defaultMenuItems2.add(defaultMenuItem2);

        final Menu defaultMenu2 = new Menu();
        defaultMenu2.setName("menu name2");
        defaultMenu2.setDescription("menu description2");
        defaultMenu2.setMenuItems(defaultMenuItems2);
        // Locations
        PostalAddress address1 = new PostalAddress();
        address1.setHouseNumber("410");
        address1.setStreetAddress("Terry Avenue");
        address1.setCity("Seattle");
        address1.setState("WA");
        address1.setZipCode("98109");
        address1.setCountry("United States");

        PostalAddress address2 = new PostalAddress();
        address2.setHouseNumber("832");
        address2.setStreetAddress("Dexter Avenue North");
        address2.setCity("Seattle");
        address2.setState("WA");
        address2.setZipCode("98109");
        address2.setCountry("United States");
        // Build Restaurants
        final Restaurant defaultRestaurant1 = new Restaurant();
        final Restaurant defaultRestaurant2 = new Restaurant();
        defaultRestaurant1.setName("Best Food Restaurant");
        defaultRestaurant1.setMenu(defaultMenu1);
        defaultRestaurant1.setCuisineType("American");
        defaultRestaurant1.setId(new ObjectId());
        defaultRestaurant1.setLocation(address1);
        defaultRestaurant1.setStartTime(LocalTime.of(7, 00, 00));
        defaultRestaurant1.setEndTime(LocalTime.of(7, 00, 00));

        defaultRestaurant2.setName("Eatery");
        defaultRestaurant2.setMenu(defaultMenu2);
        defaultRestaurant2.setCuisineType("Chinese");
        defaultRestaurant2.setId(new ObjectId());
        defaultRestaurant2.setLocation(address2);
        defaultRestaurant2.setStartTime(LocalTime.of(8, 00, 00));
        defaultRestaurant2.setEndTime(LocalTime.of(22, 00, 00));

        try {
            addRestaurant(defaultRestaurant1);
            addRestaurant(defaultRestaurant2);
        } catch (Exception e) {
            log.error(
                    "defaultRestaurant1Controller > construct > adding default restaurants > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Restaurant getRestaurant(@Nonnull ObjectId uuid) {
        log.debug("RestaurantController > getRestaurant({})", uuid);
        return restaurants.get(uuid);
    }

    @Nonnull
    public Collection<Restaurant> getRestaurants() {
        log.debug("RestaurantController > getRestaurants()");
        return restaurants.getAll();
    }

    @Nonnull
    public Restaurant addRestaurant(@Nonnull Restaurant restaurant) throws Exception {
        log.debug("RestaurantController > addRestaurant(...)");
        if (!restaurant.isValid()) {
            throw new Exception("InvalidRestaurantException");
        }

        ObjectId id = restaurant.getId();

        if (id != null && restaurants.get(id) != null) {
            throw new Exception("DuplicateKeyException");
        }

        return restaurants.add(restaurant);
    }

    public void updateRestaurant(@Nonnull Restaurant restaurant) throws Exception {
        log.debug("RestaurantController > updateRestaurant(...)");
        restaurants.update(restaurant);
    }

    public void deleteRestaurant(@Nonnull ObjectId id) throws Exception {
        log.debug("RestaurantController > deleteRestaurant(...)");
        restaurants.delete(id);
    }
}
