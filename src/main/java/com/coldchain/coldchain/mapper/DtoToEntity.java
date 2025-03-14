package com.coldchain.coldchain.mapper;

import com.coldchain.coldchain.Repository.UserPasswordRepository;
import com.coldchain.coldchain.entity.Role;
import com.coldchain.coldchain.entity.User;
import com.coldchain.coldchain.entity.UserPassword;
import com.coldchain.coldchain.payload.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DtoToEntity {

    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User userDtoToUser(UserDto userDto){
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());

        UserPassword userPassword = new UserPassword();
        userPassword.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userPassword.setUser(user);

        userPasswordRepository.save(userPassword);

        user.setRole(Role.valueOf(userDto.getRole().toUpperCase()));


        return user;
    }
}
