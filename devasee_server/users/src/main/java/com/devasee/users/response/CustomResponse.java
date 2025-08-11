package com.devasee.users.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
