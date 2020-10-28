package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.CustomerUser;
import edu.northeastern.cs5500.delivery.model.Name;
import edu.northeastern.cs5500.delivery.model.PostalAddress;
import edu.northeastern.cs5500.delivery.repository.GenericRepository;
import java.sql.Date;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class CustomerUserController {
    private final GenericRepository<CustomerUser> customerUsers;

    @Inject
    CustomerUserController(GenericRepository<CustomerUser> customerUserRepository) {
        customerUsers = customerUserRepository;

        log.info("CustomerUserController > construct");

        if (customerUsers.count() > 0) {
            return;
        }

        log.info("CustomerUserController > construct > adding default customerUsers");

        final CustomerUser defaultCustomerUser1 = new CustomerUser();
        Name customerUserName1 = Name.builder().firstName("John").lastName("Smith").build();
        PostalAddress addressUser1 =
                PostalAddress.builder()
                        .houseNumber("225")
                        .streetAddress("Terry Avenue")
                        .city("Seattle")
                        .state("WA")
                        .zipCode("98109")
                        .country("United States")
                        .build();

        defaultCustomerUser1.setName(customerUserName1);
        defaultCustomerUser1.setEmail("john.smith@email.com");
        defaultCustomerUser1.setPhoneNumber("1234567890");
        defaultCustomerUser1.setLocation(addressUser1);
        defaultCustomerUser1.setBirthday(Date.valueOf("1965-12-12"));

        final CustomerUser defaultCustomerUser2 = new CustomerUser();
        Name customerUserName2 = Name.builder().firstName("Mary").lastName("Poppins").build();
        PostalAddress addressUser2 =
                PostalAddress.builder()
                        .houseNumber("401")
                        .streetAddress("Terry Avenue")
                        .city("Seattle")
                        .state("WA")
                        .zipCode("98109")
                        .country("United States")
                        .build();

        defaultCustomerUser2.setName(customerUserName2);
        defaultCustomerUser2.setEmail("mary.poppins@email.com");
        defaultCustomerUser2.setPhoneNumber("0987654321");
        defaultCustomerUser2.setLocation(addressUser2);
        defaultCustomerUser2.setBirthday(Date.valueOf("1900-01-28"));

        try {
            addCustomerUser(defaultCustomerUser1);
            addCustomerUser(defaultCustomerUser2);
        } catch (Exception e) {
            log.error(
                    "CustomerUserController > construct > adding default customerUsers > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public CustomerUser getCustomerUser(@Nonnull ObjectId uuid) {
        log.debug("CustomerUserController > getCustomerUser({})", uuid);
        return customerUsers.get(uuid);
    }

    @Nonnull
    public Collection<CustomerUser> getCustomerUsers() {
        log.debug("CustomerUserController > getCustomerUsers()");
        return customerUsers.getAll();
    }

    @Nonnull
    public CustomerUser addCustomerUser(@Nonnull CustomerUser customeruser) throws Exception {
        log.debug("CustomerUserController > addCustomerUser(...)");
        if (!customeruser.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidCustomerUserException");
        }

        ObjectId id = customeruser.getId();

        if (id != null && customerUsers.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return customerUsers.add(customeruser);
    }

    public void updateCustomerUser(@Nonnull CustomerUser customeruser) throws Exception {
        log.debug("CustomerUserController > updateCustomerUser(...)");
        customerUsers.update(customeruser);
    }

    public void deleteCustomerUser(@Nonnull ObjectId id) throws Exception {
        log.debug("CustomerUserController > deleteCustomerUser(...)");
        customerUsers.delete(id);
    }
}
