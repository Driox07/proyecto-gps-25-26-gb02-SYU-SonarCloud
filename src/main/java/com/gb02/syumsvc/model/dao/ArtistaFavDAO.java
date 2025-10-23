package com.gb02.syumsvc.model.dao;

import com.gb02.syumsvc.model.dto.ArtistaFavDTO;

public interface ArtistaFavDAO {
    public ArtistaFavDTO[] obtainArtistaFavByUser(int userId);
    public ArtistaFavDTO[] obtainArtistaFavByArtista(int artistaId);
    public ArtistaFavDTO[] obtainArtistaFav();
    public ArtistaFavDTO obtainArtistaFav(int idUsuario, int idArtista);
    public boolean insertArtistaFav(ArtistaFavDTO artistaFav);
    public boolean deleteArtistaFav(int idUsuario, int idArtista);
}
