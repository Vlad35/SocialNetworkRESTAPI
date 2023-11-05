package ru.Vlad.Spring.SocialNet.SocialNetwork.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {
    @NotNull
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2,max = 30,message = "Name should be between 2 and 30 characters at length")
    private String username;


    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 2,max = 30,message = "Password should be between 2 and 30 characters at length")
    private String password;

    @NotNull
    @NotEmpty(message = "Date of birth cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private String dateOfBirth;

    public UserRegistrationDTO(String username, String password, String dateOfBirth) {
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public UserRegistrationDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

