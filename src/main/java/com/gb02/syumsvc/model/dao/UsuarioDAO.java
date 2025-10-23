package com.gb02.syumsvc.model.dao;

import com.gb02.syumsvc.model.dto.UsuarioDTO;

public interface UsuarioDAO {
    public UsuarioDTO[] obtainUsuarios();
    public UsuarioDTO obtainUsuario(int idUsuario);
    public UsuarioDTO obtainUsuario(String nick);
    public boolean insertUsuario(UsuarioDTO usuario);
    public boolean modifyUsuario(int idUsuario, UsuarioDTO usuario);
    public boolean deleteUsuario(int idUSuario);
}
