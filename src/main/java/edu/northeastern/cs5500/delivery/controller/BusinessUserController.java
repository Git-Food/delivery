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
        Name businessUserName1 = Name.builder().firstName("Han").lastName("Solo").build();
        PostalAddress addressUser1 =
                PostalAddress.builder()
                        .houseNumber("410")
                        .streetAddress("Terry Avenue")
                        .city("Seattle")
                        .state("WA")
                        .zipCode("98109")
                        .country("United States")
                        .build();

        defaultBusinessUser1.setName(businessUserName1);
        defaultBusinessUser1.setEmail("han.solo@email.com");
        defaultBusinessUser1.setPhoneNumber("1234567890");
        defaultBusinessUser1.setLocation(addressUser1);
        defaultBusinessUser1.setDescription("Best Food Restaurant");
        defaultBusinessUser1.setOperational(true);
        defaultBusinessUser1.setStartTime(LocalTime.of(8, 00, 00));
        defaultBusinessUser1.setEndTime(LocalTime.of(22, 00, 00));

        final BusinessUser defaultBusinessUser2 = new BusinessUser();
        Name businessUserName2 = Name.builder().firstName("Bob").lastName("Ross").build();
        PostalAddress addressUser2 =
                PostalAddress.builder()
                        .houseNumber("832")
                        .streetAddress("Dexter Avenue North")
                        .city("Seattle")
                        .state("WA")
                        .zipCode("98109")
                        .country("United States")
                        .build();

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
