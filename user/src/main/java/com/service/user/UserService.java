package com.service.user;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.service.user.dtos.UserCreateRequestDTO;
import com.service.user.dtos.UserDTO;
import com.service.user.enums.UserRole;
import com.service.user.mappers.UserToUserDTO;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserToUserDTO userToUserDTO;
    // private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserToUserDTO userToUserDTO/*, PasswordEncoder passwordEncoder*/) {
        this.userRepository = userRepository;
        this.userToUserDTO = userToUserDTO;
        // this.passwordEncoder = passwordEncoder;
    }

	public List<UserDTO> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userToUserDTO)
                .collect(Collectors.toList());
	}

    public UserDTO getUser(String uuid) throws NoSuchElementException {
        return userRepository
                .findById(uuid)
                .stream()
                .map(userToUserDTO)
                .findFirst()
                .orElseThrow();
	}

    public void addUser(UserCreateRequestDTO createRequestDTO) throws IllegalArgumentException {

        // JSONObject jo = new JSONObject();

        // // 1. Email validation
        // if (userCreateRequestDto.email().isBlank()) {
        //     jo.put("email", "Email address can't be empty.");
        // } else if (!userCreateRequestDto.email().matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")) {
        //     jo.put("email", "Invalid email address.");
        // } else if (userRepository.findUserByEmail(userCreateRequestDto.email()).isPresent()) {
        //     jo.put("email", "Email address already taken.");
        // }

        // // 2. Password validation
        // if (userCreateRequestDto.pass().isBlank()) {
        //     jo.put("pass", "Password can't be empty.");
        // } else if (userCreateRequestDto.pass().length() < 8) {
        //     jo.put("pass", "Password must be at least 8 characters long.");
        // } else if (!Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9_])").matcher(userCreateRequestDto.pass()).find()) {
        //     jo.put("pass", "Password doesn't met requirements.");
        // }

        // // 3. Password comparison
        // if (userCreateRequestDto.pass2().isBlank()) {
        //     jo.put("pass2", "Password can't be empty.");
        // } else if (userCreateRequestDto.pass2().equals(userCreateRequestDto.pass()) == false) {
        //     jo.put("pass2", "Passwords must be te same.");
        // }
        
        // if (jo.isEmpty() == false) throw new IllegalArgumentException(jo.toString());

        // // BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // // String encodedPassword = bCryptPasswordEncoder.encode(userCreateRequestDto.pass());
        // String encodedPassword = passwordEncoder.encode(userCreateRequestDto.pass());

        // User user = new User(createRequestDTO.email(), encodedPassword, UserRole.USER);
        // SecureRandom
        User user = new User(createRequestDTO.email(), createRequestDTO.pass(), UserRole.USER);

        userRepository.save(user);
    }

    // public User verifyUser(UserLoginAuthenticationDto userLoginAuthenticationDto) 
    //     throws IllegalArgumentException, EntityNotFoundException, UnauthorizedCustomException, AccountLockedException {
        
    //     JSONObject jo = new JSONObject();

    //     // 1. Email validation
    //     if (userLoginAuthenticationDto.email().isBlank()) {
    //         jo.put("email", "Email address can't be empty.");
    //     }

    //     // 2. Password validation
    //     if (userLoginAuthenticationDto.pass().isBlank()) {
    //         jo.put("pass", "Password can't be empty.");
    //     }

    //     // 3. If data is empty or incorrect then throw error (422 Unprocessable Content)
    //     if (jo.isEmpty() == false) throw new IllegalArgumentException(jo.toString());

    //     // 4. Find User in the database by email address
    //     Optional<User> user = userRepository.findUserByEmail(userLoginAuthenticationDto.email());
    //     // 5. If record doesn't exists throw error (404 Not Found)
    //     if (user.isEmpty()) throw new EntityNotFoundException(jo.put("login", "There is no such user.").toString());
        
    //     // 6. Password verification - if passwords doesn't match then throw error
    //     if (!passwordEncoder.matches(userLoginAuthenticationDto.pass(), user.get().getPassword())) {
    //         throw new UnauthorizedCustomException(jo.put("login", "Incorrect password").toString());
    //     }
    //     // 7. Verification whether the user account is active
    //     if (user.get().getIsActive() == false) {
    //         String message = "Your account has not been activated yet. "
    //                        + "An activation link has been sent to your email address, click on it and complete the registration. "
    //                        + "The activation link is active for 15 minutes.";

    //         throw new AccountLockedException(jo.put("login", message).toString());
    //     }

    //     return user.get();
	// }

    // public void activateUser(UserEmailAuthenticationDto userEmailAuthenticationDto) {

    //     User user = userRepository.findUserByEmail(userEmailAuthenticationDto.email()).get();
        
    //     user.setIsActive(true);

    //     userRepository.save(user);
    // }
}
