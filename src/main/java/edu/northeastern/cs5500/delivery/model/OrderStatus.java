package edu.northeastern.cs5500.delivery.model;

/** Enum class for the status of an order. */
public enum OrderStatus {
    ACCEPTED,
    FOOD_BEING_PREPARED,
    COURIER_ON_WAY,
    FOOD_ARRIVING,
    DELIVERED,
    REJECTED_BY_RESTAURANT,
    NO_COURIER_AVAILABLE,
    REFUNDED,
    CANCELLED
}
