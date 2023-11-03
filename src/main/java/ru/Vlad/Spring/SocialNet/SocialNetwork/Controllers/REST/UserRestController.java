package ru.Vlad.Spring.SocialNet.SocialNetwork.Controllers.REST;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.MyUserDetails;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.UserService;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rest/users")
public class UserRestController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/my-friends")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        Optional<User> user = userService.getUserByName(myUserDetails.getUsername());
        return user.get().getFriends().toString();
    }

    @GetMapping("/searchWithStart")
    public String showUsersStartingWith(@RequestParam("name") String name) {
        return userService.getAllUsers().stream().filter(user -> user.getUsername().startsWith(name)).toList().toString();
    }

    @GetMapping("/searchWithContains")
    public String showUsersContaining(@RequestParam("name") String name) {
        return userService.getAllUsers().stream().filter(user -> user.getUsername().contains(name)).toList().toString();
    }

    @Transactional
    @PostMapping("/addUser/{id}")
    public Map<String, String> addUser(@PathVariable("id") int id) {
            if(userService.getUserById((long) id).isPresent()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
                Optional<User> user = userService.getUserByName(myUserDetails.getUsername());
                Optional<User> friendUser = userService.getUserById((long) id);
                if(user.isPresent() && friendUser.isPresent() && !user.get().getFriends().contains(friendUser.get())) {
                    user.get().getFriends().add(friendUser.get());
                    friendUser.get().getFriends().add(user.get());
                    return Map.of("Message","OK");
                }else {
                    return Map.of("Message","You are friends,i cannot add twice:)");
                }
            }
        return Map.of("Message","Something happened");
    }

    @Transactional
    @PostMapping("/removeUser/{id}")
    public Map<String, String> removeUser(@PathVariable("id") int id) {
        if(userService.getUserById((long) id).isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            Optional<User> user = userService.getUserByName(myUserDetails.getUsername());
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

}
