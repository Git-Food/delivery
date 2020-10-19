package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

/** Class represents the MakeModel of a Vehicle. */
public class MakeModel {
    private String make;
    private String model;

    public MakeModel(String make, String model) {
        this.make = make;
        this.model = model;
    }

    public String getModel() {
        return this.model;
    }

    public String getMake() {
        return this.make;
    }

    /** @return true if this MakeModel is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.make != null && this.model != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        MakeModel that = (MakeModel) obj;
        if (this.make.equals(that.make) && this.model.equals(that.model)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.make, this.model);
    }

    @Override
    public String toString() {
        return "MakeModel = Make :" + this.make + " Model: " + this.model;
    }
}
