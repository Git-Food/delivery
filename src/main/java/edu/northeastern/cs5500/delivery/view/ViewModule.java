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
<<<<<<< HEAD
    public View providesMenuView(MenuView menuView) {
        return menuView;
=======
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
    public View provideDriverView(DriverView driverView) {
        return driverView;
>>>>>>> main
    }
}
