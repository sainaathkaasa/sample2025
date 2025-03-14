package com.coldchain.coldchain.mapper;

import com.coldchain.coldchain.entity.User;
import com.coldchain.coldchain.payload.UserDto;
import org.springframework.stereotype.Component;

@Component
public class EntityToDto {

    public UserDto UserToUserDto(User user){
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setUsername(userDto.getUsername());
        if(user.getRole() != null){
            userDto.setRole(user.getRole().name());
        }

        return userDto;
    }
}
