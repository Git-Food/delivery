package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import javax.inject.Inject;

/** Class represents a vehicle used by a driver. */
public class Vehicle {
    private String color;
    private String licensePlate;
    private Integer year;
    @Inject protected MakeModel makeModel;

    public Vehicle(String color, String licensePlate, Integer year, MakeModel makeModel) {
        this.color = color;
        this.licensePlate = licensePlate;
        this.year = year;
        this.makeModel = makeModel;
    }

    /** @return true if this Vehicle is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.color != null
                && this.licensePlate != null
                && this.year != null
                && this.makeModel.isValid();
    }

    public String getColor() {
        return this.color;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public Integer getYear() {
        return this.year;
    }

    public MakeModel getMakeModel() {
        return this.makeModel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        Vehicle that = (Vehicle) obj;
        if (this.licensePlate.equals(that.licensePlate)
                && this.year.equals(that.year)
                && this.color.equals(that.color)
                && this.makeModel.equals(that.makeModel)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.color, this.year, this.licensePlate, this.makeModel);
    }

    @Override
    public String toString() {
        return "Vehicle = { Color: "
                + this.color
                + ", licensePlate: "
                + this.licensePlate
                + ", Year: "
                + this.year
                + ", MakeModel: "
                + this.makeModel.toString()
                + " }";
    }
}
