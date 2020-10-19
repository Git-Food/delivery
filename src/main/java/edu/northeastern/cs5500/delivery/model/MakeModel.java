package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class MakeModel {
    private String make;
    private String model;

    /** @return true if this make model is valid */
    @JsonIgnore
    public boolean isValid() {
        return !make.isEmpty() && !model.isEmpty();
    }
}
