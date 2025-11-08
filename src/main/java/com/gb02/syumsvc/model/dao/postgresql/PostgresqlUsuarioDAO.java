package com.gb02.syumsvc.model.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.gb02.syumsvc.model.dao.UsuarioDAO;
import com.gb02.syumsvc.model.dto.UsuarioDTO;

public class PostgresqlUsuarioDAO implements UsuarioDAO {

    @Override
    public UsuarioDTO[] obtainUsuarios() {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM Usuarios";
        try{
            Statement ps = connection.createStatement();          
            ResultSet rset = ps.executeQuery(query);
            ArrayList<UsuarioDTO> results = new ArrayList<>();
            while(rset.next()){
                UsuarioDTO u = new UsuarioDTO();
                u.setIdUsuario(rset.getInt("idUsuario"));
                u.setNick(rset.getString("nick"));
                u.setNombre(rset.getString("nombre"));
                u.setApellido1(rset.getString("apellido1"));
                u.setApellido2(rset.getString("apellido2"));
                u.setFechaReg(rset.getDate("fechaReg"));
                u.setEmail(rset.getString("email"));
                u.setContrasena(rset.getString("contrasena"));
                u.setIdArtista(rset.getInt("idArtista"));
                results.add(u);
            }        
            return results.toArray(new UsuarioDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining usuarios (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new UsuarioDTO[0];
        }
    }

    @Override
    public UsuarioDTO obtainUsuario(int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM Usuarios WHERE idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idUsuario);
            ResultSet rset = ps.executeQuery();
            if(rset.next()){
                UsuarioDTO u = new UsuarioDTO();
                u.setIdUsuario(rset.getInt("idUsuario"));
                u.setNick(rset.getString("nick"));
                u.setNombre(rset.getString("nombre"));
                u.setApellido1(rset.getString("apellido1"));
                u.setApellido2(rset.getString("apellido2"));
                u.setFechaReg(rset.getDate("fechaReg"));
                u.setEmail(rset.getString("email"));
                u.setContrasena(rset.getString("contrasena"));
                u.setIdArtista(rset.getInt("idArtista"));
                return u;
            }else{
                return null;
            }
        }catch(Exception e){
            System.err.println("Error obtaining usuario by idUsuario (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            return null;
        }
    }

    @Override
    public UsuarioDTO obtainUsuario(String nick) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM Usuarios WHERE nick = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, nick);
            ResultSet rset = ps.executeQuery();
            if(rset.next()){
                UsuarioDTO u = new UsuarioDTO();
                u.setIdUsuario(rset.getInt("idUsuario"));
                u.setNick(rset.getString("nick"));
                u.setNombre(rset.getString("nombre"));
                u.setApellido1(rset.getString("apellido1"));
                u.setApellido2(rset.getString("apellido2"));
                u.setFechaReg(rset.getDate("fechaReg"));
                u.setEmail(rset.getString("email"));
                u.setContrasena(rset.getString("contrasena"));
                u.setIdArtista(rset.getInt("idArtista"));
                return u;
            }else{
                return null;
            }
        }catch(Exception e){
            System.err.println("Error obtaining usuario by nick (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean insertUsuario(UsuarioDTO usuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "INSERT INTO Usuarios (nick, nombre, apellido1, apellido2, fechaReg, email, contrasena, idArtista) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, usuario.getNick());
            ps.setString(2,  usuario.getNombre());
            ps.setString(3,  usuario.getApellido1());
            ps.setString(4,  usuario.getApellido2());
            ps.setDate(5,  usuario.getFechaReg());
            ps.setString(6,  usuario.getEmail());
            ps.setString(7,  usuario.getContrasena());
            ps.setInt(8,  usuario.getIdArtista());
            ps.executeUpdate();
            return true;
        }catch(Exception e){
            System.err.println("Error inserting usuario (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modifyUsuario(int idUsuario, UsuarioDTO usuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "UPDATE Usuarios SET nick = ?, nombre = ?, apellido1 = ?, apellido2 = ?, fechaReg = ?, email = ?, contrasena = ?, idArtista = ? WHERE idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, usuario.getNick());
            ps.setString(2,  usuario.getNombre());
            ps.setString(3,  usuario.getApellido1());
            ps.setString(4,  usuario.getApellido2());
            ps.setDate(5,  usuario.getFechaReg());
            ps.setString(6,  usuario.getEmail());
            ps.setString(7,  usuario.getContrasena());
            ps.setInt(8,  usuario.getIdArtista());
            ps.setInt(9, idUsuario);
            ps.executeUpdate();
            return true;
        }catch(Exception e){
            System.err.println("Error modifying usuario (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteUsuario(int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "DELETE FROM Usuarios WHERE idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            return true;
        }catch(Exception e){
            System.err.println("Error deleting usuario (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            return false;
        }
    }
    
}
