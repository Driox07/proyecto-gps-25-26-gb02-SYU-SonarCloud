package com.gb02.syumsvc.controller;

import org.springframework.web.bind.annotation.RestController;

import com.gb02.syumsvc.config.DebugConfig;
import com.gb02.syumsvc.exceptions.SessionExpiredException;
import com.gb02.syumsvc.exceptions.SessionNotFoundException;
import com.gb02.syumsvc.exceptions.UnexpectedErrorException;
import com.gb02.syumsvc.exceptions.UserNotFoundException;
import com.gb02.syumsvc.model.Model;
import com.gb02.syumsvc.model.dto.UsuarioDTO;
import com.gb02.syumsvc.utils.Response;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * REST controller for user management operations.
 * Handles CRUD operations on user resources.
 */
@RestController
public class UserController {

    /**
     * Retrieves user information by username (nick).
     * Returns public data for non-authenticated users, full data for the user themselves.
     * 
     * @param nick Username to retrieve
     * @param sessionToken Optional session token from 'oversound_auth' cookie
     * @return ResponseEntity with user data, or error message
     */
    @GetMapping("/user/{nick}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String nick, @CookieValue(value = "oversound_auth", required = false) String sessionToken) {
        try {
            UsuarioDTO requestedUser = Model.getModel().getUsuarioByNick(nick);
            
            int currentUserId = -1;
            // Attempt to get session only if token is present
            if (sessionToken != null && !sessionToken.isBlank()) {
                try {
                    currentUserId = Model.getModel().getSessionByToken(sessionToken).getIdUsuario();
                } catch (SessionNotFoundException | SessionExpiredException e) {
                    currentUserId = -1; // Continue as unauthenticated
                } catch (Exception e) {
                    currentUserId = -1; // Invalid session, continue as unauthenticated
                }
            }
            
            // Hide private data if not the current user
            if (requestedUser.getIdUsuario() != currentUserId) {
                requestedUser.setContrasena(null);
                requestedUser.setApellido1(null);
                requestedUser.setApellido2(null);
            }
            
            return ResponseEntity.ok().body(requestedUser.toMap());
        } catch (UserNotFoundException e) {
            System.err.println("User not found: " + e.getMessage());
            return ResponseEntity.status(404).body(Response.getErrorResponse(404, "User not found"));
        } catch (UnexpectedErrorException e) {
            System.err.println("Unexpected error fetching user: " + e.getMessage());
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while fetching user data."));
        } catch (Exception e) {
            System.err.println("General error fetching user: " + e.getMessage());
             
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while fetching user data."));
        } 
    }

    /**
     * Applies changes from a payload to a user's base data.
     * Automatically hashes password if 'contrasena' field is present.
     * 
     * @param baseUser Original user data
     * @param changes Changes to apply
     * @return Modified user data
     */
    private Map<String, Object> applyUserChanges(Map<String, Object> baseUser, Map<String, Object> changes) {
        for (String key : changes.keySet()) {
            Object value = changes.get(key);
            if (key.equals("contrasena")) {
                value = com.gb02.syumsvc.utils.SecureUtils.hashPassword((String) value);
            }
            baseUser.put(key, value);
        }
        return baseUser;
    }

    /**
     * Updates user information (partial update).
     * User can only modify their own data.
     * 
     * @param nick Username to update
     * @param payload Map containing fields to update
     * @param sessionToken Session token from 'oversound_auth' cookie (required)
     * @return ResponseEntity with updated user data, or error message
     */
    @PatchMapping("/user/{nick}")
    public ResponseEntity<Map<String, Object>> patchUser(@PathVariable String nick, @RequestBody Map<String, Object> payload, @CookieValue(value = "oversound_auth", required = true) String sessionToken) {
        try {
            UsuarioDTO requestedUser = Model.getModel().getUsuarioByNick(nick);
            int currentUserId = Model.getModel().getSessionByToken(sessionToken).getIdUsuario();
            
            // Authorization check: user can only modify their own data
            if (requestedUser.getIdUsuario() != currentUserId) {
                return ResponseEntity.status(403).body(Response.getErrorResponse(403, "You are not authorized to modify this user's data."));
            }
            
            // Apply changes and update user
            UsuarioDTO updatedUser = new UsuarioDTO();
            updatedUser.fromMap(applyUserChanges(requestedUser.toMap(), payload));
            Model.getModel().updateUsuario(currentUserId, updatedUser);
            updatedUser.setContrasena(null);

            return ResponseEntity.ok().body(updatedUser.toMap());
        } catch (SessionNotFoundException e) {
            System.err.println("Session not found during user update: " + e.getMessage());
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Invalid session token."));
        } catch (SessionExpiredException e) {
            System.err.println("Session expired during user update: " + e.getMessage());
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Session has expired."));
        } catch (UserNotFoundException e) {
            System.err.println("User not found during update: " + e.getMessage());
            return ResponseEntity.status(404).body(Response.getErrorResponse(404, "User not found"));
        } catch (UnexpectedErrorException e) {
            System.err.println("Unexpected error updating user: " + e.getMessage());
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while updating user data."));
        } catch (Exception e) {
            System.err.println("Unexpected error updating user: " + e.getMessage());
             
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while updating user data."));
        } 
    }

    /**
     * Deletes a user account.
     * User can only delete their own account.
     * 
     * @param nick Username to delete
     * @param sessionToken Session token from 'oversound_auth' cookie (required)
     * @return ResponseEntity with success message, or error message
     */
    @DeleteMapping("/user/{nick}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String nick, @CookieValue(value = "oversound_auth", required = true) String sessionToken) {
        try {
            UsuarioDTO requestedUser = Model.getModel().getUsuarioByNick(nick);
            int currentUserId = Model.getModel().getSessionByToken(sessionToken).getIdUsuario();
            
            // Authorization check: user can only delete their own account
            if (requestedUser.getIdUsuario() != currentUserId) {
                return ResponseEntity.status(403).body(Response.getErrorResponse(403, "You are not authorized to delete this user."));
            }
            
            Model.getModel().deleteUsuario(requestedUser.getIdUsuario());
            return ResponseEntity.ok().body(Response.getOnlyMessage("User deleted successfully."));
        } catch (UserNotFoundException e) {
            System.err.println("User not found during deletion: " + e.getMessage());
            return ResponseEntity.status(404).body(Response.getErrorResponse(404, "User not found"));
        } catch (SessionNotFoundException e) {
            System.err.println("Session not found during user deletion: " + e.getMessage());
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Invalid session token."));
        } catch (SessionExpiredException e) {
            System.err.println("Session expired during user deletion: " + e.getMessage());
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Session has expired."));
        } catch (UnexpectedErrorException e) {
            System.err.println("Unexpected error deleting user: " + e.getMessage());
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while deleting user."));
        } catch (Exception e) {
            System.err.println("Unexpected error deleting user: " + e.getMessage());
             
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while deleting user."));
        } 
    }
}