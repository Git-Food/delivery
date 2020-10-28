package edu.northeastern.cs5500.delivery.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.delivery.model.BusinessUser;
import edu.northeastern.cs5500.delivery.model.CustomerUser;
import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.model.DriverUser;
import edu.northeastern.cs5500.delivery.model.Menu;
import edu.northeastern.cs5500.delivery.model.Order;

@Module
public class RepositoryModule {
    @Provides
    public GenericRepository<Delivery> provideDeliveryRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<CustomerUser> provideCustomerUserRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<BusinessUser> provideBusinessUserRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<DriverUser> provideDriverUserRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<Menu> providesMenuRepository() {
        return new InMemoryRepository<>();
    }

    @Provides
    public GenericRepository<Order> providesOrderRepository() {
        return new InMemoryRepository<>();
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
