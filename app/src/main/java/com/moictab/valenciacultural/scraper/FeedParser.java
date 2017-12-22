package com.moictab.valenciacultural.scraper;

import android.net.ParseException;
import android.util.Log;
import android.util.Xml;

import com.moictab.valenciacultural.model.Event;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeedParser {

    public static final String TAG = "FeedParser";

    private static final int TAG_DESCRIPTION = 1;
    private static final int TAG_TITLE = 2;
    private static final int TAG_LINK = 3;
    private static final int TAG_CATEGORY = 4;

    private static final String ns = null;

    public List<Event> parse(String input) throws XmlPullParserException, IOException, ParseException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new ByteArrayInputStream(input.getBytes("iso-8859-1")), null);
        Log.d(TAG, parser.getInputEncoding());
        parser.nextTag();
        return readFeed(parser);
    }

    private List<Event> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {

        List<Event> entries = new ArrayList<>();

        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName() != null && parser.getName().equals("item")) {
                    entries.add(readEntry(parser));
                }
            }
            parser.next();
        }

        return entries;
    }

    private Event readEntry(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String category = null;
        String title = null;
        String link = null;
        String description = null;
        String guid = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            switch (parser.getName()) {
                case "title":
                    title = readTag(parser, TAG_TITLE);
                    break;
                case "link":
                    link = readTag(parser, TAG_LINK);
                    break;
                case "description":
                    description = readTag(parser, TAG_DESCRIPTION);
                    break;
                case "category":
                    category = readTag(parser, TAG_CATEGORY);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new Event(title, link, description, guid, category);
    }

    private String readTag(XmlPullParser parser, int tagType) throws IOException, XmlPullParserException {

        switch (tagType) {

            case TAG_TITLE:
                return readBasicTag(parser, "title");
            case TAG_LINK:
                return readBasicTag(parser, "link");
            case TAG_DESCRIPTION:
                return readBasicTag(parser, "description");
            case TAG_CATEGORY:
                return readBasicTag(parser, "category");
            default:
                throw new IllegalArgumentException("Unknown tag type: " + tagType);
        }
    }

    private String readBasicTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return result;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;

        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}