package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

/** Class represents a vehicle used by a driver. */
@Data
public class Vehicle implements Model {
    private ObjectId id;
    private String color;
    private String licensePlate;
    private Integer year;
    private MakeModel makeModel;

    /** @return true if this Vehicle is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.color != null
                && this.licensePlate != null
                && this.year != null
                && this.makeModel.isValid();
    }
}
