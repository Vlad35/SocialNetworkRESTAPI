package ru.Vlad.Spring.SocialNet.SocialNetwork.Configs;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.JWTUtil;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.Details.MyUserDetailsService;

import java.io.IOException;

@Component
public class JWTFilter/* extends OncePerRequestFilter*/ {
    private final JWTUtil jwtUtil;
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, MyUserDetailsService myUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.myUserDetailsService = myUserDetailsService;
    }

    /*@Override*/
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(token.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid JWT token in Bearer header");
            } else {
                try{
                    String customerName = jwtUtil.validateTokenAndRetrieveClaim(token);
                    UserDetails userDetails = myUserDetailsService.loadUserByUsername(customerName);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),userDetails.getAuthorities());

                    if(SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }catch (JWTVerificationException exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid JWT token");
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}