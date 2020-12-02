package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalTime;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Restaurant implements Model {

    private ObjectId id;
    private String name;
    private PostalAddress location;
    private LocalTime startTime;
    private LocalTime endTime;
    private String cuisineType;
    private Menu menu;

    @JsonIgnore
    public boolean isValid() {
        return name != null
                && !name.isEmpty()
                && name != null
                && !name.isEmpty()
                && menu.isValid()
                && location.isValid();
    }
}
