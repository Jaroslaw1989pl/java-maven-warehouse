package com.service.user.dtos;

public record UserCreateRequestDTO(
    String email, 
    String pass, 
    String pass2
) {

}
