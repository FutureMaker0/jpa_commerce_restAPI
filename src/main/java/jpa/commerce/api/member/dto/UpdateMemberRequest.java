package jpa.commerce.api.member.dto;

import lombok.Data;

@Data
public class UpdateMemberRequest {
    private String name;
    private String country;
    private String city;
    private String zipcode;
}
