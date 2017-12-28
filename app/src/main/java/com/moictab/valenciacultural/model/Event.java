package com.moictab.valenciacultural.model;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.Serializable;

/**
 * Created by moict on 11/12/2017.
 */

public class Event implements Serializable {

    public static final String TAG = "Event";

    public String id;
    public String title;
    public String link;
    public String description;
    public String category;
    public String content;
    public String image;
    public String date;
    public String location;
    public String prices;
    public String guid;
    public String schedule;

    public Event() {

    }

    public Event(String title, String link, String description, String guid, String category) {
        this.title = StringEscapeUtils.unescapeXml(title);
        this.link = link;
        this.description = description;
        this.guid = guid;
        this.category = category;
    }
}
