package ru.Vlad.Spring.SocialNet.SocialNetwork.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findUserByUsername(String username);

}
