package com.devasee.users.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    private Long id;
    private String name;
    private String email;
    private boolean active;
    private String adminCode;
}
