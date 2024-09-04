package com.service.user.mappers;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.service.user.User;
import com.service.user.dtos.UserDTO;

@Service
public class UserToUserDTO implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getUuid(),
                user.getCreatedAt(),
                user.getEmail(),
                user.getEmailUpdatedAt(),
                user.getPasswordUpdatedAt(),
                user.getRole(),
                user.getIsActive()
            );
    }
}