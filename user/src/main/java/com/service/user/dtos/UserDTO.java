package com.service.user.dtos;

import java.time.LocalDateTime;

import com.service.user.enums.UserRole;

public record UserDTO(
    String uuid,
    LocalDateTime createdAt,
    String email,
    LocalDateTime emailUpdatedAt,
    LocalDateTime passwordUpdatedAt,
    UserRole role,
    Boolean isActive
) {

}
