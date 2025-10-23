package com.gb02.syumsvc.model.dao;

import com.gb02.syumsvc.model.dto.AlbumFavDTO;

public interface AlbumFavDAO {
    public AlbumFavDTO[] obtainAlbumFavByUser(int userId);
    public AlbumFavDTO[] obtainAlbumFavByAlbum(int albumId);
    public AlbumFavDTO[] obtainAlbumFav();
    public AlbumFavDTO obtainAlbumFav(int idUsuario, int idAlbum);
    public boolean insertAlbumFav(AlbumFavDTO albumFav);
    public boolean deleteAlbumFav(int idUsuario, int idAlbum);
}
