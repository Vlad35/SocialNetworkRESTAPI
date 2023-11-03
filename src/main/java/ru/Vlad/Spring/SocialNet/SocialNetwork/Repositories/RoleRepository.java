package ru.Vlad.Spring.SocialNet.SocialNetwork.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRolename(String name);
}
