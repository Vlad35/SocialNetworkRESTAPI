package ru.Vlad.Spring.SocialNet.SocialNetwork.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.Role;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Repositories.RoleRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public  void createRole(Role role) {
        roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
