package com.moictab.valenciacultural.controller;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.moictab.valenciacultural.model.Event;
import com.moictab.valenciacultural.model.Fav;

import java.util.List;

/**
 * Created by moict on 26/12/2017.
 */

public class FavsController {

    public void saveEventOnFavs(Context context, Event event) {
        DatabaseController controller = Room.databaseBuilder(context, DatabaseController.class, "valencia-cultural-database").allowMainThreadQueries().build();
        controller.favDao().insert(new Fav(event));
    }

    public List<Fav> getAllFavs(Context context) {
        DatabaseController controller = Room.databaseBuilder(context, DatabaseController.class, "valencia-cultural-database").allowMainThreadQueries().build();
        return controller.favDao().getAll();
    }
}
