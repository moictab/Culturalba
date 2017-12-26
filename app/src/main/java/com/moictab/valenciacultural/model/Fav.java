package com.moictab.valenciacultural.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

/**
 * Created by moict on 26/12/2017.
 */

@Entity
public class Fav {

    @PrimaryKey
    @android.support.annotation.NonNull
    private String name;

    @ColumnInfo(name = "link")
    private String link;

    public String getName() {
        return name;
    }

    public Fav() {

    }

    public Fav(Event event) {
        this.name = event.title;
        this.link = event.link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
