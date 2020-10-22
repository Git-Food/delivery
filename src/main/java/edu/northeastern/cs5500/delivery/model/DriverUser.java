package edu.northeastern.cs5500.delivery.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DriverUser extends AbstractUser {
    private Vehicle vehicle;
}
