package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Name {
    private String firstName;
    private String lastName;

    /** @return true if this make name is valid */
    @JsonIgnore
    public boolean isValid() {
        return !firstName.isEmpty() && !lastName.isEmpty();
    }
}
