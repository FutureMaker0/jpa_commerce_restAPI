package jpa.commerce.api.member.dto;

import lombok.Data;

@Data
// @AllArgsConstructor 생성자 대신 사용 가능
public class UpdateMemberResponse {
    private Long id;
    private String name;

    public UpdateMemberResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
