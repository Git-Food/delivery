/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.northeastern.cs5500.delivery.controller;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import edu.northeastern.cs5500.delivery.model.Delivery;
import edu.northeastern.cs5500.delivery.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryControllerTest {
    DeliveryController deliveryController;
    Delivery delivery;
    ObjectId id1;

    @BeforeEach
    void init() {
        deliveryController = new DeliveryController(new InMemoryRepository<Delivery>());
        id1 = new ObjectId();
        delivery = new Delivery();
        delivery.setDescription("New delivery for testing");
        delivery.setId(id1);
        delivery.setTitle("Title for new delivery test");
    }

    @Test
    void testRegisterCreatesDeliverys() {
        assertThat(deliveryController.getDeliverys()).isNotEmpty();
    }

    @Test
    void testRegisterCreatesValidDeliverys() {
        for (Delivery delivery : deliveryController.getDeliverys()) {
            assertWithMessage(delivery.getTitle()).that(delivery.isValid()).isTrue();
        }
    }

    @Test
    void testCanAddDelivery() throws Exception {
        // Add the delivery
        deliveryController.addDelivery(delivery);
        // Assert it is there
        assertThat(deliveryController.getDelivery(id1)).isEqualTo(delivery);
    }

    @Test
    void testCanReplaceDelivery() throws Exception {
        // Add the delivery
        deliveryController.addDelivery(delivery);
        // Assert Title
        assertThat(delivery.getTitle()).isEqualTo("Title for new delivery test");
        // Update delivery
        delivery.setTitle("Title changes for test");
        // Call Update
        deliveryController.updateDelivery(delivery);
        // Assert new Title
        assertThat(deliveryController.getDelivery(id1).getTitle())
                .isEqualTo("Title changes for test");
    }

    @Test
    void testCanDeleteDelivery() throws Exception {
        // Add the delivery
        deliveryController.addDelivery(delivery);
        // Assert delivery is in there
        assertThat(deliveryController.getDelivery(id1)).isEqualTo(delivery);
        // Delete
        deliveryController.deleteDelivery(id1);
        // Assert is not there anymore
        assertThat(deliveryController.getDeliverys()).doesNotContain(id1);
    }
}
