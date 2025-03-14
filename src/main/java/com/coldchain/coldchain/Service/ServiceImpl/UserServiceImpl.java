package com.coldchain.coldchain.Service.ServiceImpl;

import com.coldchain.coldchain.Repository.UserRepository;
import com.coldchain.coldchain.Service.UserService;
import com.coldchain.coldchain.entity.Role;
import com.coldchain.coldchain.entity.User;
import com.coldchain.coldchain.entity.UserPassword;
import com.coldchain.coldchain.mapper.DtoToEntity;
import com.coldchain.coldchain.mapper.EntityToDto;
import com.coldchain.coldchain.payload.ResponseDto;
import com.coldchain.coldchain.payload.UserDto;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DtoToEntity dtoToEntity;

    @Autowired
    private EntityToDto entityToDto;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    @Override
    @Transactional
    public UserDto registerUser(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());

        String roleDto = userDto.getRole();

        if (existingUser.isPresent()) {
            UserDto erroruserDto = new UserDto();
            ResponseDto response = new ResponseDto("Error : Email already exists", null,  HttpStatus.CONFLICT.value(), false);
            erroruserDto.setResponseDto(response);
            return erroruserDto;
        }

        try {

            User user = dtoToEntity.userDtoToUser(userDto);

            userRepository.save(user);

            UserDto resposeUserDto = entityToDto.UserToUserDto(user);
            resposeUserDto.setResponseDto(new ResponseDto("Success : register successful",null , HttpStatus.OK.value(), true));

            return resposeUserDto;
            
        } catch (Exception e) {
            UserDto erroruserDto = new UserDto();
            ResponseDto response = new ResponseDto(e.getMessage(),null, HttpStatus.INTERNAL_SERVER_ERROR.value(), false);
            erroruserDto.setResponseDto(response);
            return erroruserDto;
        }
    }
}
