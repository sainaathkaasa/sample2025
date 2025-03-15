package com.coldchain.coldchain.Service;

import com.coldchain.coldchain.entity.User;
import com.coldchain.coldchain.payload.LoginDto;
import com.coldchain.coldchain.payload.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.concurrent.CompletableFuture;

public interface UserService extends UserDetailsService {

    UserDto registerUser(UserDto userDto);

    void sendActivationEmail(String toEmail);

    LoginDto loginUser(LoginDto loginDto);
}
