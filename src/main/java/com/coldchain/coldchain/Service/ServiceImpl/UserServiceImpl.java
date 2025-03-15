package com.coldchain.coldchain.Service.ServiceImpl;

import com.coldchain.coldchain.Repository.UserRepository;
import com.coldchain.coldchain.Service.UserService;
import com.coldchain.coldchain.entity.User;
import com.coldchain.coldchain.jwt.JwtUtils;
import com.coldchain.coldchain.mapper.DtoToEntity;
import com.coldchain.coldchain.mapper.EntityToDto;
import com.coldchain.coldchain.payload.LoginDto;
import com.coldchain.coldchain.payload.ResponseDto;
import com.coldchain.coldchain.payload.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DtoToEntity dtoToEntity;

    @Autowired
    private EntityToDto entityToDto;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private AuthenticationManager manager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    @Override
    @Transactional
    public UserDto registerUser(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());


        if (existingUser.isPresent()) {
            UserDto erroruserDto = new UserDto();
            ResponseDto response = new ResponseDto("Error : Email already exists", null,  HttpStatus.CONFLICT.value(), false);
            erroruserDto.setResponseDto(response);
            return erroruserDto;
        }

        try {

            User user = dtoToEntity.userDtoToUser(userDto);

//            sendActivationEmail(user.getEmail());
            userRepository.save(user);
            // sendMailVerified(user.getEmail());

            UserDto resposeUserDto = entityToDto.UserToUserDto(user);
            resposeUserDto.setResponseDto(new ResponseDto("Success : registeration successful verify the link send to you mail to activate your account",null , HttpStatus.OK.value(), true));

            return resposeUserDto;
            
        } catch (Exception e) {
            UserDto erroruserDto = new UserDto();
            ResponseDto response = new ResponseDto(e.getMessage(),null, HttpStatus.INTERNAL_SERVER_ERROR.value(), false);
            erroruserDto.setResponseDto(response);
            return erroruserDto;
        }
    }
    @Override
    public void sendActivationEmail(String toEmail) {
        System.out.println("Activate account" );
        String activationLink = String.format("http://localhost:2025/api/users/activate-account?email=%s", toEmail.trim());

        SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject("Activate Your Account");

        String emailBody = String.format("Dear User,\n\n" + "Your OTP is: %s\n\n"
                        + "To activate your account, please click the link below:\n%s\n\n"
                        + "If you did not request this, please ignore this email.\n\n" + "Best Regards,\nUrjaLinks Team",
                activationLink);

        message.setText(emailBody);

        mailSender.send(message);
    }
    
    
     @Override
     public LoginDto loginUser(LoginDto loginDto) {
         try {
             Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                     loginDto.getPassword());
             authentication = manager.authenticate(authentication);

             UserDetails userDetails = this.loadUserByUsername(loginDto.getEmail());

             String token = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

             String email = jwtUtils.getUserNameFromJwtToken(token);
             LoginDto successDto = new LoginDto();
//             successDto.setEmail(email);
             successDto.setResponseDto(new ResponseDto("Success : Login Successfull for user : "+email, token, HttpStatus.OK.value(), true));

             return successDto;

         } catch (Exception e) {
             LoginDto errorDto = new LoginDto();
             errorDto.setResponseDto(new ResponseDto("Error : "+e.getMessage(), null, HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), false));
             return errorDto;
         }
     }
}
