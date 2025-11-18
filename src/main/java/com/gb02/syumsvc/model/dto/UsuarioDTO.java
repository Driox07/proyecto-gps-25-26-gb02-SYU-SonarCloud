package com.gb02.syumsvc.model.dto;

import java.sql.Date;
import java.util.Map;

public class UsuarioDTO implements DTO {
    private Integer idUsuario;
    private String nick;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private Date fechaReg;
    private String email;
    private String contrasena;
    private Integer idArtista;
    private String imagen;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Integer getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(Integer idArtista) {
        this.idArtista = idArtista;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("idUsuario", this.idUsuario);
        map.put("nick", this.nick);
        map.put("nombre", this.nombre);
        map.put("apellido1", this.apellido1);
        map.put("apellido2", this.apellido2);
        map.put("fechaReg", this.fechaReg);
        map.put("email", this.email);
        map.put("contrasena", this.contrasena);
        map.put("idArtista", this.idArtista);
        map.put("imagen", this.imagen);
        return map;
    }

    @Override
    public void fromMap(Map<String, Object> map) {
        idUsuario = map.get("idUsuario") != null ? (Integer) map.get("idUsuario") : null;
        nick = map.get("nick") != null ? (String) map.get("nick") : null;
        nombre = map.get("nombre") != null ? (String) map.get("nombre") : null;
        apellido1 = map.get("apellido1") != null ? (String) map.get("apellido1") : null;
        apellido2 = map.get("apellido2") != null ? (String) map.get("apellido2") : null;
        fechaReg = map.get("fechaReg") != null ? (Date) map.get("fechaReg") : null;
        email = map.get("email") != null ? (String) map.get("email") : null;
        contrasena = map.get("contrasena") != null ? (String) map.get("contrasena") : null;
        idArtista = map.get("idArtista") != null ? (Integer) map.get("idArtista") : null;
        imagen = map.get("imagen") != null ? (String) map.get("imagen") : null;
    }
}
