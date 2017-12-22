package com.moictab.valenciacultural.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {
    public String title;
    public List<Event> events = new ArrayList<>();

    public Block(String category, Event event) {
        this.title = category;
        this.events.add(event);
    }
}
