package ru.Vlad.Spring.SocialNet.SocialNetwork.Configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.Details.MyUserDetailsService;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

//ТУТ ЗАКОМЕЧЕНО ВСЕ ТО,ЧТО НЕ НУЖНО ДЛЯ BROWSER ВЕРСИИ,ПРИ НЕОБХОДИМОСТИ ПРОСМОТРА REST API,НУЖНО РАЗКОММЕНТИТЬ!!

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;

    //private final JWTFilter jwtFilter;
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        //authenticationManagerBuilder.authenticationProvider(authProvider);
        authenticationManagerBuilder.userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth-> {
                    auth.requestMatchers(antMatcher("/admin/**")).hasRole("ADMIN");
                    auth.requestMatchers(antMatcher("/auth/login")).permitAll();
                    auth.requestMatchers(antMatcher("/error")).permitAll();
                    auth.requestMatchers(antMatcher("/auth/registration")).permitAll();
                    auth.requestMatchers(antMatcher("/auth/rest/registration")).permitAll();
                    auth.requestMatchers(antMatcher("/auth/rest/login")).permitAll();
                    auth.anyRequest().hasAnyRole("ADMIN","USER");
                })
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/processing_login")
                        .defaultSuccessUrl("/showUserInfo",true)
                        .failureUrl("/auth/login?error")
                        .permitAll()
                )
                .logout(logout -> {logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login");
                })
                .exceptionHandling(exc->{
                    exc.accessDeniedPage("/auth/denied");
                });
                //.sessionManagement(SM -> SM.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}