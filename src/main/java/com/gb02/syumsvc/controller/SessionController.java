package com.gb02.syumsvc.controller;

import java.sql.Date;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gb02.syumsvc.exceptions.DupedEmailException;
import com.gb02.syumsvc.exceptions.DupedUsernameException;
import com.gb02.syumsvc.exceptions.SessionExpiredException;
import com.gb02.syumsvc.exceptions.SessionNotFoundException;
import com.gb02.syumsvc.exceptions.UnexpectedErrorException;
import com.gb02.syumsvc.exceptions.UserNotFoundException;
import com.gb02.syumsvc.model.Model;
import com.gb02.syumsvc.model.dto.SesionDTO;
import com.gb02.syumsvc.model.dto.UsuarioDTO;
import com.gb02.syumsvc.utils.Response;
import com.gb02.syumsvc.utils.SecureUtils;


/**
 * REST controller for session management (login, register, authentication, logout).
 * Handles user registration, authentication, and session lifecycle.
 */
@RestController
public class SessionController {

    private static final long SESSION_DURATION_MS = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    /**
     * Registers a new user and creates an initial session.
     * 
     * @param payload Map containing user data (nick, contrasena, email, etc.)
     * @return ResponseEntity with registered user data and session token, or error message
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> payload) {
        try {
            Model model = Model.getModel();
            
            // Create user from payload
            UsuarioDTO usuario = new UsuarioDTO();
            usuario.fromMap(payload);
            
            // Hash password and register user
            usuario.setContrasena(SecureUtils.hashPassword(usuario.getContrasena()));
            UsuarioDTO nuevoUsuario = model.registrarUsuario(usuario);
            
            // Create initial session for the new user
            SesionDTO sesion = new SesionDTO();
            sesion.setIdUsuario(nuevoUsuario.getIdUsuario());
            java.util.Date expDate = new java.util.Date();
            expDate.setTime(expDate.getTime() + SESSION_DURATION_MS);
            sesion.setFechaValidez(new Date(expDate.getTime()));
            sesion.setToken(SecureUtils.generateSessionToken());
            Model.getModel().insertarSesion(sesion);
            
            // Remove password from response for security
            nuevoUsuario.setContrasena(null);
            
            return ResponseEntity.ok().body(Map.of("registered_user", nuevoUsuario.toMap(), "session_token", sesion.getToken()));
        } catch (DupedEmailException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "This email is already registered."));
        } catch (DupedUsernameException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "This username is already registered."));
        } catch (Exception e) {
            System.err.println("Unexpected error during registration: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during registration."));
        }
    }

    /**
     * Authenticates a user and creates a new session.
     * Accepts either username (nick) or email as identifier.
     * 
     * @param payload Map containing 'nick' (username or email) and 'contrasena' (password)
     * @return ResponseEntity with success message and session token, or error message
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, Object> payload) {
        try {
            // Validate required fields
            if (!payload.containsKey("nick") || !payload.containsKey("contrasena")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Nick and password are required."));
            }
            
            // Fetch user by nick or email (auto-detect if contains @)
            UsuarioDTO usuario;
            if (payload.get("nick").toString().contains("@")) {
                usuario = Model.getModel().getUsuarioByMail(payload.get("nick").toString());
            } else {
                usuario = Model.getModel().getUsuarioByNick(payload.get("nick").toString());
            }
            
            // Verify password
            Boolean passwordMatch = SecureUtils.verifyPassword(payload.get("contrasena").toString(), usuario.getContrasena());
            if (!passwordMatch) {
                return ResponseEntity.status(401).body(Map.of("error", "Wrong user or password."));
            }
            
            // Create new session
            SesionDTO sesion = new SesionDTO();
            sesion.setIdUsuario(usuario.getIdUsuario());
            java.util.Date expDate = new java.util.Date();
            expDate.setTime(expDate.getTime() + SESSION_DURATION_MS);
            sesion.setFechaValidez(new Date(expDate.getTime()));
            sesion.setToken(SecureUtils.generateSessionToken());
            Model.getModel().insertarSesion(sesion);
            
            return ResponseEntity.ok().body(Map.of("msg", "Login successful.", "session_token", sesion.getToken()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Wrong user or password."));
        } catch (UnexpectedErrorException e) {
            System.err.println("Unexpected error during login: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during login."));
        } catch (Exception e) {
            System.err.println("Unexpected error during login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during login."));
        }
    }

    /**
     * Validates a session token and returns the authenticated user's data.
     * 
     * @param token Session token from 'oversound_auth' cookie
     * @return ResponseEntity with user data (without password), or error message
     */
    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> authenticateUser(@CookieValue(value = "oversound_auth", required = true) String token) {
        try {
            SesionDTO sesion = Model.getModel().getSessionByToken(token);
            if (sesion == null) {
                return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Invalid session token."));
            }
            
            UsuarioDTO usuario = Model.getModel().getUsuario(sesion.getIdUsuario());
            usuario.setContrasena(null); // Remove password from response
            return ResponseEntity.ok().body(usuario.toMap());
        } catch (SessionExpiredException e) {
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Session has expired."));
        } catch (SessionNotFoundException e) {
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Invalid session token."));
        } catch (UnexpectedErrorException e) {
            System.err.println("Unexpected error during authentication: " + e.getMessage());
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "An unknown error occurred during authentication."));
        } catch (Exception e) {
            System.err.println("Unexpected error during authentication: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "An unknown error occurred during authentication."));
        }
    }

    /**
     * Logs out a user by deleting their session.
     * 
     * @param token Session token from 'oversound_auth' cookie
     * @return ResponseEntity with success message, or error message
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutUser(@CookieValue(value = "oversound_auth", required = true) String token) {
        try {
            SesionDTO sesion = Model.getModel().getSessionByToken(token);
            if (sesion == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid session token."));
            }
            
            Model.getModel().deleteSesion(sesion.getIdSesion());
            return ResponseEntity.ok().body(Map.of("success", "Logout successful."));
        } catch (SessionNotFoundException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid session token."));
        } catch (UnexpectedErrorException e) {
            System.err.println("Unexpected error during logout: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during logout."));
        } catch (Exception e) {
            System.err.println("Unexpected error during logout: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during logout."));
        }
    }
}
