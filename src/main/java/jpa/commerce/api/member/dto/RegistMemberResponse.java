package jpa.commerce.api.member.dto;

import lombok.Data;

@Data
public class RegistMemberResponse {
    private Long id;
    public RegistMemberResponse(Long id) {
        this.id = id;
    }
}