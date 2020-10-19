package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Vehicle {
    private MakeModel makeModel;
    private String color;
    private String licensePlate;
    private int year;

    /** @return true if this vehicle is valid */
    @JsonIgnore
    public boolean isValid() {
        return !licensePlate.isEmpty();
    }
}
