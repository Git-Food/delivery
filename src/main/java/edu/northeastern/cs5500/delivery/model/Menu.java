package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Menu implements Model {
    private ObjectId id;
    private String name;
    private String description;
    private Map<String, MenuItem> menuItems;

    /** @return true if this Menu is valid */
    @JsonIgnore
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
