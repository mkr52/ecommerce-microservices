package com.mkr.ecom.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AddressDTO {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
