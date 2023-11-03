package ru.Vlad.Spring.SocialNet.SocialNetwork.REST;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Controllers.REST.AuthRestController;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserRegistrationDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.JWTUtil;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.UserService;
import ru.Vlad.Spring.SocialNet.SocialNetwork.utils.Validator.UserDTOValidator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AuthRestController.class,
excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class AuthRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserDTOValidator userDTOValidator;

    @MockBean
    UserService userService;

    @MockBean
    JWTUtil jwtUtil;

    @MockBean
    AuthenticationManager authenticationManager;

    @Test
    @DisplayName("User can be created")
    void testCreateUser_WhenValidUserDetailsProvided_ThenReturnsCreatedUsersJWTToken() throws Exception {
        //Arrange
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUsername("Vullo");
        userRegistrationDTO.setPassword("pass");
        userRegistrationDTO.setDateOfBirth(new Date());

        Map<String,String> expectedResult = new HashMap<>();
        expectedResult.put("jwt-token","Bearer ");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/rest/registration")
                              .contentType(MediaType.APPLICATION_JSON)
                              .accept(MediaType.APPLICATION_JSON)
                              .content(new ObjectMapper().writeValueAsString(userRegistrationDTO));

        when(userService.convertToUserToRegistrate(any())).thenReturn(new ModelMapper().map(userRegistrationDTO, User.class));
        doNothing().when(userDTOValidator).validate(Mockito.any(), Mockito.any());
        when(jwtUtil.generateToken(any())).thenReturn("Bearer ");
        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Map actualResult = new ObjectMapper().readValue(responseBodyAsString, Map.class);
        System.out.println(expectedResult.get("jwt-token") + " " + actualResult.get("jwt-token"));
        //Assert
        assertEquals(expectedResult.get("jwt-token"),actualResult.get("jwt-token"),"Excepted and Actual tokens during registration are not same");
    }

    @Test
    void testPerformLogin_WhenValidUserDetailsProvided_ThenReturnsLoginnedUsersJWTToken() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Vullo");
        userDTO.setPassword("pass");

        Map<String,String> expectedResult = new HashMap<>();
        expectedResult.put("jwt-tokens","Token ");

        Authentication successfulAuthentication = new UsernamePasswordAuthenticationToken("username", "password");


        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/rest/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(successfulAuthentication);
        when(jwtUtil.generateToken(any())).thenReturn("Token ");

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Map actualResult = new ObjectMapper().readValue(responseBodyAsString, Map.class);
        System.out.println(expectedResult.get("jwt-token") + " " + actualResult.get("jwt-token"));


        assertEquals(expectedResult.get("jwt-tokens"),actualResult.get("jwt-tokens"),"Expected and Actual tokens during login are not same");
    }

    /*@Test
    void testShowUserInfo_WhenUserIsPresent_ThenReturnFriendsList() throws Exception {
        //Arrange
        Set<User> expectedList = new HashSet<>();

        MyUserDetails userDetails = new MyUserDetails(new User(11111L,"username", "password", new Date(),LocalDateTime.now()));

        TestingAuthenticationToken auth = new TestingAuthenticationToken(userDetails, "credentials", "ROLE_USER");

        SecurityContextHolder.getContext().setAuthentication(auth);

        User user1 = new User(10000L,"Vello","pass",new Date(), LocalDateTime.now());
        User user2 = new User(10001L,"Vellp","pass",new Date(), LocalDateTime.now());

        expectedList.add(user2);
        user2.getFriends().add(user1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/auth/rest/showUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        when(userService.getUserByName(any(String.class))).thenReturn(Optional.of(new User(10001L, "Vellp", "pass", new Date(), LocalDateTime.now())));

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Set actualList = new ObjectMapper().readValue(responseBodyAsString, Set.class);

        //Assert
        assertArrayEquals(expectedList.toArray(),actualList.toArray(),"List of Friends is not same");
    }*/

    /*@Test
    void testPerformLogin_WhenInvalidUserDetailsProvided_ThenReturnsLoginnedUsersJWTToken() throws JsonProcessingException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Some name");
        userDTO.setPassword("Some pass");
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(),
                        userDTO.getPassword());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> {
            authenticationManager.authenticate(authInputToken);
        },"Bad Credentials Exception should have thrown");
    }
*/
    /*@Test
    @DisplayName("If authenticate() method causes BadCredentialsException,a UserServiceException is thrown")
    void testPerformLogin_WhenAuthenticateMethodThrowsBadCredentialsException_ThenThrowsUserServiceException() {
        //Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        //Act & Assert
        assertThrows(UserServiceException.class,() -> {

        },"Should have thrown UserServiceException instead");
        //Assert

    }*/

}