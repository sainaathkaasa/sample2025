package com.coldchain.coldchain.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private String userId;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 4, message = "Name must minimum of 5 character")
    private String username;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Role must not be empty")
    private String role;

    private String password;

    private ResponseDto responseDto;

}
