package edu.northeastern.cs5500.delivery.view;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class ViewModule {
    @Provides
    @IntoSet
    public View provideDeliveryView(DeliveryView deliveryView) {
        return deliveryView;
    }

    @Provides
    @IntoSet
    public View provideStatusView(StatusView statusView) {
        return statusView;
    }

    @Provides
    @IntoSet
    public View providesMenuView(MenuView menuView) {
        return menuView;
    }

    @Provides
    @IntoSet
    public View provideCustomerUserView(CustomerUserView customerUserView) {
        return customerUserView;
    }

    @Provides
    @IntoSet
    public View provideBusinessUserView(BusinessUserView businessUserView) {
        return businessUserView;
    }

    @Provides
    @IntoSet
    public View provideDriverUserView(DriverUserView driverUserView) {
        return driverUserView;
    }

    @Provides
    @IntoSet
    public View provideOrderView(OrderView orderView) {
        return orderView;
    }

    @Provides
    @IntoSet
    public View providesShoppingCartView(ShoppingCartView shoppingCartView) {
        return shoppingCartView;
    }

    @Provides
    @IntoSet
    public View providesRestaurantView(RestaurantView restaurantView) {
        return restaurantView;
    }
}
