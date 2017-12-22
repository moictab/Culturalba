package com.moictab.valenciacultural.scraper;

import android.util.Log;

import com.moictab.valenciacultural.model.Event;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebParser {

    public static final String TAG = "WebScrapper";

    private Document document;

    public WebParser(String response) {
        this.document = Jsoup.parse(response);
    }

    public Event scrapEvent() {

        Event event = new Event();

        event.location = getLocation();
        event.title = getTitle();
        event.link = document.baseUri();
        event.image = getImage();
        event.date = getDate();
        event.description = getDescription();

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
            Elements elements = document.select(".bloque_texto");
            StringBuilder builder = new StringBuilder();

            for (Element element : elements) {
                if (!element.html().contains("FECHA")
                        && !element.html().contains("LUGAR")
                        && !element.html().contains("PRECIO")
                        && !element.html().contains("HORARIO")) {
                    builder.append(element.text());
                }
            }

            return builder.toString();

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            return "";
        }
    }

    public String getPrices() {
        return "";
    }

    public String getDate() {

        try {
            Elements elements = document.select(".bloque_texto");
            Element dateElement = null;

            for (Element element : elements) {
                if (element.text().contains("FECHA")) {
                    dateElement = element;
                }
            }

            if (dateElement != null) {
                String text = dateElement.text();
                return text.substring(text.indexOf("FECHA") + 7, text.indexOf("."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
