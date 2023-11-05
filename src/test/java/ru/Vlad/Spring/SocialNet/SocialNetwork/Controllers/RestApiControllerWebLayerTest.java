package ru.Vlad.Spring.SocialNet.SocialNetwork.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserLoginDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.DTO.UserRegistrationDTO;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Details.MyUserDetailsService;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.JWTUtil;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.SecurityConstants;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.UserService;
import ru.Vlad.Spring.SocialNet.SocialNetwork.utils.Validator.UserRegistrationDtoValidator;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@MockBean({UserRegistrationDtoValidator.class, UserService.class,JWTUtil.class, AuthenticationManager.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = RestApiController.class,excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class RestApiControllerWebLayerTest {

    private final WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRegistrationDtoValidator userRegistrationDtoValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    @MockBean
    private MyUserDetailsService UserDetailsService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    private UserRegistrationDTO privateUserRegistrationDTO;
    private UserLoginDTO privateUserLoginDTO;
    private String authorizationToken;
    private MockMvc mvc;

    @Autowired
    RestApiControllerWebLayerTest(WebApplicationContext context) {
        this.context = context;
    }


    @BeforeEach
    public void setup() {
//        mvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(SecurityMockMvcConfigurers.springSecurity())
//                .build();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        privateUserRegistrationDTO = new UserRegistrationDTO();
        privateUserRegistrationDTO.setUsername("Bello");
        privateUserRegistrationDTO.setPassword("pass");
        privateUserRegistrationDTO.setDateOfBirth("1990-10-10");
        privateUserLoginDTO = new UserLoginDTO();
        privateUserLoginDTO.setUsername("Bello");
        privateUserLoginDTO.setPassword("pass");
    }

    @Test
    @Order(1)
    @DisplayName("User can be created")
    void testCreateUser_WhenValidUserDetailsProvided_ThenReturnJWTToken() throws Exception {
        //Arrange
        User user = new ModelMapper().map(privateUserRegistrationDTO,User.class);
        user.setRegistrationDate(LocalDateTime.now());
        user.setId(156560L);

        when(userService.createUser(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(any(String.class))).thenReturn("token");
        doNothing().when(userService).setJwtToken(any(String.class));
        doNothing().when(userRegistrationDtoValidator)
                .validate(Mockito.any(UserRegistrationDTO.class),Mockito.any(BindingResult.class));

        String expectedToken = jwtUtil.generateToken(privateUserRegistrationDTO.getUsername());


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                                            .post("/api/v1/registration")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .accept(MediaType.APPLICATION_JSON)
                                            .content(new ObjectMapper().writeValueAsString(privateUserRegistrationDTO));

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Map createdUsersJwtToken = new ObjectMapper().readValue(responseBodyAsString, Map.class);

        //Assert
        assertFalse(createdUsersJwtToken.get("jwt-token").toString().trim().isEmpty(),"Users JWT should not be empty");
        assertEquals(expectedToken,createdUsersJwtToken.get("jwt-token"),"The returned users JWT token is most likely incorrect");
    }

    @Test
    @Order(2)
    @DisplayName("User name is not empty")
    void testCreateUser_WhenUsernameIsNotProvided_ThenReturn404StatusCode() throws Exception {
        //Arrange
        privateUserRegistrationDTO.setUsername("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserRegistrationDTO));

        String expectedResultString = "You have some errors with validating";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Map createdUsersJwtToken = new ObjectMapper().readValue(responseBodyAsString, Map.class);
        //Assert
        assertEquals(expectedResultString,createdUsersJwtToken.get("message"),
                "Incorrect message returned");
    }

    @Test
    @Order(3)
    @DisplayName("User password is not empty")
    void testCreateUser_WhenPasswordIsNotProvided_ThenReturn404StatusCode() throws Exception {
        //Arrange
        privateUserRegistrationDTO.setPassword("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserRegistrationDTO));

        String expectedResultString = "You have some errors with validating";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Map createdUsersJwtToken = new ObjectMapper().readValue(responseBodyAsString, Map.class);
        //Assert
        assertEquals(expectedResultString,createdUsersJwtToken.get("message"),
                "Incorrect message returned");
    }

    @Test
    @Order(4)
    @DisplayName("User date of birth is not empty")
    void testCreateUser_WhenDateOfBirthIsNotProvided_ThenReturn404StatusCode() throws Exception {
        //Arrange
        privateUserRegistrationDTO.setDateOfBirth("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserRegistrationDTO));

        String expectedResultString = "You have some errors with validating";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Map createdUsersJwtToken = new ObjectMapper().readValue(responseBodyAsString, Map.class);
        //Assert
        assertEquals(expectedResultString,createdUsersJwtToken.get("message"),
                "Incorrect message returned");
    }

    @Test
    @Order(5)
    @DisplayName("User name cannot be shorter than 2 characters")
    void testCreateUser_WhenUsernameIsOnlyOneCharacter_ThenReturn404StatusCode() throws Exception {
        //Arrange
        privateUserRegistrationDTO.setUsername("a");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserRegistrationDTO));

        String expectedResultString = "You have some errors with validating";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Map createdUsersJwtToken = new ObjectMapper().readValue(responseBodyAsString, Map.class);
        //Assert
        assertEquals(expectedResultString,createdUsersJwtToken.get("message"),
                "Incorrect message returned");
    }

    @Test
    @Order(6)
    @DisplayName("User name cannot be shorter than 2 characters")
    void testCreateUser_WhenPasswordIsOnlyOneCharacter_ThenReturn404StatusCode() throws Exception {
        //Arrange
        privateUserRegistrationDTO.setPassword("a");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserRegistrationDTO));

        String expectedResultString = "You have some errors with validating";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Map createdUsersJwtToken = new ObjectMapper().readValue(responseBodyAsString, Map.class);
        //Assert
        assertEquals(expectedResultString,createdUsersJwtToken.get("message"),
                "Incorrect message returned");
    }

    @Test
    @Order(7)
    @DisplayName("User can log in")
    void testPerformLogin_WhenValidUserDetailsProvided_ThenReturnJWTToken() throws Exception {
        //Arrange
        User user = new ModelMapper().map(privateUserLoginDTO,User.class);
        user.setRegistrationDate(LocalDateTime.now());
        user.setDateOfBirth("1999-09-09");
        user.setId(156560L);

        Collection<GrantedAuthority> authorities = Collections.emptyList();
        UsernamePasswordAuthenticationToken authResultToken = new UsernamePasswordAuthenticationToken(user, null, authorities);

        when(userService.getUserByName(any(String.class))).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authResultToken);
        when(jwtUtil.generateTokenAndSetHeader(Mockito.any(String.class),Mockito.any(HttpServletResponse.class))).thenReturn(SecurityConstants.TOKEN_PREFIX + "Token");
        doNothing().when(userService).setJwtToken(any(String.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserLoginDTO));

        String expectedToken = "Bearer Token";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String createdJwtToken = mvcResult.getResponse().getContentAsString();

        //Assert
        assertFalse(createdJwtToken.trim().isEmpty(),"Users Data and JWT should not be empty");
        assertEquals(expectedToken,createdJwtToken,"The returned users JWT token is most likely incorrect");

    }

    @Test
    @Order(8)
    @DisplayName("User name is not empty")
    void testPerformLogin_WhenUsernameIsNotProvided_ThenReturn404StatusCode() throws Exception {
        //Arrange
        privateUserLoginDTO.setUsername("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserLoginDTO));

        String expectedResultString = "You have some errors with validating";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String createdUsersJwtToken = mvcResult.getResponse().getContentAsString();

        //Assert
        assertEquals(expectedResultString,createdUsersJwtToken,"Incorrect message returned");
    }

    @Test
    @Order(9)
    @DisplayName("User password is not empty")
    void testPerformLogin_WhenPasswordIsNotProvided_ThenReturn404StatusCode() throws Exception {
        //Arrange
        privateUserLoginDTO.setPassword("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserLoginDTO));

        String expectedResultString = "You have some errors with validating";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String createdUsersJwtToken = mvcResult.getResponse().getContentAsString();

        //Assert
        assertEquals(expectedResultString,createdUsersJwtToken,"Incorrect message returned");
    }

    @Test
    @Order(10)
    @DisplayName("User name cannot be shorter than 2 characters")
    void testPerformLogin_WhenUsernameIsOnlyOneCharacter_ThenReturn404StatusCode() throws Exception {
        //Arrange
        privateUserLoginDTO.setUsername("a");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserLoginDTO));

        String expectedResultString = "You have some errors with validating";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String createdUsersJwtToken = mvcResult.getResponse().getContentAsString();

        //Assert
        assertEquals(expectedResultString,createdUsersJwtToken,"Incorrect message returned");
    }

    @Test
    @Order(11)
    @DisplayName("User password cannot be shorter than 2 characters")
    void testPerformLogin_WhenPasswordIsOnlyOneCharacter_ThenReturn404StatusCode() throws Exception {
        //Arrange
        privateUserLoginDTO.setPassword("a");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(privateUserLoginDTO));

        String expectedResultString = "You have some errors with validating";

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String createdUsersJwtToken = mvcResult.getResponse().getContentAsString();

        //Assert
        assertEquals(expectedResultString,createdUsersJwtToken,"Incorrect message returned");
    }
/*
    @Test
    @Order(12)
    @WithUserDetails(value = "Rhoda Sipes", userDetailsServiceBeanName = "myUserDetailsService")
    void testGetFriendList_WhenMissingJWT_ThenReturn403() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/friends"))
                .andReturn();
        assertEquals(HttpStatus.OK,mvcResult.getResponse().getStatus(),"Incorrect message returned");
    }*/

}