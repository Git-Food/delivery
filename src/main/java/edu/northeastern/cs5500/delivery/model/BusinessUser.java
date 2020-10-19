package edu.northeastern.cs5500.delivery.model;

import java.time.LocalTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessUser extends AbstractUser {
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean operational;
}
