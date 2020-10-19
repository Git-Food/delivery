package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public abstract class AbstractUser implements Model {
    protected ObjectId id;
    protected String name; // TODO: add Name class
    protected String phoneNumber;
    // TODO: add IPayment interface
    protected String email;
    // TODO: add Location class

    /** @return true if this abstract user is valid */
    @JsonIgnore
    public boolean isValid() {
        return !name.isEmpty() && !email.isEmpty();
    }
}
