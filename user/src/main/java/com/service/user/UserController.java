package com.service.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.user.dtos.UserCreateRequestDTO;
import com.service.user.dtos.UserDTO;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin("http://localhost:3000")
public class UserController {
    
    private final UserService userService;
    // private final TokenService tokenService;
    // private final EmailService emailService;

    public UserController(UserService userService/*, TokenService tokenService, EmailService emailService*/) {
        this.userService = userService;
        // this.tokenService = tokenService;
        // this.emailService = emailService;
    }

    /**
     * Endpoint returns list of all registrated users
     * @return List<UserDTO>
     */
    @GetMapping
	public ResponseEntity<List<UserDTO>> getUsers() {

        List<UserDTO> userDTOList = userService.getUsers();

        if (userDTOList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userDTOList);
        }
    }

    @GetMapping("/{uuid}")
	public ResponseEntity<UserDTO> getUser(@PathVariable String uuid) {
        try {
            UserDTO userDTO = userService.getUser(uuid);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userDTO);
        } catch (NoSuchElementException exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserCreateRequestDTO userCreateRequestDTO) {

        try {
            userService.addUser(userCreateRequestDTO);
            // Token token = tokenService.addToken(userCreateRequestDTO);
            // emailService.send(userCreateRequestDTO.email(), token);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("{message: \"success\"}");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(exception.getMessage());
        }

    }
}
