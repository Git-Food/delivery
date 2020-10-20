package edu.northeastern.cs5500.delivery.model;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class represents a postal address.
 */


@Data
public class PostalAddress {
    private String streetAddress;
    private String houseNumber;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    /* TODO: Implement methods to calculate GPS coordinates */

    /**
     * Returns true if PostalAddress is valid, false otherwise
     * @return true if PostalAdress is valid, false otherwise
     */
    @JsonIgnore
    public boolean isValid() {
        return this.streetAddress != null
                && !this.streetAddress.isEmpty()
                && this.houseNumber !=null
                && !this.houseNumber.isEmpty()
                && this.city !=null
                && !this.city.isEmpty()
                && this.state !=null
                && !this.state.isEmpty()
                && this.zipCode != null
                && !this.zipCode.isEmpty()
                && this.country != null
                && !this.country.isEmpty();
    }


}