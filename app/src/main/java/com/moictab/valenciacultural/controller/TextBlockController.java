package com.moictab.valenciacultural.controller;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

/**
 * Created by moict on 28/12/2017.
 */

public class TextBlockController {

    public String[] getTextBlocks(Document document) {

        Elements textBlocks = document.select(".bloque_texto");
        Element textBlock = null;

        for (Element element : textBlocks) {
            if (element.text().contains("FECHA") || element.text().contains("HORARIO") || element.text().contains("PRECIO") || element.text().contains("LUGAR")) {
                textBlock = element;
            }
        }

        if (textBlock != null) {
            return textBlock.html().split("<br>");
        } else {
            return new String[0];
        }
    }
}
