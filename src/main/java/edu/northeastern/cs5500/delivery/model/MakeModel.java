package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

/** Class represents the MakeModel of a Vehicle. */
@Data
public class MakeModel implements Model {
    private ObjectId id;
    private String make;
    private String model;

    /** @return true if this MakeModel is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.make != null && this.model != null;
    }
}
