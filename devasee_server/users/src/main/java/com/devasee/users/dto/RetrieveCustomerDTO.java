package com.devasee.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class RetrieveCustomerDTO {
    private Long id;
    private String name;
    private String email;
    private boolean active;
    private String address;
    private String phoneNumber;
}