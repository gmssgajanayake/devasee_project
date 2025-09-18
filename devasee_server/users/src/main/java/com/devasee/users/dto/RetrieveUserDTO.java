package com.devasee.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveUserDTO {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String imageUrl;
    private LocalDateTime createdAt;
}