package ru.Vlad.Spring.SocialNet.SocialNetwork.Controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserLoginDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserRegistrationDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.JWTUtil;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.MyUserDetails;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.UserService;
import ru.Vlad.Spring.SocialNet.SocialNetwork.utils.Validator.UserRegistrationDtoValidator;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class RestApiController {
    @Autowired
    private UserRegistrationDtoValidator userRegistrationDtoValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public Map<String,String> createUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO,
                                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return Map.of("message","You have some errors with validating");
        }
        User user = convertToUserToRegistrate(userRegistrationDTO);
        userRegistrationDtoValidator.validate(userRegistrationDTO, bindingResult);
        userService.createUser(user);

        String token = jwtUtil.generateToken(user.getUsername());
        userService.setJwtToken(token);
        return Map.of("jwt-token",token);
    }

    @Transactional
    @PostMapping("/login")
    public String performLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        User user = userService.getUserByName(userLoginDTO.getUsername());
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(),
                        userLoginDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
        }

        String token = jwtUtil.generateTokenAndSetHeader(userLoginDTO.getUsername(), response).substring(7);

        userService.setJwtToken(token);
        return user.toString() + "\n token is: " +  token;
    }



    @GetMapping("/user/friends")
    public String getFriendsList() {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return myUserDetails.getUser().getFriends().toString();
    }

    @GetMapping("/user/searchWithStart")
    public String showUsersStartingWith(@RequestParam("name") String name) {
        return userService.getAllUsers().stream().filter(user -> user.getUsername().startsWith(name)).toList().toString();
    }

    @GetMapping("/user/searchWithContains")
    public String showUsersContaining(@RequestParam("name") String name) {
        return userService.getAllUsers().stream().filter(user -> user.getUsername().contains(name)).toList().toString();
    }

    @Transactional
    @PostMapping("/user/addUser/{id}")
    public Map<String, String> addUser(@PathVariable("id") int id) {
        if(userService.getUserById((long) id).isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            Optional<User> user = Optional.of(userService.getUserByName(myUserDetails.getUsername()));
            Optional<User> friendUser = userService.getUserById((long) id);
            if(friendUser.isPresent() && !user.get().getFriends().contains(friendUser.get())) {
                user.get().getFriends().add(friendUser.get());
                friendUser.get().getFriends().add(user.get());
                return Map.of("Message","OK");
            }else {
                return Map.of("Message","You are friends, I cannot add twice:)");
            }
        }
        return Map.of("Message","Something happened");
    }

    @Transactional
    @PostMapping("/user/removeUser/{id}")
    public Map<String, String> removeUser(@PathVariable("id") int id) {
        if(userService.getUserById((long) id).isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            Optional<User> user = Optional.of(userService.getUserByName(myUserDetails.getUsername()));
            Optional<User> friendUser = userService.getUserById((long) id);
            if(user.isPresent() && friendUser.isPresent() && user.get().getFriends().contains(friendUser.get())) {
                user.get().getFriends().remove(friendUser.get());
                friendUser.get().getFriends().remove(user.get());
                return Map.of("Message","OK");
            }else {
                return Map.of("Message","You are not friends,no way i can delete:)");
            }
        }
        return Map.of("Message","Something happened");
    }

    public User convertToUserToRegistrate(UserRegistrationDTO userRegistrationDTO) {
        return new ModelMapper().map(userRegistrationDTO,User.class);
    }
}
