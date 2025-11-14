package com.gb02.syumsvc.model.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.gb02.syumsvc.exceptions.SessionNotFoundException;
import com.gb02.syumsvc.exceptions.UnexpectedErrorException;
import com.gb02.syumsvc.model.dao.SesionDAO;
import com.gb02.syumsvc.model.dto.SesionDTO;

public class PostgresqlSesionDAO implements SesionDAO {

    @Override
    public SesionDTO[] obtainSesiones() {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM Sesiones";
        try{
            Statement ps = connection.createStatement();          
            ResultSet rset = ps.executeQuery(query);
            ArrayList<SesionDTO> results = new ArrayList<>();
            while(rset.next()){
                SesionDTO s = new SesionDTO();
                s.setIdSesion(rset.getInt("idSesion"));
                s.setToken(rset.getString("token"));
                s.setFechaValidez(rset.getDate("fechaValidez"));
                s.setIdUsuario(rset.getInt("idUsuario"));
                results.add(s);
            }        
            return results.toArray(new SesionDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining sesiones (PostgresqlSesionDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new SesionDTO[0];
        }
    }

    @Override
    public SesionDTO obtainSesion(int idSesion) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM Sesiones WHERE idSesion = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idSesion);
            ResultSet rset = ps.executeQuery();
            if(rset.next()){
                SesionDTO s = new SesionDTO();
                s.setIdSesion(rset.getInt("idSesion"));
                s.setToken(rset.getString("token"));
                s.setFechaValidez(rset.getDate("fechaValidez"));
                s.setIdUsuario(rset.getInt("idUsuario"));
                return s;
            }else{
                return null;
            }
        }catch(Exception e){
            System.err.println("Error obtaining sesion by idSesion (PostgresqlSesionDAO)");
            System.err.println("Reason: " + e.getMessage());        
            return null;
        }
    }

    @Override
    public SesionDTO obtainSesion(String token) throws SessionNotFoundException, UnexpectedErrorException{
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM Sesiones WHERE token = ?";
        try{
            if (token == null){
                throw new SessionNotFoundException("Session token is null");
            }
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, token);
            ResultSet rset = ps.executeQuery();
            if(rset.next()){
                SesionDTO s = new SesionDTO();
                s.setIdSesion(rset.getInt("idSesion"));
                s.setToken(rset.getString("token"));
                s.setFechaValidez(rset.getDate("fechaValidez"));
                s.setIdUsuario(rset.getInt("idUsuario"));
                return s;
            }else{
                throw new SessionNotFoundException("Session not found for token: " + token);
            }
        }catch(Exception e){
            System.err.println("Error obtaining sesion by token (PostgresqlSesionDAO)");
            System.err.println("Reason: " + e.getMessage());        
            throw new UnexpectedErrorException("Unexpected error obtaining session by token: " + token);
        }
    }

    @Override
    public boolean insertSesion(SesionDTO sesion) throws UnexpectedErrorException {
        Connection connection = PostgresqlConnector.connect();
        String query = "INSERT INTO Sesiones (token, fechaValidez, idUsuario) VALUES (?, ?, ?)";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, sesion.getToken());
            ps.setDate(2, sesion.getFechaValidez());
            ps.setInt(3, sesion.getIdUsuario());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1){
                throw new UnexpectedErrorException("Error inserting sesion (PostgresqlSesionDAO)");
            }
            return true;
        }catch(Exception e){
            System.err.println("Error inserting sesion (PostgresqlSesionDAO)");
            System.err.println("Reason: " + e.getMessage());        
            throw new UnexpectedErrorException("Error creating a session for user " + sesion.getIdUsuario());
        }
    }

    @Override
    public boolean modifySesion(int idSesion, SesionDTO sesion) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifySesion'");
    }

    @Override
    public boolean deleteSesion(int idSesion) throws SessionNotFoundException, UnexpectedErrorException {
        Connection connection = PostgresqlConnector.connect();
        String query = "DELETE FROM Sesiones WHERE idSesion = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idSesion);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SessionNotFoundException("Session not found with id: " + idSesion);
            }
            return true;
        }catch(Exception e){
            System.err.println("Error deleting sesion (PostgresqlSesionDAO)");
            System.err.println("Reason: " + e.getMessage());
            throw new UnexpectedErrorException("Unexpected error deleting session with id: " + idSesion);
        }
    }
    
}
