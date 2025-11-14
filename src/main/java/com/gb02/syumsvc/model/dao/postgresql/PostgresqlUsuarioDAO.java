package com.gb02.syumsvc.model.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

import com.gb02.syumsvc.exceptions.DupedEmailException;
import com.gb02.syumsvc.exceptions.DupedUsernameException;
import com.gb02.syumsvc.exceptions.UnexpectedErrorException;
import com.gb02.syumsvc.exceptions.UserNotFoundException;
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
    public UsuarioDTO obtainUsuario(int idUsuario) throws UserNotFoundException, UnexpectedErrorException {
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
                throw new UserNotFoundException("Usuario con idUsuario " + idUsuario + " no encontrado.");
            }
        }catch(UserNotFoundException unfe){
            throw unfe;
        }catch(Exception e){
            System.err.println("Error obtaining usuario by idUsuario (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error obtaining usuario: " + e.getMessage());
        }
    }

    @Override
    public UsuarioDTO obtainUsuarioByNick(String nick) throws UserNotFoundException, UnexpectedErrorException {
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
                throw new UserNotFoundException("Usuario con nick " + nick + " no encontrado.");
            }
        }catch(UserNotFoundException unfe){
            throw unfe;
        }catch(Exception e){
            System.err.println("Error obtaining usuario by nick (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error obtaining usuario: " + e.getMessage());
        }
    }

    @Override
    public UsuarioDTO obtainUsuarioByMail(String mail) throws UserNotFoundException, UnexpectedErrorException {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM Usuarios WHERE email = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, mail);
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
                throw new UserNotFoundException("Usuario con email " + mail + " no encontrado.");
            }
        }catch(UserNotFoundException unfe){
            throw unfe;
        }catch(Exception e){
            System.err.println("Error obtaining usuario by email (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error obtaining usuario: " + e.getMessage());
        }
    }

    @Override
    public int insertUsuario(UsuarioDTO usuario) throws UnexpectedErrorException, DupedUsernameException, DupedEmailException {
        Connection connection = PostgresqlConnector.connect();
        String query = "INSERT INTO Usuarios (nick, nombre, apellido1, apellido2, email, contrasena, idArtista) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNick());
            ps.setString(2,  usuario.getNombre());
            ps.setString(3,  usuario.getApellido1());
            ps.setString(4,  usuario.getApellido2());
            ps.setString(5,  usuario.getEmail());
            ps.setString(6,  usuario.getContrasena());
            if(usuario.getIdArtista() == null){
                ps.setNull(7, java.sql.Types.INTEGER);
            }else{
                ps.setInt(7, usuario.getIdArtista());
            }
            int rows = ps.executeUpdate();
            if(rows != 1){
                throw new UnexpectedErrorException("");
            }
            ResultSet rs = ps.getGeneratedKeys();
            if (rs != null && rs.next()) {
                return rs.getInt(1);
            } else {
                throw new UnexpectedErrorException("");
            }
        }catch(PSQLException e){
            if(e.getSQLState().equals("23505")){
                String serverError = e.getServerErrorMessage().getDetail();
                if(serverError.contains("nick")){
                    throw new DupedUsernameException("Username " + usuario.getNick() + " already exists");
                }
                if(serverError.contains("email")){
                    throw new DupedEmailException("Email " + usuario.getEmail() + " already exists");
                }
            }
            throw new UnexpectedErrorException("Unexpected error inserting usuario: " + e.getMessage());
        }catch(Exception e){
            System.err.println("Error inserting usuario (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error inserting usuario: " + e.getMessage());
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
            int rows = ps.executeUpdate();
            if(rows != 1){
                throw new UserNotFoundException("Usuario with idUsuario " + idUsuario + " not found.");
            }
            return true;
        }catch(Exception e){
            System.err.println("Error deleting usuario (PostgresqlUsuarioDAO)");
            System.err.println("Reason: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error deleting usuario: " + e.getMessage());
        }
    }
    
}
