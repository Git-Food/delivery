package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.BusinessUser;
import edu.northeastern.cs5500.delivery.model.Name;
import edu.northeastern.cs5500.delivery.model.PostalAddress;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.time.LocalTime;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class BusinessUserController {
    private final GenericRepository<BusinessUser> businessUsers;

    @Inject
    BusinessUserController(GenericRepository<BusinessUser> businessUserRepository) {
        businessUsers = businessUserRepository;

        log.info("BusinessUserController > construct");

        if (businessUsers.count() > 0) {
            return;
        }

        log.info("BusinessUserController > construct > adding default businessUsers");

        final BusinessUser defaultBusinessUser1 = new BusinessUser();
        Name businessUserName1 = new Name();
        businessUserName1.setFirstName("Han");
        businessUserName1.setLastName("Solo");
        PostalAddress addressUser1 = new PostalAddress();
        addressUser1.setHouseNumber("410");
        addressUser1.setStreetAddress("Terry Avenue");
        addressUser1.setCity("Seattle");
        addressUser1.setState("WA");
        addressUser1.setZipCode("98109");
        addressUser1.setCountry("United States");

        defaultBusinessUser1.setName(businessUserName1);
        defaultBusinessUser1.setEmail("han.solo@email.com");
        defaultBusinessUser1.setPhoneNumber("1234567890");
        defaultBusinessUser1.setLocation(addressUser1);
        defaultBusinessUser1.setDescription("Best Food Restaurant");
        defaultBusinessUser1.setOperational(true);
        defaultBusinessUser1.setStartTime(LocalTime.of(8, 00, 00));
        defaultBusinessUser1.setEndTime(LocalTime.of(22, 00, 00));

        final BusinessUser defaultBusinessUser2 = new BusinessUser();
        Name businessUserName2 = new Name();
        businessUserName2.setFirstName("Bob");
        businessUserName2.setLastName("Ross");
        PostalAddress addressUser2 = new PostalAddress();
        addressUser2.setHouseNumber("832");
        addressUser2.setStreetAddress("Dexter Avenue North");
        addressUser2.setCity("Seattle");
        addressUser2.setState("WA");
        addressUser2.setZipCode("98109");
        addressUser2.setCountry("United States");

        defaultBusinessUser2.setName(businessUserName2);
        defaultBusinessUser2.setEmail("bob.ross@email.com");
        defaultBusinessUser2.setPhoneNumber("0987654321");
        defaultBusinessUser2.setLocation(addressUser2);
        defaultBusinessUser2.setDescription("Eatery");
        defaultBusinessUser2.setOperational(true);
        defaultBusinessUser2.setStartTime(LocalTime.of(7, 00, 00));
        defaultBusinessUser2.setEndTime(LocalTime.of(21, 00, 00));

        try {
            addBusinessUser(defaultBusinessUser1);
            addBusinessUser(defaultBusinessUser2);
        } catch (Exception e) {
            log.error(
                    "BusinessUserController > construct > adding default businessUsers > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public BusinessUser getBusinessUser(@Nonnull ObjectId uuid) {
        log.debug("BusinessUserController > getBusinessUser({})", uuid);
        return businessUsers.get(uuid);
    }

    @Nonnull
    public Collection<BusinessUser> getBusinessUsers() {
        log.debug("BusinessUserController > getBusinessUsers()");
        return businessUsers.getAll();
    }

    @Nonnull
    public BusinessUser addBusinessUser(@Nonnull BusinessUser businessUser) throws Exception {
        log.debug("BusinessUserController > addBusinessUser(...)");
        if (!businessUser.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidBusinessUserException");
        }

        ObjectId id = businessUser.getId();

        if (id != null && businessUsers.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return businessUsers.add(businessUser);
    }

    public void updateBusinessUser(@Nonnull BusinessUser businessUser) throws Exception {
        log.debug("BusinessUserController > updateBusinessUser(...)");
        businessUsers.update(businessUser);
    }

    public void deleteBusinessUser(@Nonnull ObjectId id) throws Exception {
        log.debug("BusinessUserController > deleteBusinessUser(...)");
        businessUsers.delete(id);
    }
}
