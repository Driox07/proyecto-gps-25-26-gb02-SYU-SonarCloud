package com.gb02.syumsvc.model.dto;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class SesionDTO implements DTO {
    private Integer idSesion;
    private String token;
    private Date fechaValidez;
    private Integer idUsuario;

    public Integer getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(Integer idSesion) {
        this.idSesion = idSesion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getFechaValidez() {
        return fechaValidez;
    }

    public void setFechaValidez(Date fechaValidez) {
        this.fechaValidez = fechaValidez;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idSesion", this.idSesion);
        map.put("token", this.token);
        map.put("fechaValidez", this.fechaValidez);
        map.put("idUsuario", this.idUsuario);
        return map;
    }

    @Override
    public void fromMap(Map<String, Object> map) {
        idSesion = map.get("idSesion") != null ? (Integer) map.get("idSesion") : null;
        token = map.get("token") != null ? (String) map.get("token") : null;
        fechaValidez = map.get("fechaValidez") != null ? (Date) map.get("fechaValidez") : null;
        idUsuario = map.get("idUsuario") != null ? (Integer) map.get("idUsuario") : null;
    }

}