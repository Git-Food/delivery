package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class MenuItem {
    private ObjectId id;
    private String name;
    private String description;
    private long price;
    private String note;
    private ObjectId businessId;

    /** @return true if this MenuItem is valid */
    @JsonIgnore
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
