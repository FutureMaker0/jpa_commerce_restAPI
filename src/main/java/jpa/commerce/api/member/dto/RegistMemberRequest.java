package jpa.commerce.api.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistMemberRequest {
    @NotEmpty
    private String name;
}
