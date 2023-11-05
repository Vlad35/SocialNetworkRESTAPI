package ru.Vlad.Spring.SocialNet.SocialNetwork.Services;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserRegistrationDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.Role;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Repositories.RoleRepository;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private String JwtToken;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


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


    public User getUserByName(String name) {
        User user = userRepository.findUserByUsername(name);
        return user;
    }


    public User convertToUserToRegistrate(UserRegistrationDTO userRegistrationDTO) {
        return modelMapper.map(userRegistrationDTO,User.class);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }
}