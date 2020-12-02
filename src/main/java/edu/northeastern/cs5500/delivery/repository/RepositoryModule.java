package edu.northeastern.cs5500.delivery.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.delivery.model.BusinessUser;
import edu.northeastern.cs5500.delivery.model.CustomerUser;
import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DriverUser;
import edu.northeastern.cs5500.delivery.model.Menu;
import edu.northeastern.cs5500.delivery.model.Order;
import edu.northeastern.cs5500.delivery.model.Restaurant;
import edu.northeastern.cs5500.delivery.model.ShoppingCart;
import edu.northeastern.cs5500.delivery.service.MongoDBService;

@Module
public class RepositoryModule {
    // @Provides
    // public GenericRepository<Delivery> provideDeliveryRepository() {
    //     return new InMemoryRepository<>();
    // }

    @Provides
    public GenericRepository<Delivery> providesDeliveryRepository(MongoDBService mongoDBService) {
        return new MongoDBRepository<Delivery>(Delivery.class, mongoDBService);
    }

    // @Provides
    // public GenericRepository<CustomerUser> provideCustomerUserRepository() {
    //     return new InMemoryRepository<>();
    // }

    @Provides
    public GenericRepository<CustomerUser> providesCustomerUserRepository(
            MongoDBService mongoDBService) {
        return new MongoDBRepository<CustomerUser>(CustomerUser.class, mongoDBService);
    }

    // @Provides
    // public GenericRepository<BusinessUser> provideBusinessUserRepository() {
    //     return new InMemoryRepository<>();
    // }

    @Provides
    public GenericRepository<BusinessUser> providesBusinessUserRepository(
            MongoDBService mongoDBService) {
        return new MongoDBRepository<BusinessUser>(BusinessUser.class, mongoDBService);
    }

    // @Provides
    // public GenericRepository<DriverUser> provideDriverUserRepository() {
    //     return new InMemoryRepository<>();
    // }

    @Provides
    public GenericRepository<DriverUser> providesDriverUserRepository(
            MongoDBService mongoDBService) {
        return new MongoDBRepository<DriverUser>(DriverUser.class, mongoDBService);
    }

    // @Provides
    // public GenericRepository<Menu> providesMenuRepository() {
    //     return new InMemoryRepository<>();
    // }
    @Provides
    public GenericRepository<Menu> providesMenuRepository(MongoDBService mongoDBService) {
        return new MongoDBRepository<Menu>(Menu.class, mongoDBService);
    }

    // @Provides
    // public GenericRepository<Order> providesOrderRepository() {
    //     return new InMemoryRepository<>();
    // }

    @Provides
    public GenericRepository<Order> providesOrderRepository(MongoDBService mongoDBService) {
        return new MongoDBRepository<Order>(Order.class, mongoDBService);
    }

    // @Provides
    // public GenericRepository<ShoppingCart> providesShoppingCartRepository() {
    //     return new InMemoryRepository<>();
    // }

    @Provides
    public GenericRepository<ShoppingCart> providesShoppingCartRepository(
            MongoDBService mongoDBService) {
        return new MongoDBRepository<ShoppingCart>(ShoppingCart.class, mongoDBService);
    }

    // @Provides
    // public OrderRepository providesNonGenericOrderRepository() {
    //     return new InMemoryOrderRepository();
    // }

    @Provides
    public OrderRepository providesNonGenericOrderRepository(MongoDBService mongoDBService) {
        return new MongoDBOrderRepository(mongoDBService);
    }

    @Provides
    public GenericRepository<Restaurant> providesRestaurantRepository(
            MongoDBService mongoDBService) {
        return new MongoDBRepository<Restaurant>(Restaurant.class, mongoDBService);
    }
}

/*
 * // Here's an example of how you imght swap out the in-memory repository for a
 * database-backed // repository:
 *
 * @Provides public GenericRepository<Menu> provideMenuRepository() { return new
 * InMemoryRepository<>(); } }
 *
 * /* // Here's an example of how you imght swap out the in-memory repository
 * for a database-backed // repository:
 *
 * package edu.northeastern.cs5500.delivery.repository;
 *
 * import dagger.Module; import dagger.Provides; import
 * edu.northeastern.cs5500.delivery.model.Delivery; import
 * edu.northeastern.cs5500.delivery.service.MongoDBService;
 *
 * @Module public class RepositoryModule {
 *
 * @Provides public GenericRepository<Delivery>
 * provideDeliveryRepository(MongoDBService mongoDBService) { return new
 * MongoDBRepository<>(Delivery.class, mongoDBService); } }
 *
 */
