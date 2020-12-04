package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.Menu;
import edu.northeastern.cs5500.delivery.model.MenuItem;
import edu.northeastern.cs5500.delivery.model.PostalAddress;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestaurantControllerTest {
    RestaurantController restaurants;
    MenuController menus;

    @BeforeEach
    void init() {
        restaurants = new RestaurantController(new InMemoryRepository<Restaurant>(), menus);
    }

    Restaurant createRestaurantOne() {
        // Build Menu
        ObjectId menuItemObjectId1 = new ObjectId();
        MenuItem defaultMenuItem1 = new MenuItem();
        defaultMenuItem1.setId(menuItemObjectId1);
        defaultMenuItem1.setName("Chicken1");
        defaultMenuItem1.setDescription("chicken1 description");
        defaultMenuItem1.setPrice(2);
        defaultMenuItem1.setNote("Spicy sauce included");
        defaultMenuItem1.setBusinessId(new ObjectId());

        ArrayList<MenuItem> defaultMenuItems1 = new ArrayList<MenuItem>();
        defaultMenuItems1.add(defaultMenuItem1);

        final Menu defaultMenu1 = new Menu();
        ObjectId menuObjectId1 = new ObjectId();
        defaultMenu1.setName("menu name1");
        defaultMenu1.setDescription("menu description1");
        defaultMenu1.setMenuItems(defaultMenuItems1);
        defaultMenu1.setId(menuObjectId1);

        // Locations
        PostalAddress address1 = new PostalAddress();
        address1.setHouseNumber("410");
        address1.setStreetAddress("Terry Avenue");
        address1.setCity("Seattle");
        address1.setState("WA");
        address1.setZipCode("98109");
        address1.setCountry("United States");
        // Build Restaurants
        final Restaurant defaultRestaurant1 = new Restaurant();
        defaultRestaurant1.setName("Best Food Restaurant");
        defaultRestaurant1.setMenuId(defaultMenu1.getId().toString());
        defaultRestaurant1.setCuisineType("American");
        defaultRestaurant1.setId(new ObjectId());
        defaultRestaurant1.setLocation(address1);
        defaultRestaurant1.setStartTime(LocalTime.of(7, 00, 00));
        defaultRestaurant1.setEndTime(LocalTime.of(7, 00, 00));

        return defaultRestaurant1;
    }

    Restaurant createRestaurantTwo() {
        // Build Menu
        ObjectId menuItemObjectId2 = new ObjectId();
        MenuItem defaultMenuItem2 = new MenuItem();
        defaultMenuItem2.setId(menuItemObjectId2);
        defaultMenuItem2.setName("Beef1");
        defaultMenuItem2.setDescription("beef1 description");
        defaultMenuItem2.setPrice(3);
        defaultMenuItem2.setNote("BBQ sauce included");
        defaultMenuItem2.setBusinessId(new ObjectId());

        ArrayList<MenuItem> defaultMenuItems2 = new ArrayList<MenuItem>();
        defaultMenuItems2.add(defaultMenuItem2);

        final Menu defaultMenu2 = new Menu();
        ObjectId menuObjectId2 = new ObjectId();
        defaultMenu2.setName("menu name2");
        defaultMenu2.setDescription("menu description2");
        defaultMenu2.setMenuItems(defaultMenuItems2);
        defaultMenu2.setId(menuObjectId2);
        // Locations
        PostalAddress address2 = new PostalAddress();
        address2.setHouseNumber("832");
        address2.setStreetAddress("Dexter Avenue North");
        address2.setCity("Seattle");
        address2.setState("WA");
        address2.setZipCode("98109");
        address2.setCountry("United States");
        // Build Restaurants
        final Restaurant defaultRestaurant2 = new Restaurant();

        defaultRestaurant2.setName("Eatery");
        defaultRestaurant2.setMenuId(defaultMenu2.getId().toString());
        defaultRestaurant2.setCuisineType("Chinese");
        defaultRestaurant2.setId(new ObjectId());
        defaultRestaurant2.setLocation(address2);
        defaultRestaurant2.setStartTime(LocalTime.of(8, 00, 00));
        defaultRestaurant2.setEndTime(LocalTime.of(22, 00, 00));

        return defaultRestaurant2;
    }

    @Test
    void testRegisterCreatesValidRestaurants() throws Exception {
        restaurants.addRestaurant(createRestaurantOne());
        restaurants.addRestaurant(createRestaurantTwo());
        for (Restaurant restaurant : restaurants.getRestaurants()) {
            Assertions.assertTrue(restaurant.isValid());
        }
    }

    @Test
    void testCanAddRestaurant() throws Exception {
        Restaurant restaurant1 = createRestaurantOne();
        restaurants.addRestaurant(restaurant1);
        System.out.println(restaurants);
        Assertions.assertEquals(
                restaurants.getRestaurant(restaurant1.getId()).getName(), restaurant1.getName());
    }

    @Test
    void testCannotAddRestaurant() throws Exception {
        Assertions.assertThrows(
                Exception.class,
                () -> {
                    Restaurant restaurant = new Restaurant();
                    restaurants.addRestaurant(restaurant);
                });
    }

    @Test
    void testCanUpdateRestaurant() throws Exception {
        Restaurant restaurant1 = createRestaurantOne();
        Restaurant restaurant2 = createRestaurantTwo();
        restaurant2.setId(restaurant1.getId());
        restaurants.addRestaurant(restaurant1);
        restaurants.updateRestaurant(restaurant2);
        Assertions.assertEquals(
                restaurants.getRestaurant(restaurant1.getId()).getName(), restaurant2.getName());
    }

    @Test
    void testCannotUpdateRestaurant() throws Exception {
        Assertions.assertThrows(
                Exception.class,
                () -> {
                    restaurants.updateRestaurant(null);
                });
    }

    @Test
    void testCanDeleteRestaurant() throws Exception {
        Restaurant restaurant1 = createRestaurantOne();
        restaurants.addRestaurant(restaurant1);
        restaurants.deleteRestaurant(restaurant1.getId());
        Assertions.assertTrue(!restaurants.getRestaurants().contains(restaurant1));
    }
}
