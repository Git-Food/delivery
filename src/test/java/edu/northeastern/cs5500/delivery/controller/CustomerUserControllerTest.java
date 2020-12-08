package edu.northeastern.cs5500.delivery.controller;

import edu.northeastern.cs5500.delivery.model.CustomerUser;
import edu.northeastern.cs5500.delivery.model.Name;
import edu.northeastern.cs5500.delivery.model.PostalAddress;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import java.time.LocalDate;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerUserControllerTest {
    CustomerUserController customerUsers;

    @BeforeEach
    void init() {
        customerUsers = new CustomerUserController(new InMemoryRepository<CustomerUser>());
    }

    CustomerUser createTestCustomerUserOne() {
        CustomerUser testCustomerUser1 = new CustomerUser();
        ObjectId testUser1Id = new ObjectId();
        testCustomerUser1.setId(testUser1Id);
        Name customerUserName1 = new Name();
        customerUserName1.setFirstName("testPersonFirst");
        customerUserName1.setLastName("testPersonLast");
        PostalAddress addressUser1 = new PostalAddress();
        addressUser1.setHouseNumber("000");
        addressUser1.setStreetAddress("XYZ Avenue");
        addressUser1.setCity("City");
        addressUser1.setState("State");
        addressUser1.setZipCode("01234");
        addressUser1.setCountry("United States");

        testCustomerUser1.setName(customerUserName1);
        testCustomerUser1.setEmail("First.Last@email.com");
        testCustomerUser1.setPhoneNumber("1234567890");
        testCustomerUser1.setLocation(addressUser1);
        testCustomerUser1.setBirthday(LocalDate.of(1900, 1, 1));

        return testCustomerUser1;
    }

    CustomerUser createTestCustomerUserTwo() {
        CustomerUser testCustomerUser2 = new CustomerUser();
        ObjectId testUser2Id = new ObjectId();
        testCustomerUser2.setId(testUser2Id);
        Name customerUserName2 = new Name();
        customerUserName2.setFirstName("A_First");
        customerUserName2.setLastName("Z_Last");
        PostalAddress addressUser2 = new PostalAddress();
        addressUser2.setHouseNumber("9999");
        addressUser2.setStreetAddress("ZYX Boulevard");
        addressUser2.setCity("Some City");
        addressUser2.setState("Some State");
        addressUser2.setZipCode("99999");
        addressUser2.setCountry("United States");

        testCustomerUser2.setName(customerUserName2);
        testCustomerUser2.setEmail("Last.First@email.com");
        testCustomerUser2.setPhoneNumber("9876543210");
        testCustomerUser2.setLocation(addressUser2);
        testCustomerUser2.setBirthday(LocalDate.of(2000, 12, 31));

        return testCustomerUser2;
    }

    @Test
    void testGetCustomerUser() throws Exception {
        // Create testUsers
        CustomerUser testUser1 = createTestCustomerUserOne();
        CustomerUser testUser2 = createTestCustomerUserTwo();

        // Obtain User ObjectIds
        ObjectId testUserId1 = testUser1.getId();
        ObjectId testUserId2 = testUser2.getId();

        // Add test customerUsers to repository
        customerUsers.addCustomerUser(testUser1);
        customerUsers.addCustomerUser(testUser2);

        Assertions.assertEquals(testUser1, customerUsers.getCustomerUser(testUserId1));
        Assertions.assertEquals(testUser2, customerUsers.getCustomerUser(testUserId2));
    }

    @Test
    void testGetCustomerUsers() {
        // TODO: modify test if we remove dummy data in CustomerUser class
        // Is not empty right away because of dummy data inserted
        // directly inside the CustomerUser class.
        Assertions.assertTrue(customerUsers.getCustomerUsers().size() != 0);
    }

    @Test
    void testRegisterCreatesValidCustomerUsers() throws Exception {
        customerUsers.addCustomerUser(createTestCustomerUserOne());
        customerUsers.addCustomerUser(createTestCustomerUserTwo());
        for (CustomerUser user : customerUsers.getCustomerUsers()) {
            Assertions.assertTrue(user.isValid());
        }
    }

    @Test
    void testCanAddCustomerUser() throws Exception {
        CustomerUser testUser1 = createTestCustomerUserOne();
        customerUsers.addCustomerUser(testUser1);
        Assertions.assertEquals(testUser1, customerUsers.getCustomerUser(testUser1.getId()));
    }

    // Invalid Customer User Exception
    @Test
    void testCannotAddCustomerUser() throws Exception {
        Assertions.assertThrows(
                Exception.class,
                () -> {
                    CustomerUser user1 = new CustomerUser();
                    // System.out.println(user1);
                    // System.out.println(user1.isValid());
                    customerUsers.addCustomerUser(user1);
                });
    }

    @Test
    void testCanUpdateCustomerUser() throws Exception {
        CustomerUser testUser1 = createTestCustomerUserOne();
        customerUsers.addCustomerUser(testUser1);
        String newPhone = "8881112222";
        testUser1.setPhoneNumber(newPhone);
        customerUsers.updateCustomerUser(testUser1);
        Assertions.assertEquals(
                newPhone, customerUsers.getCustomerUser(testUser1.getId()).getPhoneNumber());
    }

    @Test
    void testCannotUpdateCustomerUser() throws Exception {
        Assertions.assertThrows(
                Exception.class,
                () -> {
                    customerUsers.updateCustomerUser(null);
                });
    }

    @Test
    void testCanDeleteCustomerUser() throws Exception {
        CustomerUser testUser1 = createTestCustomerUserOne();
        customerUsers.addCustomerUser(testUser1);
        customerUsers.deleteCustomerUser(testUser1.getId());
        Assertions.assertTrue(!customerUsers.getCustomerUsers().contains(testUser1));
    }
}
