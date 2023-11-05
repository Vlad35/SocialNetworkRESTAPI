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
import org.springframework.web.filter.OncePerRequestFilter;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.JWTUtil;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Details.MyUserDetailsService;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.SecurityConstants;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(SecurityConstants.HEADER_STRING);
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            String token = authHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
            if (token.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in " + SecurityConstants.HEADER_STRING + " header");
            } else {
                try {
                    String customerName = jwtUtil.validateTokenAndRetrieveClaim(token);
                    UserDetails userDetails = myUserDetailsService.loadUserByUsername(customerName);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (JWTVerificationException exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
