package edu.northeastern.cs5500.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * Class represents GPSCoordinates of an IUser.
 */

@Data
public class GPSCoordinates {
    private Double latitude;
    private Double longitude;

    /**
     * Returns true if GPSCoordinates are valid, false otherwise.
     * @return true if GPSCoordinates are valid, false otherwise.
     */
    @JsonIgnore
    public boolean isValid() {
        return this.latitude != null && this.longitude != null;
    }

}