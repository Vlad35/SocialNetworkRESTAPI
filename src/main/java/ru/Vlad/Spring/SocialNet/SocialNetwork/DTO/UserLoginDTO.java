package ru.Vlad.Spring.SocialNet.SocialNetwork.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserLoginDTO {
    @NotNull
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2,max = 30,message = "Name should be between 2 and 30 characters at length")
    private String username;


    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 2,max = 30,message = "Name should be between 2 and 30 characters at length")
    private String password;

    public UserLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserLoginDTO() {
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
}