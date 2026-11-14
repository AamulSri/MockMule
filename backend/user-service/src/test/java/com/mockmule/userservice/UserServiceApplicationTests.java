
package com.mockmule.userservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Test 1: Application context loads successfully
    @Test
    void contextLoads() {
    }

    // Test 2: Successful user registration
    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        String requestBody = """
                {
                    "username": "integrationuser",
                    "email": "integrationuser@gmail.com",
                    "password": "Test@1234"
                }
                """;

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username")
                        .value("integrationuser"))
                .andExpect(jsonPath("$.data.email")
                        .value("integrationuser@gmail.com"));
    }

    // Test 3: Duplicate email registration
    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {

        String registerRequest = """
                {
                    "username": "firstuser",
                    "email": "duplicate@gmail.com",
                    "password": "Test@1234"
                }
                """;

        // First registration
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isCreated());

        String duplicateRequest = """
                {
                    "username": "seconduser",
                    "email": "duplicate@gmail.com",
                    "password": "Test@1234"
                }
                """;

        // Second registration with the same email
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Email already registered"));
    }

    // Test 4: Invalid registration data
    @Test
    void shouldReturnBadRequestForInvalidRegistrationData()
            throws Exception {

        String requestBody = """
                {
                    "username": "",
                    "email": "validuser@gmail.com",
                    "password": "Test@1234"
                }
                """;

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    // Test 5: Successful login
    @Test
    void shouldLoginUserSuccessfully() throws Exception {

        String registerRequest = """
                {
                    "username": "logintest",
                    "email": "logintest@gmail.com",
                    "password": "Test@1234"
                }
                """;

        // Register user
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isCreated());

        String loginRequest = """
                {
                    "email": "logintest@gmail.com",
                    "password": "Test@1234"
                }
                """;

        // Login
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email")
                        .value("logintest@gmail.com"))
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    // Test 6: Incorrect password
    @Test
    void shouldReturnUnauthorizedForIncorrectPassword()
            throws Exception {

        String registerRequest = """
                {
                    "username": "wrongpassworduser",
                    "email": "wrongpassword@gmail.com",
                    "password": "Test@1234"
                }
                """;

        // Register user
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isCreated());

        String loginRequest = """
                {
                    "email": "wrongpassword@gmail.com",
                    "password": "Wrong@1234"
                }
                """;

        // Login with incorrect password
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Invalid email or password"));
    }

    // Test 7: Protected API without JWT
    @Test
    void shouldReturnUnauthorizedWhenAccessingProtectedApiWithoutToken()
            throws Exception {

        mockMvc.perform(get("/api/v1/users/profile"))
                .andExpect(status().isUnauthorized());
    }

    // Test 8: Normal USER cannot access ADMIN API
    @Test
    void shouldReturnForbiddenWhenUserAccessesAdminApi()
            throws Exception {

        String registerRequest = """
                {
                    "username": "securityuser",
                    "email": "securityuser@gmail.com",
                    "password": "Test@1234"
                }
                """;

        // Register normal user
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isCreated());

        String loginRequest = """
                {
                    "email": "securityuser@gmail.com",
                    "password": "Test@1234"
                }
                """;

        // Login and extract JWT
        String loginResponse = mockMvc.perform(
                        post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson = objectMapper.readTree(loginResponse);

        String jwt = responseJson
                .get("data")
                .get("token")
                .asText();

        // Attempt to access ADMIN endpoint
        mockMvc.perform(get("/api/v1/admin/test")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isForbidden());
    }
}
