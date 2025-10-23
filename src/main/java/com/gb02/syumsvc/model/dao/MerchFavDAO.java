package com.gb02.syumsvc.model.dao;

import com.gb02.syumsvc.model.dto.MerchFavDTO;

public interface MerchFavDAO {
    public MerchFavDTO[] obtainMerchFavByUser(int userId);
    public MerchFavDTO[] obtainMerchFavByMerch(int merchId);
    public MerchFavDTO[] obtainMerchFav();
    public MerchFavDTO obtainMerchFav(int idUsuario, int idMerch);
    public boolean insertMerchFav(MerchFavDTO merchFav);
    public boolean deleteMerchFav(int idUsuario, int idMerch);
}
