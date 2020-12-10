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
    private final MenuController menuController;

    @Inject
    RestaurantController(
            GenericRepository<Restaurant> restaurantRepository,
            MenuController menuControllerInstance) {
        restaurants = restaurantRepository;
        menuController = menuControllerInstance;

        log.info("RestaurantController > construct");

        if (restaurants.count() > 0) {
            return;
        }

        log.info("RestaurantController > construct > adding default restaurants");

        // Build Menu
        ObjectId businessObjectId1 = new ObjectId();
        ObjectId businessObjectId2 = new ObjectId();
        ObjectId businessObjectId3 = new ObjectId();

        MenuItem defaultMenuItem1 = new MenuItem();
        MenuItem defaultMenuItem2 = new MenuItem();
        MenuItem defaultMenuItem3 = new MenuItem();
        MenuItem defaultMenuItem4 = new MenuItem();
        MenuItem defaultMenuItem5 = new MenuItem();
        MenuItem defaultMenuItem6 = new MenuItem();
        MenuItem defaultMenuItem7 = new MenuItem();

        defaultMenuItem1.setId(new ObjectId());
        defaultMenuItem1.setName("Chicken burrito");
        defaultMenuItem1.setDescription("Rice, beans, chicken");
        defaultMenuItem1.setNote("Extra chicken");
        defaultMenuItem1.setPrice(8);
        defaultMenuItem1.setBusinessId(businessObjectId1);

        defaultMenuItem2.setId(new ObjectId());
        defaultMenuItem2.setName("Beef salad");
        defaultMenuItem2.setDescription("Romaine salad, rice, beans, beef");
        defaultMenuItem2.setPrice(9);
        defaultMenuItem2.setNote("No source cream");
        defaultMenuItem2.setBusinessId(businessObjectId1);

        defaultMenuItem3.setId(new ObjectId());
        defaultMenuItem3.setName("Kaiseki Gozen");
        defaultMenuItem3.setDescription("7-8 course Kaiseki dishes");
        defaultMenuItem3.setPrice(100);
        defaultMenuItem3.setNote("");
        defaultMenuItem3.setBusinessId(businessObjectId2);

        defaultMenuItem4.setId(new ObjectId());
        defaultMenuItem4.setName("The wa'z Don");
        defaultMenuItem4.setDescription("The highest quality ikura, negitoro, and uni toro");
        defaultMenuItem4.setPrice(50);
        defaultMenuItem4.setNote("Available until Dec 31");
        defaultMenuItem4.setBusinessId(businessObjectId2);

        defaultMenuItem5.setId(new ObjectId());
        defaultMenuItem5.setName("Party Temaki");
        defaultMenuItem5.setDescription("Temaki plate for three");
        defaultMenuItem5.setPrice(135);
        defaultMenuItem5.setNote("4th of July special Temaki");
        defaultMenuItem5.setBusinessId(businessObjectId2);

        defaultMenuItem6.setId(new ObjectId());
        defaultMenuItem6.setName("Shack Burger");
        defaultMenuItem6.setDescription("Cheeseburger with lettuce and tomato");
        defaultMenuItem6.setPrice(9);
        defaultMenuItem6.setNote("Double");
        defaultMenuItem6.setBusinessId(businessObjectId3);

        defaultMenuItem7.setId(new ObjectId());
        defaultMenuItem7.setName("Bacon cheese fries");
        defaultMenuItem7.setDescription("Topped with cheese sauce and applewood-smoked bacon");
        defaultMenuItem7.setPrice(5);
        defaultMenuItem7.setNote("Niman Ranch");
        defaultMenuItem7.setBusinessId(businessObjectId3);

        // Build menu items
        ArrayList<MenuItem> defaultMenuItems1 = new ArrayList<MenuItem>();
        ArrayList<MenuItem> defaultMenuItems2 = new ArrayList<MenuItem>();
        ArrayList<MenuItem> defaultMenuItems3 = new ArrayList<MenuItem>();

        defaultMenuItems1.add(defaultMenuItem1);
        defaultMenuItems1.add(defaultMenuItem2);
        defaultMenuItems2.add(defaultMenuItem3);
        defaultMenuItems2.add(defaultMenuItem4);
        defaultMenuItems2.add(defaultMenuItem5);
        defaultMenuItems3.add(defaultMenuItem6);
        defaultMenuItems3.add(defaultMenuItem7);

        final Menu defaultMenu1 = new Menu();
        final Menu defaultMenu2 = new Menu();
        final Menu defaultMenu3 = new Menu();

        defaultMenu1.setName("Chipotle Menu");
        defaultMenu1.setDescription("Fast food chain offering Mexican fare");
        defaultMenu1.setMenuItems(defaultMenuItems1);
        defaultMenu1.setId(new ObjectId());

        defaultMenu2.setName("Wa'z Menu");
        defaultMenu2.setDescription("Artful kaiseki-style plates");
        defaultMenu2.setMenuItems(defaultMenuItems2);
        defaultMenu2.setId(new ObjectId());

        defaultMenu3.setName("Shake Shack Menu");
        defaultMenu3.setDescription("Hip, counter-server chain");
        defaultMenu3.setMenuItems(defaultMenuItems3);
        defaultMenu3.setId(new ObjectId());

        // Locations
        final PostalAddress address1 = new PostalAddress();
        final PostalAddress address2 = new PostalAddress();
        final PostalAddress address3 = new PostalAddress();

        address1.setHouseNumber("212");
        address1.setStreetAddress("Westlake Ave");
        address1.setCity("Seattle");
        address1.setState("WA");
        address1.setZipCode("98109");
        address1.setCountry("United States");

        address2.setHouseNumber("411");
        address2.setStreetAddress("Cedar St");
        address2.setCity("Seattle");
        address2.setState("WA");
        address2.setZipCode("98121");
        address2.setCountry("United States");

        address3.setHouseNumber("2115");
        address3.setStreetAddress("Westlake Ave");
        address3.setCity("Seattle");
        address3.setState("WA");
        address3.setZipCode("98121");
        address3.setCountry("United States");

        // Build Restaurants
        final Restaurant defaultRestaurant1 = new Restaurant();
        final Restaurant defaultRestaurant2 = new Restaurant();
        final Restaurant defaultRestaurant3 = new Restaurant();

        defaultRestaurant1.setName("Chipotle");
        defaultRestaurant1.setMenuId(defaultMenu1.getId().toHexString());
        defaultRestaurant1.setCuisineType("Mexican");
        defaultRestaurant1.setId(businessObjectId1);
        defaultRestaurant1.setLocation(address1);
        defaultRestaurant1.setStartTime(LocalTime.of(10, 45, 00));
        defaultRestaurant1.setEndTime(LocalTime.of(22, 00, 00));

        defaultRestaurant2.setName("Wa'z Seattle");
        defaultRestaurant2.setMenuId(defaultMenu2.getId().toHexString());
        defaultRestaurant2.setCuisineType("Japanese");
        defaultRestaurant2.setId(businessObjectId2);
        defaultRestaurant2.setLocation(address2);
        defaultRestaurant2.setStartTime(LocalTime.of(18, 00, 00));
        defaultRestaurant2.setEndTime(LocalTime.of(21, 00, 00));

        defaultRestaurant3.setName("Shake Shack");
        defaultRestaurant3.setMenuId(defaultMenu3.getId().toHexString());
        defaultRestaurant3.setCuisineType("American");
        defaultRestaurant3.setId(businessObjectId3);
        defaultRestaurant3.setLocation(address3);
        defaultRestaurant3.setStartTime(LocalTime.of(11, 00, 00));
        defaultRestaurant3.setEndTime(LocalTime.of(22, 00, 00));

        try {
            addRestaurant(defaultRestaurant1);
            addRestaurant(defaultRestaurant2);
            addRestaurant(defaultRestaurant3);
            menuController.addMenu(defaultMenu1);
            menuController.addMenu(defaultMenu2);
            menuController.addMenu(defaultMenu3);
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
