package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Menu implements Model {
    private ObjectId id;
    private String name;
    private String description;
    private ArrayList<MenuItem> menuItems;

    /** @return true if this Menu is valid */
    @JsonIgnore
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
