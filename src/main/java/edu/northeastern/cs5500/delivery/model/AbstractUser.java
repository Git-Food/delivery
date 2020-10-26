package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public abstract class AbstractUser implements Model {
    protected ObjectId id;
    protected Name name;
    protected String phoneNumber;
    // TODO: add IPayment interface
    protected String email;
    protected PostalAddress location;

    /** @return true if this AbstractUser is valid */
    @JsonIgnore
    public boolean isValid() {
        return name.isValid()
                && !email.isEmpty()
                && email != null
                && phoneNumber != null
                && !phoneNumber.isEmpty()
                && location.isValid();
    }
}
