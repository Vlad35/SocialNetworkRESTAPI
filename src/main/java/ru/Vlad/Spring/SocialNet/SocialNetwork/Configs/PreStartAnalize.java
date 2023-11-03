package ru.Vlad.Spring.SocialNet.SocialNetwork.Configs;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.Role;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.RoleService;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
public class PreStartAnalize implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final Integer NUMBER_OF_PEOPLE_NEEDED = 50;

    @Autowired
    public PreStartAnalize(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if(roleService.getAllRoles().isEmpty()) {
            Role role1 = new Role();
            role1.setRolename("ROLE_ADMIN");
            roleService.createRole(role1);

            Role role2 = new Role();
            role2.setRolename("ROLE_USER");
            roleService.createRole(role2);

            User superUser = new User();
            superUser.setUsername("Vlad");
            superUser.setPassword("pass");
            superUser.getRoles().add(role1);
            userService.createUser(superUser);

            Faker faker = new Faker();

            List<User> users = new ArrayList<>();

            for (int i = 0; i < NUMBER_OF_PEOPLE_NEEDED; i++) {
                User user = new User();
                user.setUsername(faker.name().fullName());
                user.setPassword("pass");
                users.add(user);
            }

            for (User user : users) {
                userService.createUser(user);
            }
            for(int i = 0;i < users.size() - 2;i ++) {
                User userI = users.get(i);
                if(i % 3 == 0) {
                    userI.getFriends().add(users.get(i + 1));
                    userI.getFriends().add(users.get(i + 2));
                    users.get(i + 1).getFriends().add(userI);
                    users.get(i + 2).getFriends().add(userI);
                }else if(i % 3 == 1) {
                    userI.getFriends().add(users.get(i +  1));
                    users.get(i + 1).getFriends().add(userI);
                }
                if((users.size() - 1) % 3 == 1) {
                    users.get(users.size() - 2).getFriends().add(users.get(users.size() - 1));
                    users.get(users.size() - 1).getFriends().add(users.get(users.size() - 2));
                }else if((users.size() - 1) % 3 == 2) {
                    users.get(users.size() - 3).getFriends().add(users.get(users.size() - 2));
                    users.get(users.size() - 2).getFriends().add(users.get(users.size() - 3));
                    users.get(users.size() - 1).getFriends().add(users.get(users.size() - 2));
                    users.get(users.size() - 2).getFriends().add(users.get(users.size() - 1));
                    users.get(users.size() - 1).getFriends().add(users.get(users.size() - 3));
                    users.get(users.size() - 3).getFriends().add(users.get(users.size() - 1));
                }
            }
        }
    }
}

