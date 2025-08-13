package com.devasee.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserDTO {
    private String userId;
    private String email;
    private String username;
}