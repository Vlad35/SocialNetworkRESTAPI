package ru.Vlad.Spring.SocialNet.SocialNetwork.Controllers.REST;

import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserRegistrationDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.JWTUtil;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.MyUserDetails;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.UserService;
import ru.Vlad.Spring.SocialNet.SocialNetwork.utils.Validator.UserDTOValidator;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


@RestController
@NoArgsConstructor
@RequestMapping("/auth/rest/")
public class AuthRestController {
    @Autowired
    private UserDTOValidator userDTOValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;



    /*@PostMapping("/registration")
    public UserRegistrationDTO createUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO,
                                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new RuntimeException("Something went wrong");
        }
        User user = convertToUserToRegistrate(userRegistrationDTO);
        userDTOValidator.validate(userRegistrationDTO, bindingResult);
        userService.createUser(user);

        String token = jwtUtil.generateToken(user.getUsername());
        userService.setJwtToken(token);
        return userRegistrationDTO;
    }*/

    @PostMapping("/registration")
    public Map<String,String> createUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO,
                                         BindingResult bindingResult) {
        User user = convertToUserToRegistrate(userRegistrationDTO);
        userDTOValidator.validate(userRegistrationDTO, bindingResult);
        userService.createUser(user);

        String token = jwtUtil.generateToken(user.getUsername());
        userService.setJwtToken(token);
        return Map.of("jwt-token",token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody @Valid UserDTO userDTO){
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(),
                        userDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        String token = jwtUtil.generateToken(userDTO.getUsername());
        userService.setJwtToken(token);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        return Map.of("jwt-tokens", token);
    }

    @GetMapping("/showUserInfo")
    public Set<User> showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        Optional<User> user = userService.getUserByName(myUserDetails.getUsername());
        return user.get().getFriends();
    }

    public User convertToUserToRegistrate(UserRegistrationDTO userRegistrationDTO) {
        return new ModelMapper().map(userRegistrationDTO,User.class);
    }

}
