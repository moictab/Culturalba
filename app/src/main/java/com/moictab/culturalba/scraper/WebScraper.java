package com.moictab.culturalba.scraper;

import com.moictab.culturalba.model.Block;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import com.moictab.culturalba.model.Evento;

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
                block.eventos = new ArrayList<>();

                List<Element> eventos = element.children();
                eventos.remove(0);

                for (Element contentTypeEvento : eventos) {
                    Evento evento = new Evento();
                    evento.title = contentTypeEvento.select(".titulo").first().child(0).html();
                    evento.link = contentTypeEvento.select(".titulo").first().child(0).attr("href");
                    evento.dateFrom = contentTypeEvento.select(".fecha").first().html();
                    evento.horario = contentTypeEvento.select(".horario").first().html();
                    evento.location = contentTypeEvento.select(".lugar").first().html();

                    block.eventos.add(evento);
                }

                blocks.add(block);
            }
        } catch (Exception ex) {
            return blocks;
        }

        return blocks;
    }

    public static Evento scrapEvento(String response) {

        Document document = Jsoup.parse(response);
        Evento evento = new Evento();

        evento.link = document.baseUri();

        try {
            evento.title = document.select(".titolSeccio").text();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            evento.imageLink = document.select("#parent-fieldname-text").first().getElementsByTag("img").first().attributes().get("src");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            evento.dateFrom = document.select("#parent-fieldname-startDate").attr("title");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            evento.dateTo = document.select("#parent-fieldname-endDate").attr("title");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            evento.location = document.select(".location").get(0).child(0).ownText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            evento.precios = document.select(".precios").get(0).child(0).ownText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            evento.horario = document.select(".horario").get(0).child(0).ownText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            evento.description = document.select("#parent-fieldname-text").text();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return evento;
    }

}
