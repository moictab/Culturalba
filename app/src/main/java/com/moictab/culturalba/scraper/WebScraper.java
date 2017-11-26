package com.moictab.culturalba.scraper;

import android.util.Log;

import com.moictab.culturalba.model.Block;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import com.moictab.culturalba.model.Event;

public class WebScraper {

    public static final String TAG = "WebScrapper";

    public static List<Block> scrapList(String response) {

        Document document = Jsoup.parse(response);
        List<Block> blocks = new ArrayList<>();

        try {
            List<Element> elements = document.select(".searchResults").first().children();

            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).children().size() == 0) {
                    elements.remove(i);
                    i--;
                }
            }

            for (Element element : elements) {

                Block block = new Block();
                block.title = element.child(0).html();
                block.events = new ArrayList<>();

                List<Element> events = element.children();
                events.remove(0);

                for (Element contentTypeEvent : events) {
                    Event event = new Event();
                    event.title = contentTypeEvent.select(".titulo").first().child(0).html();
                    event.link = contentTypeEvent.select(".titulo").first().child(0).attr("href");
                    event.dateFrom = contentTypeEvent.select(".fecha").first().html();
                    event.schedule = contentTypeEvent.select(".horario").first().html();
                    event.location = contentTypeEvent.select(".lugar").first().html();

                    block.events.add(event);
                }

                blocks.add(block);
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        return blocks;
    }

    public static Event scrapEvent(String response) {

        Document document = Jsoup.parse(response);
        Event event = new Event();

        event.link = document.baseUri();

        try {
            event.title = document.select(".titolSeccio").text();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            event.imageLink = document.select("#parent-fieldname-text").first().getElementsByTag("img").first().attributes().get("src");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            event.dateFrom = document.select("#parent-fieldname-startDate").attr("title");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            event.dateTo = document.select("#parent-fieldname-endDate").attr("title");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            event.location = document.select(".location").get(0).child(0).ownText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            event.prices = document.select(".prices").get(0).child(0).ownText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            event.schedule = document.select(".schedule").get(0).child(0).ownText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            event.description = document.select("#parent-fieldname-text").text();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }

}
