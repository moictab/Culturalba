package com.moictab.valenciacultural.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.moictab.valenciacultural.model.Event;
import com.moictab.valenciacultural.scraper.FeedParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moict on 14/12/2017.
 */

public class EventsController {

    public static final String TAG = "EventsController";

    private Context context;
    private RequestQueue queue;

    public EventsController(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public void completeEvent(Event event) {

        StringRequest request = new StringRequest(Request.Method.GET, event.link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.networkResponse.toString());
            }
        });
    }

    public List<Event> getEvents(String URL) {

        List<Event> entries = new ArrayList<>();
        FeedParser feedParser = new FeedParser();

        try {
            entries = feedParser.parse(URL);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries;
    }
}
