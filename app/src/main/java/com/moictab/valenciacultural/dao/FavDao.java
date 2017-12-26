package com.moictab.valenciacultural.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.moictab.valenciacultural.model.Fav;

import java.util.List;

/**
 * Created by moict on 26/12/2017.
 */

@Dao
public interface FavDao {

    @Query("SELECT * FROM fav")
    List<Fav> getAll();

    @Insert
    void insert(Fav fav);

    @Delete
    void delete(Fav fav);
}
