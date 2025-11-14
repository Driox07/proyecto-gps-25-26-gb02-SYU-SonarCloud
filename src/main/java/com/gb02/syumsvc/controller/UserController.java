package com.gb02.syumsvc.controller;

import org.springframework.web.bind.annotation.RestController;

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


@RestController
public class UserController{

    @GetMapping("/user/{nick}")
    public ResponseEntity<Map<String,Object>> patchUser(@PathVariable String nick, @CookieValue(value = "oversound_auth", required = false) String sessionToken) {
        System.out.println("Received request with nick: " + nick + " and session token: " + sessionToken);
        try{
            UsuarioDTO requestedUser = Model.getModel().getUsuarioByNick(nick);
            
            int currentUserId = -1;
            // Solo intentar obtener sesión si el token no es null
            if(sessionToken != null && !sessionToken.isBlank()){
                try{
                    currentUserId = Model.getModel().getSessionByToken(sessionToken).getIdUsuario();
                }catch(SessionNotFoundException e){
                    currentUserId = -1;
                }catch(Exception e){
                    // Sesión inválida, continuar como no autenticado
                    currentUserId = -1;
                }
            }
            
            // Ocultar datos privados si no es el usuario actual
            if(requestedUser.getIdUsuario() != currentUserId){
                requestedUser.setContrasena(null);
                requestedUser.setApellido1(null);
                requestedUser.setApellido2(null);
            }
            return ResponseEntity.ok().body(requestedUser.toMap());
        }catch(UserNotFoundException unfe){
            return ResponseEntity.status(404).body(Response.getErrorResponse(404, "User not found"));
        }catch(UnexpectedErrorException uee){
            System.out.println("Unexpected error: " + uee.getMessage());
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while fetching user data."));
        }catch(Exception e){
            System.out.println("General error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while fetching user data."));
        } 
    }

    private Map<String,Object> applyUserChanges(Map<String,Object> baseUser, Map<String, Object> changes){
        for(String key : changes.keySet()){
            Object value = changes.get(key);
            if(key.equals("contrasena")){
                value = com.gb02.syumsvc.utils.SecureUtils.hashPassword((String) value);
            }
            baseUser.put(key, value);
        }
        return baseUser;
    }

    @PatchMapping("/user/{nick}")
    public ResponseEntity<Map<String,Object>> getUser(@PathVariable String nick, @RequestBody Map<String, Object> payload, @CookieValue(value = "oversound_auth", required = true) String sessionToken) {
        System.out.println("Received PATCH request with nick: " + nick + " and session token: " + sessionToken);
        try{
            UsuarioDTO requestedUser = Model.getModel().getUsuarioByNick(nick);
            int currentUserId = Model.getModel().getSessionByToken(sessionToken).getIdUsuario();
            if(requestedUser.getIdUsuario() != currentUserId){
                return ResponseEntity.status(403).body(Response.getErrorResponse(403, "You are not authorized to modify this user's data."));
            }
            UsuarioDTO updatedUser = new UsuarioDTO();
            updatedUser.fromMap(applyUserChanges(requestedUser.toMap(), payload));
            Model.getModel().updateUsuario(currentUserId, updatedUser);
            return ResponseEntity.ok().body(updatedUser.toMap());
        }catch(SessionNotFoundException snfe){
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Invalid session token."));
        }catch(SessionExpiredException see){
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Session has expired."));
        }catch(UserNotFoundException unfe){
            return ResponseEntity.status(404).body(Response.getErrorResponse(404, "User not found"));
        }catch(UnexpectedErrorException uee){
            System.out.println("Unexpected error: " + uee.getMessage());
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while updating user data."));
        }catch(Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while updating user data."));
        } 
    }

    @DeleteMapping("/user/{nick}")
    public ResponseEntity<Map<String,Object>> deleteUser(@PathVariable String nick, @CookieValue(value = "oversound_auth", required = true) String sessionToken) {
        System.out.println("Received DELETE request with nick: " + nick + " and session token: " + sessionToken);
        try{
            UsuarioDTO requestedUser = Model.getModel().getUsuarioByNick(nick);
            int currentUserId = Model.getModel().getSessionByToken(sessionToken).getIdUsuario();
            if(requestedUser.getIdUsuario() != currentUserId){
                return ResponseEntity.status(403).body(Response.getErrorResponse(403, "You are not authorized to delete this user."));
            }
            Model.getModel().deleteUsuario(requestedUser.getIdUsuario());
            return ResponseEntity.ok().body(Response.getOnlyMessage("User deleted successfully."));
        }catch(UserNotFoundException unfe){
            return ResponseEntity.status(404).body(Response.getErrorResponse(404, "User not found"));
        }catch(SessionNotFoundException snfe){
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Invalid session token."));
        }catch(SessionExpiredException see){
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Session has expired."));
        }catch(UnexpectedErrorException uee){
            System.out.println("Unexpected error: " + uee.getMessage());
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while deleting user."));
        }catch(Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "Unexpected error occurred while deleting user."));
        } 
    }

}