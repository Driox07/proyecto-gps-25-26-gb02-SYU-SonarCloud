package com.gb02.syumsvc.model.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.gb02.syumsvc.model.dao.ArtistaFavDAO;
import com.gb02.syumsvc.model.dto.ArtistaFavDTO;

public class PostgresqlArtistaFavDAO implements ArtistaFavDAO {
    @Override
    public ArtistaFavDTO[] obtainArtistaFavByUser(int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM ArtistasFav WHERE idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idUsuario);
            ResultSet rset = ps.executeQuery();
            ArrayList<ArtistaFavDTO> results = new ArrayList<>();
            while(rset.next()){
                ArtistaFavDTO af = new ArtistaFavDTO();
                af.setIdArtista(rset.getInt(0));
                af.setIdUsuario(rset.getInt(1));
                results.add(af);
            }        
            return results.toArray(new ArtistaFavDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining artistas fav by idUsuario (PostgresqlArtistaFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new ArtistaFavDTO[0];
        }
    }

    @Override
    public ArtistaFavDTO[] obtainArtistaFavByArtista(int idArtista) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM ArtistasFav WHERE idArtista = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idArtista);
            ResultSet rset = ps.executeQuery();
            ArrayList<ArtistaFavDTO> results = new ArrayList<>();
            while(rset.next()){
                ArtistaFavDTO af = new ArtistaFavDTO();
                af.setIdArtista(rset.getInt(0));
                af.setIdUsuario(rset.getInt(1));
                results.add(af);
            }        
            return results.toArray(new ArtistaFavDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining artista fav by idArtista (PostgresqlArtistaFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new ArtistaFavDTO[0];
        }
    }

    @Override
    public ArtistaFavDTO[] obtainArtistaFav() {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM ArtistasFav";
        try{
            Statement s = connection.createStatement();
            ResultSet rset = s.executeQuery(query);
            ArrayList<ArtistaFavDTO> results = new ArrayList<>();
            while(rset.next()){
                ArtistaFavDTO af = new ArtistaFavDTO();
                af.setIdArtista(rset.getInt(0));
                af.setIdUsuario(rset.getInt(1));
                results.add(af);
            }        
            return results.toArray(new ArtistaFavDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining all artista fav (PostgresqlArtistaFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new ArtistaFavDTO[0];
        }
    }

    @Override
    public ArtistaFavDTO obtainArtistaFav(int idArtista, int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM ArtistasFav WHERE idArtista = ? AND idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idArtista);
            ps.setInt(2, idUsuario);
            ResultSet rset = ps.executeQuery();
            if(rset.next()){
                ArtistaFavDTO af = new ArtistaFavDTO();
                af.setIdArtista(rset.getInt(0));
                af.setIdUsuario(rset.getInt(1));
                return af;
            }        
            return null;
        }catch(Exception e){
            System.err.println("Error obtaining artista fav by idArtista (PostgresqlArtistaFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean insertArtistaFav(ArtistaFavDTO artistaFav) {
        // TODO: Comprobación de integridad. ¿Existe el artista en el micro servicio TYA?
        Connection connection = PostgresqlConnector.connect();
        String query = "INSERT INTO ArtistasFav VALUES (?, ?)";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, artistaFav.getIdArtista());
            ps.setInt(2, artistaFav.getIdUsuario());
            if(ps.executeUpdate() == 0) return false;
            return true;
        }catch(Exception e){
            System.err.println("Error obtaining artista fav by idArtista (PostgresqlArtistaFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteArtistaFav(int idArtista, int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "DELETE FROM ArtistasFav WHERE idArtista = ? AND idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idArtista);
            ps.setInt(2, idUsuario);
            if(ps.executeUpdate() == 0) return false;
            return true;
        }catch(Exception e){
            System.err.println("Error obtaining artista fav by idArtista (PostgresqlArtistaFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return false;
        }
    }
}
