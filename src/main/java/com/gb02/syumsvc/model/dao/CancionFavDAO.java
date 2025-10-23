package com.gb02.syumsvc.model.dao;

import com.gb02.syumsvc.model.dto.CancionFavDTO;

public interface CancionFavDAO {
    public CancionFavDTO[] obtainCancionFavByUser(int userId);
    public CancionFavDTO[] obtainCancionFavByCancion(int cancionId);
    public CancionFavDTO[] obtainCancionFav();
    public CancionFavDTO obtainCancionFav(int idUsuario, int idCancion);
    public boolean insertCancionFav(CancionFavDTO cancionFav);
    public boolean deleteCancionFav(int idUsuario, int idCancion);
}
