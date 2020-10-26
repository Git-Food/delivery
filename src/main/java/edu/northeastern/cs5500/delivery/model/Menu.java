package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Menu {
    private ObjectId objectId;
    private String name;
    private String description;
    private List<MenuItem> menuItems;

    /** @return true if this Menu is valid */
    @JsonIgnore
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
