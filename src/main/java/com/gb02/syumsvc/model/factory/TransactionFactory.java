package com.gb02.syumsvc.model.factory;

import java.sql.Connection;

import com.gb02.syumsvc.model.dto.SesionDTO;
import com.gb02.syumsvc.model.dto.UsuarioDTO;

public interface TransactionFactory {

    public Connection getConnection();
    public void transactionRollback();
    public UsuarioDTO registrarUsuario(UsuarioDTO usuario);
    public boolean eliminarUsuario(int idUsuario);
    public boolean modificarUsuario(int idUsuario, UsuarioDTO usuario);
    public boolean crearSesion(SesionDTO sesion);
    
}
