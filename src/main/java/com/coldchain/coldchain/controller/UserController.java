package com.coldchain.coldchain.controller;

import com.coldchain.coldchain.Repository.UserRepository;
import com.coldchain.coldchain.Service.UserService;
import com.coldchain.coldchain.entity.User;
import com.coldchain.coldchain.payload.LoginDto;
import com.coldchain.coldchain.payload.ResponseDto;
import com.coldchain.coldchain.payload.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Explicitly tell Spring which bean to use
    public UserController(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto response = userService.registerUser(userDto);

        if (response.getResponseDto() != null) {
            System.out.println("error status : " + response.getResponseDto().getStatuscode());
            return ResponseEntity
                    .status(response.getResponseDto().getStatuscode())
                    .body(response.getResponseDto());
        }

        // If successful, return success response with HTTP 201 Created
        System.out.println("success status : " + response.getResponseDto().getStatuscode());
        return ResponseEntity
                .status(response.getResponseDto().getStatuscode())
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto){
        LoginDto response = userService.loginUser(loginDto);

        if (response.getResponseDto() != null) {
            return ResponseEntity
                    .status(response.getResponseDto().getStatuscode())
                    .body(response.getResponseDto());
        }

        return ResponseEntity
                .status(response.getResponseDto().getStatuscode())
                .body(response.getResponseDto());
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> activateAccount(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.isEnabled()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDto("Error: Account already activated!", null, HttpStatus.BAD_REQUEST.value(), false));
            }

            user.setAccountEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok(new ResponseDto("Success: Account activated!", null, HttpStatus.OK.value(), true));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto("Error: Invalid email or user not found", null, HttpStatus.NOT_FOUND.value(), false));
    }



}
