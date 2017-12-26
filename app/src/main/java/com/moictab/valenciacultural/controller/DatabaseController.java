package com.moictab.valenciacultural.controller;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.moictab.valenciacultural.dao.FavDao;
import com.moictab.valenciacultural.model.Fav;

/**
 * Created by moict on 26/12/2017.
 */

@Database(entities = {Fav.class}, version = 1)
public abstract class DatabaseController extends RoomDatabase {
    public abstract FavDao favDao();
}
