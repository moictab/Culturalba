package com.moictab.valenciacultural.scraper;

import android.util.Log;

import com.moictab.valenciacultural.controller.TextBlockController;
import com.moictab.valenciacultural.model.Event;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class EventParser {

    public static final String TAG = "WebScrapper";

    private Document document;
    private HashMap<String, String> blocks;

    public EventParser(String response) {
        this.document = Jsoup.parse(response);
        blocks = getParsedBlocks(new TextBlockController().getTextBlocks(document));
    }

    public Event scrapEvent() {

        Event event = new Event();

        event.location = getLocation();
        event.title = getTitle();
        event.link = document.baseUri();
        event.image = getImage();
        event.date = getDate();
        event.description = getDescription();
        event.schedule = getSchedule();
        event.prices = getPrices();

        return event;
    }

    public String getTitle() {
        try {
            String text = document.select(".bloque_subtitulo").first().html();
            return text.substring(text.indexOf("<br>") + 4, text.length());
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            return "";
        }
    }

    public String getLocation() {
        StringBuilder builder = new StringBuilder();

        String text = document.select(".bloque_subtitulo").first().html();
        builder.append(text.substring(0, text.indexOf("<br>")));
        builder.append(", ");

        Elements elements = document.select(".elementoLista");
        builder.append(elements.get(0).text());
        builder.append(", ");
        builder.append(elements.get(1).text());

        return builder.toString();
    }

    public String getImage() {

        try {
            Elements elements = document.select(".imagenPie");
            return "www.valencia.es" + elements.get(1).getElementsByTag("img").first().attributes().get("src");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getDescription() {
        try {
            return document.select(".bloque_texto").last().text();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            return "";
        }
    }

    public String getPrices() {

        if (blocks.containsKey("PRECIO")) {
            return blocks.get("PRECIO");
        }

        if (blocks.containsKey("PRECIOS")) {
            return blocks.get("PRECIOS");
        }

        return "";
    }

    public String getDate() {

        if (blocks.containsKey("FECHA")) {
            return blocks.get("FECHA");
        }

        if (blocks.containsKey("FECHAS")) {
            return blocks.get("FECHAS");
        }

        return "";
    }

    public String getSchedule() {

        if (blocks.containsKey("HORARIO")) {
            return blocks.get("HORARIO");
        }

        if (blocks.containsKey("HORARIOS")) {
            return blocks.get("HORARIOS");
        }

        return "";
    }

    public HashMap<String, String> getParsedBlocks(String[] blocks) {

        HashMap<String, String> map = new HashMap<>();

        if (blocks == null || blocks.length == 0) {
            return map;
        }

        try {
            for (String block : blocks) {

                String key = block.substring(8, block.indexOf("</strong>"));
                String value = block.substring(block.indexOf("</strong>") + 10, block.length());
                map.put(key, value);
            }
        } catch (Exception ex) {
            return map;
        }

        return map;
    }

}
