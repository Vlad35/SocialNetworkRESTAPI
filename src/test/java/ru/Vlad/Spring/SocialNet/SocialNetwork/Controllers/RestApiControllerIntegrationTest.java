package ru.Vlad.Spring.SocialNet.SocialNetwork.Controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Models.User;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Security.SecurityConstants;
import ru.Vlad.Spring.SocialNet.SocialNetwork.Services.UserService;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RestApiControllerIntegrationTest {
    private final Long ADD_OR_REMOVE_USER_TEST_ID = 17L;
    private final String SEARCH_USER_TEST_NAME = "A";
    private final String CREATE_USERNAME_TO_TEST = "RTUYTIUYOGKHJVH";
    private final String LOGIN_USERNAME_TO_TEST = "Lasandra Schamberger";
    private String authorizationToken;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("User can be created(Valid arguments)")
    @Order(1)
    void testCreateUser_WhenValidUserDetailsProvided_ThenReturnsCreatedUsersJWTToken() throws JSONException {
        //Arrange
        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("username",CREATE_USERNAME_TO_TEST);
        userDetailsRequestJson.put("password", "pass");
        userDetailsRequestJson.put("dateOfBirth","1990-10-10");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJson.toString(),httpHeaders);

        //Act
        ResponseEntity<Map> createdUserDetailsEntity = testRestTemplate.postForEntity("/api/v1/registration",request,Map.class);

        Map<String,String> createdUserDetails = createdUserDetailsEntity.getBody();
        System.out.println(createdUserDetails.get("jwt-token"));

        //Assert
        assertTrue(createdUserDetails.containsKey("jwt-token") && !createdUserDetails.get("jwt-token").trim().isEmpty(),"The returned JWT token seems to be incorrect");
    }

    @Test
    @DisplayName("User can log in(Valid arguments)")
    @Order(2)
    void testPerformLogin_WhenValidUserDetailsProvided_ThenReturnsLoginnedUsersJWTToken() throws JSONException {
        //Arrange
        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("username", LOGIN_USERNAME_TO_TEST);
        userDetailsRequestJson.put("password", "pass");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJson.toString(),httpHeaders);

        //Act
        ResponseEntity response = testRestTemplate.postForEntity("/api/v1/login",request,null);

        authorizationToken = response.getHeaders().getValuesAsList(SecurityConstants.HEADER_STRING).get(0).substring(7);


        //Assert
        assertEquals(HttpStatus.OK,response.getStatusCode(),"HTTP status code should be 2000");
        assertNotNull(authorizationToken,"Response should contain Authorization header with JWT");
    }

    @Test
    @DisplayName("GET /friends requires JWT")
    @Order(3)
    void testGetFriends_WhenMissingJWT_ThenReturn403() {
        //Arrange
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept","application/json");

        HttpEntity requestEntity = new HttpEntity(null,httpHeaders);

        //Act
        /*ResponseEntity<List<User>> response = testRestTemplate.exchange("/api/rest/users/my-friends",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<User>>() {}
        );*/
        ResponseEntity<String> response = testRestTemplate.exchange("/api/v1/user/friends",
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        //Assert
        assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode(),"HTTP status code 403 Forbidden should have been returned");
    }

    @Test
    @Order(4)
    @DisplayName("GET /friends works")
    void testGetFriends_WhenValidJWTProvided_ThenReturnFriends() {
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(authorizationToken);

        HttpEntity request = new HttpEntity(headers);
        //Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/v1/user/friends",
                HttpMethod.GET,
                request,
                String.class
        );

        //Assert
        assertEquals(HttpStatus.OK,response.getStatusCode(),"HTTP status code should be 200");
        assertNotNull(response.getBody(),"User should have friends");
    }

    @Test
    @Order(5)
    @DisplayName("GET /searchWithStart requires JWT")
    void testShowUsersStartingWith_WhenMissingJWT_ThenReturn403() {
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/json");

        HttpEntity requestEntity = new HttpEntity(null,headers);

        //Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/v1//user/searchWithStart?name=" + SEARCH_USER_TEST_NAME,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        //Assert
        assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode(),"HTTP status code 403 Forbidden should have been returned");
    }

    @Test
    @Order(6)
    @DisplayName("GET /searchWithStart works")
    void testShowUsersStartingWith_WhenValidJwtTokenProvided_ThenReturnUsers() {
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(authorizationToken);

        HttpEntity request = new HttpEntity(headers);
        //Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/v1/user/searchWithStart?name=" + SEARCH_USER_TEST_NAME,
                HttpMethod.GET,
                request,
                String.class
        );

        //Assert
        assertEquals(HttpStatus.OK,response.getStatusCode(),"HTTP status code should be 200");
        assertNotNull(response.getBody(),"User should have friends");
    }

    @Test
    @Order(7)
    @DisplayName("GET /searchWithContains requires JWT")
    void testShowUsersContaining_WhenMissingJWT_ThenReturn403() {
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/json");

        HttpEntity requestEntity = new HttpEntity(null,headers);

        //Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/v1//user/searchWithContains?name=" + SEARCH_USER_TEST_NAME,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        //Assert
        assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode(),"HTTP status code 403 Forbidden should have been returned");
    }

    @Test
    @Order(8)
    @DisplayName("GET /searchWithContains works")
    void testShowUsersContaining_WhenValidJwtTokenProvided_ThenReturnUsers() {
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(authorizationToken);

        HttpEntity request = new HttpEntity(headers);
        //Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/v1/user/searchWithContains?name=" + SEARCH_USER_TEST_NAME,
                HttpMethod.GET,
                request,
                String.class
        );

        //Assert
        assertEquals(HttpStatus.OK,response.getStatusCode(),"HTTP status code should be 200");
        assertNotNull(response.getBody(),"User should have friends");
    }

    @Test
    @Order(9)
    @DisplayName("POST /addUser/{id} requires")
    void testAddUser_WhenMissingJWT_ThenReturn403() {
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/json");

        HttpEntity requestEntity = new HttpEntity(null,headers);

        //Act
        ResponseEntity<Map> response = testRestTemplate.exchange(
                "/api/v1//user/addUser/" + ADD_OR_REMOVE_USER_TEST_ID,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        //Assert
        assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode(),"HTTP status code 403 Forbidden should have been returned");
    }

    @Test
    @Order(10)
    @DisplayName("POST /addUser/{id} works")
    void testAddUser_WhenValidJWTProvided_ThenReturnMessage() {
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(authorizationToken);

        String userName = extractUserNameFromToken(authorizationToken);
        User user = userService.getUserByName(userName);
        Boolean isActual = user.getFriends().contains(userService.getUserById(ADD_OR_REMOVE_USER_TEST_ID).get());


        HttpEntity request = new HttpEntity(headers);

        // Act
        ResponseEntity<Map> response = testRestTemplate.exchange(
                "/api/v1/user/addUser/" + ADD_OR_REMOVE_USER_TEST_ID,
                HttpMethod.POST,
                request,
                Map.class
        );

        // Assert
        if(!user.getUsername().isEmpty() && userService.getUserById(ADD_OR_REMOVE_USER_TEST_ID).isPresent()) {
            if (isActual) {
                assertEquals("You are friends, I cannot add twice:)", response.getBody().get("Message"), "Message should be \"You are friends, I cannot add twice:)");
            } else {
                assertEquals("OK", response.getBody().get("Message"), "Message should be \"OK\"");
            }
        }else {
            assertEquals("Something happened", response.getBody().get("Message"), "Message should be \"Something happened\"");
        }
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status code should be 200 OK");
        assertNotNull(response.getBody(), "Return value cannot be null");
    }

    @Test
    @Order(11)
    @DisplayName("POST /removeUser/{id} requires")
    void testRemoveUser_WhenMissingJWT_ThenReturn403() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity requestEntity = new HttpEntity(null, headers);

        // Act
        ResponseEntity<Map> response = testRestTemplate.exchange(
                "/api/v1/user/removeUser/" + ADD_OR_REMOVE_USER_TEST_ID,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "HTTP status code 403 Forbidden should have been returned");
    }

    @Test
    @Order(12)
    @DisplayName("POST /removeUser/{id} works")
    void testRemoveUser_WhenValidJWTProvided_ThenReturnMessage() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(authorizationToken);

        String userName = extractUserNameFromToken(authorizationToken);
        User user = userService.getUserByName(userName);
        Boolean isActual = user.getFriends().contains(userService.getUserById(ADD_OR_REMOVE_USER_TEST_ID).get());

        HttpEntity request = new HttpEntity(headers);

        // Act
        ResponseEntity<Map> response = testRestTemplate.exchange(
                "/api/v1/user/removeUser/" + ADD_OR_REMOVE_USER_TEST_ID,
                HttpMethod.POST,
                request,
                Map.class
        );

        // Assert
        if (!user.getUsername().isEmpty() && userService.getUserById(ADD_OR_REMOVE_USER_TEST_ID).isPresent()) {
            if (isActual) {
                assertEquals("OK", response.getBody().get("Message"), "Message should be \"OK\"");
            } else {
                assertEquals("You are not friends, no way I can delete:)", response.getBody().get("Message"), "Message should be \"You are not friends, no way I can delete:)");
            }
        } else {
            assertEquals("Something happened", response.getBody().get("Message"), "Message should be \"Something happened\"");
        }
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status code should be 200 OK");
        assertNotNull(response.getBody(), "Return value cannot be null");
    }

    public String extractUserNameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);

        return decodedJWT.getClaim("customerName").asString();
    }

}