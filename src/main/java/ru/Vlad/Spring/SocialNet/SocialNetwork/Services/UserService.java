package ru.Vlad.Spring.SocialNet.SocialNetwork.Services;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserRegistrationDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.Role;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Repositories.RoleRepository;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Repositories.UserRepository;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.MyUserDetails;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Getter
@Setter
@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private String JwtToken;

    public User createUser(User user) {
        Optional<Role> optionalRole = roleRepository.findByRolename("ROLE_USER");
        Long id = (long) getAllUsers().size() + 1;

        if(user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("User's username is empty");
        }

        if(user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("User's password is empty");
        }

        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            if (!user.getRoles().stream().map(Role::getRolename).toList().contains(role.getRolename())) {
                user.getRoles().add(role);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if(user.getDateOfBirth() == null) {
                user.setDateOfBirth(generateRandomDateOfBirth());
            }
            user.setRegistrationDate(LocalDateTime.now());
            user.setId(id);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    private String generateRandomDateOfBirth() {
        Random random = new Random();
        int randomYears = random.nextInt(81);

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime dateOfBirth = currentDateTime.minusYears(randomYears);

        return Date.from(dateOfBirth.toInstant(ZoneOffset.UTC)).toString();
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUserByName(String name) {
        return userRepository.findUserByUsername(name);
    }


    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO,User.class);
    }

    public boolean areUsersFriends(Long userId1, Long userId2) {
        return userRepository.existsByIdAndFriendsId(userId1, userId2) || userRepository.existsByIdAndFriendsId(userId2, userId1);
    }

    public User getCurrentSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        return myUserDetails.user();
    }

    public User convertToUserToRegistrate(UserRegistrationDTO userRegistrationDTO) {
        return modelMapper.map(userRegistrationDTO,User.class);
    }
}