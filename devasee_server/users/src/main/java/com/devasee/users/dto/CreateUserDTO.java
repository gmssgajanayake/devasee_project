package com.devasee.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String imageUrl;
}