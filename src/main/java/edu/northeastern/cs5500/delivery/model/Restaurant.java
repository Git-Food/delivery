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
    private String menuId;

    @JsonIgnore
    public boolean isValid() {
        return name != null && !name.isEmpty() && location.isValid();
    }
}
