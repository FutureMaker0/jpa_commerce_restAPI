package jpa.commerce.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// api/v2/members의 반환 자료형
// [] 타입 응답에서 {} 타입의 json object(객체) 형태로 응답해주기 위해 껍데기를 한 번 씌워주었다. 향후 확장성을 고려하여 꼭 필요한 작업이다.
@Data
@AllArgsConstructor
public class CustomFormat<T> {
    private T data;
}
