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


@RestController
public class SessionController {

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> payload) {
        try{
            Model model = Model.getModel();
            // Creación del usuario con el payload
            UsuarioDTO usuario = new UsuarioDTO();
            usuario.fromMap(payload);
            // Hasheo de la contraseeña y registro
            usuario.setContrasena(SecureUtils.hashPassword(usuario.getContrasena()));
            UsuarioDTO nuevoUsuario = model.registrarUsuario(usuario);
            // Creación de la sesión
            SesionDTO sesion = new SesionDTO();
            sesion.setIdUsuario(nuevoUsuario.getIdUsuario());
            java.util.Date expDate = new java.util.Date();
            expDate.setTime(expDate.getTime() + (1000 * 60 * 60 * 24)); // 24 horas (creo)
            sesion.setFechaValidez(new Date(expDate.getTime()));
            sesion.setToken(SecureUtils.generateSessionToken());
            Model.getModel().insertarSesion(sesion);
            nuevoUsuario.setContrasena(null); // No devolver la contraseña
            // Respuesta exitosa
            return ResponseEntity.ok().body(Map.of("registered_user", nuevoUsuario.toMap(), "session_token", sesion.getToken()));
        }catch(DupedEmailException dem){
            return ResponseEntity.badRequest().body(Map.of("error", "This email is already registered."));
        }catch(DupedUsernameException dun){
            return ResponseEntity.badRequest().body(Map.of("error", "This username is already registered."));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during registration."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, Object> payload) {
        try{
            // Validación de los campos
            if(!payload.containsKey("nick") || !payload.containsKey("contrasena")){
                return ResponseEntity.badRequest().body(Map.of("error", "Nick and password are required."));
            }
            // Obtención del usuario por nick o email
            UsuarioDTO usuario;
            if(payload.get("nick").toString().contains("@")){
                usuario = Model.getModel().getUsuarioByMail(payload.get("nick").toString());
            }else{
                usuario = Model.getModel().getUsuarioByNick(payload.get("nick").toString());
            }
            // Verificación de la contraseña
            Boolean passwordMatch = SecureUtils.verifyPassword(payload.get("contrasena").toString(), usuario.getContrasena());
            if(!passwordMatch){
                return ResponseEntity.status(401).body(Map.of("error", "Wrong user or password."));
            }
            // Creación de la sesión
            SesionDTO sesion = new SesionDTO();
            sesion.setIdUsuario(usuario.getIdUsuario());
            java.util.Date expDate = new java.util.Date();
            expDate.setTime(expDate.getTime() + (1000 * 60 * 60 * 24)); // 24 horas (creo)
            sesion.setFechaValidez(new Date(expDate.getTime()));
            sesion.setToken(SecureUtils.generateSessionToken());
            Model.getModel().insertarSesion(sesion);
            // Respuesta exitosa
            return ResponseEntity.ok().body(Map.of("msg", "Login successful.", "session_token", sesion.getToken()));
        }catch(UserNotFoundException unfe){
            return ResponseEntity.status(401).body(Map.of("error", "Wrong user or password."));
        }catch(UnexpectedErrorException uee){
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during login."));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during login."));
        }
    }

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> authenticateUser(@CookieValue(value = "oversound_auth", required = true) String token) {
        try{
            SesionDTO sesion = Model.getModel().getSessionByToken(token);
            if(sesion == null){
                return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Invalid session token."));
            }
            UsuarioDTO usuario = Model.getModel().getUsuario(sesion.getIdUsuario());
            usuario.setContrasena(null); // No devolver la contraseña
            return ResponseEntity.ok().body(usuario.toMap());
        }catch(SessionExpiredException see){
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Session has expired."));
        }catch(SessionNotFoundException snfe){
            return ResponseEntity.status(401).body(Response.getErrorResponse(401, "Invalid session token."));
        }catch(UnexpectedErrorException uee){
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "An unknown error occurred during authentication."));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(Response.getErrorResponse(500, "An unknown error occurred during authentication."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutUser(@CookieValue(value = "oversound_auth", required = true) String token) {
        try{
            SesionDTO sesion = Model.getModel().getSessionByToken(token);
            if(sesion == null){
                return ResponseEntity.status(401).body(Map.of("error", "Invalid session token."));
            }
            Model.getModel().deleteSesion(sesion.getIdSesion());
            return ResponseEntity.ok().body(Map.of("success", "Logout successful."));
        }catch(SessionNotFoundException snfe){
            return ResponseEntity.status(401).body(Map.of("error", "Invalid session token."));
        }catch(UnexpectedErrorException uee){
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during logout."));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "An unknown error occurred during logout."));
        }
    }
    
}
