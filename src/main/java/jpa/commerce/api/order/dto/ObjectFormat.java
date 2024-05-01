package jpa.commerce.api.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ObjectFormat<T> {
    private T data;
}
