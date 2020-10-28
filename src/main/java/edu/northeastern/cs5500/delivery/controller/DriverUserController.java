package edu.northeastern.cs5500.delivery.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.delivery.model.DriverUser;
import edu.northeastern.cs5500.delivery.model.GPSCoordinates;
import edu.northeastern.cs5500.delivery.model.MakeModel;
import edu.northeastern.cs5500.delivery.model.Name;
import edu.northeastern.cs5500.delivery.model.PostalAddress;
import edu.northeastern.cs5500.delivery.model.Vehicle;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class DriverUserController {
    private final GenericRepository<DriverUser> drivers;

    @Inject
    DriverUserController(GenericRepository<DriverUser> driversRepository) {
        drivers = driversRepository;

        log.info("DriversController > construct");

        if (drivers.count() > 0) {
            return;
        }

        log.info("DriversController > construct > adding default drivers");
        // Names
        Name driverUserName1 = Name.builder().firstName("Will").lastName("Douglas").build();
        Name driverUserName2 = Name.builder().firstName("Michael").lastName("Walton").build();
        // Addresses
        PostalAddress driverUserAddress1 =
                PostalAddress.builder()
                        .streetAddress("Westlake Ave")
                        .houseNumber("123")
                        .city("Seattle")
                        .state("WA")
                        .zipCode("98103")
                        .country("US")
                        .build();
        PostalAddress driverUserAddress2 =
                PostalAddress.builder()
                        .streetAddress("Thomas Ave")
                        .houseNumber("456")
                        .city("Seattle")
                        .state("WA")
                        .zipCode("98107")
                        .country("US")
                        .build();
        // Vehicles
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setColor("red");
        vehicle1.setLicensePlate("WAAAA123");
        vehicle1.setYear(2020);
        MakeModel makemodel1 = MakeModel.builder().make("Toyota").model("Rav4").build();
        vehicle1.setMakeModel(makemodel1);
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setColor("blue");
        vehicle2.setLicensePlate("WBBA456");
        vehicle2.setYear(2018);
        MakeModel makemodel2 = MakeModel.builder().make("Honda").model("CRV").build();
        vehicle2.setMakeModel(makemodel2);
        // Coordinates
        GPSCoordinates coordinate1 = new GPSCoordinates();
        coordinate1.setLatitude(47.64938723);
        coordinate1.setLongitude(-122.385973723);
        GPSCoordinates coordinate2 = new GPSCoordinates();
        coordinate2.setLatitude(47.67511789);
        coordinate2.setLongitude(-122.323399063);
        // Drivers
        final DriverUser defaultDriver1 = new DriverUser();
        defaultDriver1.setName(driverUserName1);
        defaultDriver1.setPhoneNumber("206123456");
        defaultDriver1.setEmail("Driver1@gmail.com");
        defaultDriver1.setLocation(driverUserAddress1);
        defaultDriver1.setVehicle(vehicle1);
        defaultDriver1.setCoordinates(coordinate1);
        final DriverUser defaultDriver2 = new DriverUser();
        defaultDriver2.setName(driverUserName2);
        defaultDriver2.setPhoneNumber("206123789");
        defaultDriver2.setEmail("Driver2@gmail.com");
        defaultDriver2.setLocation(driverUserAddress2);
        defaultDriver2.setVehicle(vehicle2);
        defaultDriver2.setCoordinates(coordinate2);

        try {
            addDriver(defaultDriver1);
            addDriver(defaultDriver2);
        } catch (Exception e) {
            log.error("DriverUserController > construct > adding default drivers > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public DriverUser getDriver(@Nonnull ObjectId uuid) {
        log.debug("DriverUserController > getDriver({})", uuid);
        return drivers.get(uuid);
    }

    @Nonnull
    public Collection<DriverUser> getDrivers() {
        log.debug("DriverUserController > getDriver()");
        return drivers.getAll();
    }

    @Nonnull
    public DriverUser addDriver(@Nonnull DriverUser driver) throws Exception {
        log.debug("DriverUserController > addDriver(...)");
        if (!driver.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidDriverException");
        }

        ObjectId id = driver.getId();

        if (id != null && drivers.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return drivers.add(driver);
    }

    public void updateDriver(@Nonnull DriverUser driver) throws Exception {
        log.debug("DriverUserController > updateDriver(...)");
        drivers.update(driver);
    }

    public void deleteDriver(@Nonnull ObjectId id) throws Exception {
        log.debug("DriverUserController > deleteDriver(...)");
        drivers.delete(id);
    }
}
