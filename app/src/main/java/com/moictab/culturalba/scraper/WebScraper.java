package com.moictab.culturalba.scraper;

import com.moictab.culturalba.model.Block;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import com.moictab.culturalba.model.Event;

public class WebScraper {

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

                List<Element> eventos = element.children();
                eventos.remove(0);

                for (Element contentTypeEvento : eventos) {
                    Event event = new Event();
                    event.title = contentTypeEvento.select(".titulo").first().child(0).html();
                    event.link = contentTypeEvento.select(".titulo").first().child(0).attr("href");
                    event.dateFrom = contentTypeEvento.select(".fecha").first().html();
                    event.horario = contentTypeEvento.select(".horario").first().html();
                    event.location = contentTypeEvento.select(".lugar").first().html();

                    block.events.add(event);
                }

                blocks.add(block);
            }
        } catch (Exception ex) {
            return blocks;
        }

        return blocks;
    }

    public static Event scrapEvento(String response) {

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
            event.precios = document.select(".precios").get(0).child(0).ownText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            event.horario = document.select(".horario").get(0).child(0).ownText();
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
