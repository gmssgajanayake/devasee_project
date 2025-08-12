package com.devasee.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// For creation
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateCustomerDTO {
    private String name;
    private String email;
    private String password;
    private boolean active;
    private String address;
    private String phoneNumber;
}