package ru.Vlad.Spring.SocialNet.SocialNetwork.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    @NotNull
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2,max = 30,message = "Name should be between 2 and 30 characters at length")
    private String username;


    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private String dateOfBirth;
}

