package ru.Vlad.Spring.SocialNet.SocialNetwork.Details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Repositories.UserRepository;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.MyUserDetails;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User optionalUser = userRepository.findUserByUsername(username);
        if(optionalUser == null) {
            throw new UsernameNotFoundException("User Not Found!");
        }
        return new MyUserDetails(optionalUser);
    }

}