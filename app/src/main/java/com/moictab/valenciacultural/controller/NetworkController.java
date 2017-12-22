package com.moictab.valenciacultural.controller;

import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moictab.valenciacultural.model.Event;
import com.moictab.valenciacultural.scraper.FeedParser;
import com.moictab.valenciacultural.scraper.WebParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moict on 19/12/2017.
 */

public class NetworkController {

    public static final String TAG = "NetworkController";

    private RequestQueue queue;
    private List<Event> events = new ArrayList<>();
    private int requestCounter = 0;

    public NetworkController(List<Event> events, RequestQueue queue) {
        this.events = events;
        this.queue = queue;
    }

    public StringRequest getEventsRequest(String URL) {

        requestCounter++;

        return new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    events = new FeedParser().parse(response);
                    for (Event event : events) {
                        queue.add(getEventRequest(event));
                        requestCounter++;
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    private StringRequest getEventRequest(final Event event) {
        return new StringRequest(Request.Method.GET, event.link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                WebParser parser = new WebParser(response);
                event.date = parser.getDate();

                try {
                    event.location = parser.getLocation();
                } catch (Exception e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    event.location = "";
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    public void requestFinished() {
        requestCounter--;
    }

    public boolean isRequestsPending() {
        return requestCounter != 0;
    }

    public List<Event> getEvents() {
        return this.events;
    }
}
