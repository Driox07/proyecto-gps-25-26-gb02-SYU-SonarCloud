package com.gb02.syumsvc.model.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.gb02.syumsvc.model.dao.CancionFavDAO;
import com.gb02.syumsvc.model.dto.CancionFavDTO;

public class PostgresqlCancionFavDAO implements CancionFavDAO {

    @Override
    public CancionFavDTO[] obtainCancionFavByUser(int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM CancionesFav WHERE idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idUsuario);
            ResultSet rset = ps.executeQuery();
            ArrayList<CancionFavDTO> results = new ArrayList<>();
            while(rset.next()){
                CancionFavDTO cf = new CancionFavDTO();
                cf.setIdCancion(rset.getInt(0));
                cf.setIdUsuario(rset.getInt(1));
                results.add(cf);
            }        
            return results.toArray(new CancionFavDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining canciones fav by idUsuario (PostgresqlCancionFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new CancionFavDTO[0];
        }
    }

    @Override
    public CancionFavDTO[] obtainCancionFavByCancion(int idCancion) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM CancionesFav WHERE idCancion = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idCancion);
            ResultSet rset = ps.executeQuery();
            ArrayList<CancionFavDTO> results = new ArrayList<>();
            while(rset.next()){
                CancionFavDTO cf = new CancionFavDTO();
                cf.setIdCancion(rset.getInt(0));
                cf.setIdUsuario(rset.getInt(1));
                results.add(cf);
            }        
            return results.toArray(new CancionFavDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining cancion fav by idCancion (PostgresqlCancionFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new CancionFavDTO[0];
        }
    }

    @Override
    public CancionFavDTO[] obtainCancionFav() {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM CancionesFav";
        try{
            Statement s = connection.createStatement();
            ResultSet rset = s.executeQuery(query);
            ArrayList<CancionFavDTO> results = new ArrayList<>();
            while(rset.next()){
                CancionFavDTO cf = new CancionFavDTO();
                cf.setIdCancion(rset.getInt(0));
                cf.setIdUsuario(rset.getInt(1));
                results.add(cf);
            }        
            return results.toArray(new CancionFavDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining all cancion fav (PostgresqlCancionFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new CancionFavDTO[0];
        }
    }

    @Override
    public CancionFavDTO obtainCancionFav(int idCancion, int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM CancionesFav WHERE idCancion = ? AND idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idCancion);
            ps.setInt(2, idUsuario);
            ResultSet rset = ps.executeQuery();
            if(rset.next()){
                CancionFavDTO cf = new CancionFavDTO();
                cf.setIdCancion(rset.getInt(0));
                cf.setIdUsuario(rset.getInt(1));
                return cf;
            }        
            return null;
        }catch(Exception e){
            System.err.println("Error obtaining cancion fav by idCancion (PostgresqlCancionFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean insertCancionFav(CancionFavDTO cancionFav) {
        // TODO: Comprobación de integridad. ¿Existe la canción en el micro servicio TYA?
        Connection connection = PostgresqlConnector.connect();
        String query = "INSERT INTO CancionesFav VALUES (?, ?)";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, cancionFav.getIdCancion());
            ps.setInt(2, cancionFav.getIdUsuario());
            if(ps.executeUpdate() == 0) return false;
            return true;
        }catch(Exception e){
            System.err.println("Error obtaining cancion fav by idCancion (PostgresqlCancionFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCancionFav(int idCancion, int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "DELETE FROM CancionesFav WHERE idCancion = ? AND idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idCancion);
            ps.setInt(2, idUsuario);
            if(ps.executeUpdate() == 0) return false;
            return true;
        }catch(Exception e){
            System.err.println("Error obtaining cancion fav by idCancion (PostgresqlCancionFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return false;
        }
    }
    
}
