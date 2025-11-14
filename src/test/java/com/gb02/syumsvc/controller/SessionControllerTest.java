package com.gb02.syumsvc.controller;

import com.gb02.syumsvc.model.Model;
import com.gb02.syumsvc.model.dto.SesionDTO;
import com.gb02.syumsvc.model.dto.UsuarioDTO;
import com.gb02.syumsvc.exceptions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.servlet.http.Cookie;

/**
 * Integration tests for SessionController endpoints.
 * Tests registration, login, authentication, and logout functionality.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String testUsername = "tses_" + (System.currentTimeMillis() % 100000000);
    private String testPassword = "testpassword123";
    private String testEmail = "tses_" + (System.currentTimeMillis() % 100000000) + "@test.com";

    @BeforeEach
    public void setup() {
        // Clean up test user if exists
        try {
            UsuarioDTO user = Model.getModel().getUsuarioByNick(testUsername);
            if (user != null) {
                Model.getModel().deleteUsuario(user.getIdUsuario());
            }
        } catch (Exception e) {
            // User doesn't exist, that's fine
        }
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        String registerJson = String.format("""
            {
                "nick": "%s",
                "nombre": "Test",
                "apellido1": "User",
                "apellido2": "Session",
                "email": "%s",
                "contrasena": "%s"
            }
            """, testUsername, testEmail, testPassword);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registered_user.nick").value(testUsername))
                .andExpect(jsonPath("$.registered_user.email").value(testEmail));
    }

    @Test
    public void testRegisterUser_DuplicateUsername() throws Exception {
        // First registration
        String registerJson = String.format("""
            {
                "nick": "%s",
                "nombre": "Test",
                "apellido1": "User",
                "email": "%s",
                "contrasena": "%s"
            }
            """, testUsername, testEmail, testPassword);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isOk());

        // Try to register again with same username
        String duplicateJson = String.format("""
            {
                "nick": "%s",
                "nombre": "Test2",
                "apellido1": "User2",
                "email": "other@test.com",
                "contrasena": "%s"
            }
            """, testUsername, testPassword);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicateJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testLogin_Success() throws Exception {
        // Register user first
        String registerJson = String.format("""
            {
                "nick": "%s",
                "nombre": "Test",
                "apellido1": "User",
                "email": "%s",
                "contrasena": "%s"
            }
            """, testUsername, testEmail, testPassword);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isOk());

        // Now login
        String loginJson = String.format("""
            {
                "nick": "%s",
                "contrasena": "%s"
            }
            """, testUsername, testPassword);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful."))
                .andExpect(jsonPath("$.session_token").exists());
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        String loginJson = String.format("""
            {
                "nick": "nonexistentuser_%s",
                "contrasena": "wrongpassword"
            }
            """, System.currentTimeMillis());

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testAuth_ValidToken() throws Exception {
        // Register and login to get token
        String registerJson = String.format("""
            {
                "nick": "%s",
                "nombre": "Test",
                "apellido1": "User",
                "email": "%s",
                "contrasena": "%s"
            }
            """, testUsername, testEmail, testPassword);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isOk());

        String loginJson = String.format("""
            {
                "nick": "%s",
                "contrasena": "%s"
            }
            """, testUsername, testPassword);

        MvcResult loginResult = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        // Extract token from response body and create cookie
        String responseBody = loginResult.getResponse().getContentAsString();
        String token = responseBody.split("session_token\":\"")[1].split("\"")[0];
        Cookie authCookie = new Cookie("oversound_auth", token);

        // Test authentication with valid token
        mockMvc.perform(get("/auth")
                .cookie(authCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick").value(testUsername));
    }

    @Test
    public void testAuth_InvalidToken() throws Exception {
        mockMvc.perform(get("/auth")
                .cookie(new Cookie("oversound_auth", "invalidtoken123")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testAuth_MissingCookie() throws Exception {
        mockMvc.perform(get("/auth"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testLogout_Success() throws Exception {
        // Register and login
        String registerJson = String.format("""
            {
                "nick": "%s",
                "nombre": "Test",
                "apellido1": "User",
                "email": "%s",
                "contrasena": "%s"
            }
            """, testUsername, testEmail, testPassword);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isOk());

        String loginJson = String.format("""
            {
                "nick": "%s",
                "contrasena": "%s"
            }
            """, testUsername, testPassword);

        MvcResult loginResult = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        // Extract token from response body and create cookie
        String responseBody = loginResult.getResponse().getContentAsString();
        String token = responseBody.split("session_token\":\"")[1].split("\"")[0];
        Cookie authCookie = new Cookie("oversound_auth", token);

        // Logout
        mockMvc.perform(post("/logout")
                .cookie(authCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"));

        // Try to authenticate with logged out token
        mockMvc.perform(get("/auth")
                .cookie(authCookie))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegisterUser_NickTooLong() throws Exception {
        // Try to register with nick longer than 30 characters
        String longNick = "a".repeat(31); // 31 characters
        String registerJson = String.format("""
            {
                "nick": "%s",
                "nombre": "Test",
                "apellido1": "User",
                "email": "test@example.com",
                "contrasena": "testpassword123"
            }
            """, longNick);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testRegisterUser_EmailTooLong() throws Exception {
        // Try to register with email longer than 30 characters
        String longEmail = "verylongemailaddress12345@test.com"; // 35 characters
        String registerJson = String.format("""
            {
                "nick": "shortuser",
                "nombre": "Test",
                "apellido1": "User",
                "email": "%s",
                "contrasena": "testpassword123"
            }
            """, longEmail);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").exists());
    }
}
