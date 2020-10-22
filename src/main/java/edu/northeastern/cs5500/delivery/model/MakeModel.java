package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

/** Class represents the MakeModel of a Vehicle. */
@Builder
@Data
public class MakeModel {
    private String make;
    private String model;

    /** @return true if this MakeModel is valid */
    @JsonIgnore
    public boolean isValid() {
        return this.make != null
                && !this.make.isEmpty()
                && this.model != null
                && !this.model.isEmpty();
    }
}
