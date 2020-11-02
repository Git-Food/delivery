package edu.northeastern.cs5500.delivery.model;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {
    @Provides
    public Class<Delivery> provideDeliveryClass() {
        return Delivery.class;
    }

    @Provides
    public Class<OrderItem> provideOrderItemClass() {
        return OrderItem.class;
    }

    @Provides
    public Class<Order> provideOrderClass() {
        return Order.class;
    }

    @Provides
    public Class<MakeModel> provideMakeModel() {
        return MakeModel.class;
    }

    @Provides
    public Class<Vehicle> provideVehicle() {
        return Vehicle.class;
    }

    @Provides
    public Class<PostalAddress> providePostalAddress() {
        return PostalAddress.class;
    }

    @Provides
    public Class<GPSCoordinates> provideGPSCoordinates() {
        return GPSCoordinates.class;
    }

    @Provides
    public Class<Name> provideName() {
        return Name.class;
    }

    @Provides
    public Class<DriverUser> provideDriverUser() {
        return DriverUser.class;
    }

    @Provides
    public Class<CustomerUser> provideCustomerUser() {
        return CustomerUser.class;
    }

    @Provides
    public Class<BusinessUser> provideBusinessUser() {
        return BusinessUser.class;
    }

    @Provides
    public Class<Menu> providesMenu() {
        return Menu.class;
    }

    @Provides
    public Class<MenuItem> providesMenuItem() {
        return MenuItem.class;
    }

    @Provides
    public Class<ShoppingCart> providesShoppingCart() {
        return ShoppingCart.class;
    }
}
