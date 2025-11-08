package com.gb02.syumsvc.model.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.gb02.syumsvc.model.dao.AlbumFavDAO;
import com.gb02.syumsvc.model.dto.AlbumFavDTO;

public class PostgresqlAlbumFavDAO implements AlbumFavDAO {
    @Override
    public AlbumFavDTO[] obtainAlbumFavByUser(int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM AlbumesFav WHERE idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idUsuario);
            ResultSet rset = ps.executeQuery();
            ArrayList<AlbumFavDTO> results = new ArrayList<>();
            while(rset.next()){
                AlbumFavDTO af = new AlbumFavDTO();
                af.setIdAlbum(rset.getInt(0));
                af.setIdUsuario(rset.getInt(1));
                results.add(af);
            }        
            return results.toArray(new AlbumFavDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining albumes fav by idUsuario (PostgresqlAlbumFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new AlbumFavDTO[0];
        }
    }

    @Override
    public AlbumFavDTO[] obtainAlbumFavByAlbum(int idAlbum) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM AlbumesFav WHERE idAlbum = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idAlbum);
            ResultSet rset = ps.executeQuery();
            ArrayList<AlbumFavDTO> results = new ArrayList<>();
            while(rset.next()){
                AlbumFavDTO af = new AlbumFavDTO();
                af.setIdAlbum(rset.getInt(0));
                af.setIdUsuario(rset.getInt(1));
                results.add(af);
            }        
            return results.toArray(new AlbumFavDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining album fav by idAlbum (PostgresqlAlbumFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new AlbumFavDTO[0];
        }
    }

    @Override
    public AlbumFavDTO[] obtainAlbumFav() {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM AlbumesFav";
        try{
            Statement s = connection.createStatement();
            ResultSet rset = s.executeQuery(query);
            ArrayList<AlbumFavDTO> results = new ArrayList<>();
            while(rset.next()){
                AlbumFavDTO af = new AlbumFavDTO();
                af.setIdAlbum(rset.getInt(0));
                af.setIdUsuario(rset.getInt(1));
                results.add(af);
            }        
            return results.toArray(new AlbumFavDTO[results.size()]);
        }catch(Exception e){
            System.err.println("Error obtaining all album fav (PostgresqlAlbumFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return new AlbumFavDTO[0];
        }
    }

    @Override
    public AlbumFavDTO obtainAlbumFav(int idAlbum, int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "SELECT * FROM AlbumesFav WHERE idAlbum = ? AND idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idAlbum);
            ps.setInt(2, idUsuario);
            ResultSet rset = ps.executeQuery();
            if(rset.next()){
                AlbumFavDTO af = new AlbumFavDTO();
                af.setIdAlbum(rset.getInt(0));
                af.setIdUsuario(rset.getInt(1));
                return af;
            }        
            return null;
        }catch(Exception e){
            System.err.println("Error obtaining album fav by idAlbum (PostgresqlAlbumFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean insertAlbumFav(AlbumFavDTO albumFav) {
        // TODO: Comprobación de integridad. ¿Existe el artista en el micro servicio TYA?
        Connection connection = PostgresqlConnector.connect();
        String query = "INSERT INTO AlbumesFav VALUES (?, ?)";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, albumFav.getIdAlbum());
            ps.setInt(2, albumFav.getIdUsuario());
            if(ps.executeUpdate() == 0) return false;
            return true;
        }catch(Exception e){
            System.err.println("Error obtaining album fav by idAlbum (PostgresqlAlbumFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAlbumFav(int idAlbum, int idUsuario) {
        Connection connection = PostgresqlConnector.connect();
        String query = "DELETE FROM AlbumesFav WHERE idAlbum = ? AND idUsuario = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idAlbum);
            ps.setInt(2, idUsuario);
            if(ps.executeUpdate() == 0) return false;
            return true;
        }catch(Exception e){
            System.err.println("Error obtaining album fav by idAlbum (PostgresqlAlbumFavDAO)");
            System.err.println("Reason: " + e.getMessage());
            return false;
        }
    }
}
