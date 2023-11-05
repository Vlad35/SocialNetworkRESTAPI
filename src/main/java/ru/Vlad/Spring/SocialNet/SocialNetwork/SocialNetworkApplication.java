package ru.Vlad.Spring.SocialNet.SocialNetwork;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Details.MyUserDetailsService;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Repositories.UserRepository;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.UserService;

@SpringBootApplication
public class SocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetworkApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
