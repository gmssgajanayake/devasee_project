package com.devasee.users.dto;

import com.devasee.users.entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private Set<Role> roles;
    private LocalDateTime updatedAt;
}
