package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class AbstractUser implements Model {
    private ObjectId id;
    private String name; // TODO: should name be a class? but
    private String phoneNumber;
    // TODO: add IPayment interface
    private String email;
    // TODO: add Location class

    /** @return true if this AbstractUser is valid */
    @JsonIgnore
    public boolean isValid() {
        return name != null && email != null;
    }
}
