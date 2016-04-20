package parser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateParser {

    public static List<DateTime> parse(String input) {

        List<DateTime> fechas = new ArrayList<>();

        String pattern = "dd-MM-yyyy";

        List<String> words = new ArrayList<>(Arrays.asList(input.split("\\s+")));

        for (int i = 0; i < words.size(); i++) {
            if (!words.get(i).matches(".*\\d+.*") && words.get(i).length() < 10) {
                words.remove(i);
                i++;
            }
        }

        for (int i = 0; i < words.size(); i++) {

            words.get(i).replace("/", "-");

            if (words.get(i).toLowerCase().contains("enero")) {
                words.set(i, words.get(i).toLowerCase().replace("enero", "01"));
            }

            if (words.get(i).toLowerCase().contains("febrero")) {
                words.set(i, words.get(i).toLowerCase().replace("febrero", "02"));
            }

            if (words.get(i).toLowerCase().contains("marzo")) {
                words.set(i, words.get(i).toLowerCase().replace("marzo", "03"));
            }

            if (words.get(i).toLowerCase().contains("abril")) {
                words.set(i, words.get(i).toLowerCase().replace("abril", "04"));
            }

            if (words.get(i).toLowerCase().contains("mayo")) {
                words.set(i, words.get(i).toLowerCase().replace("mayo", "05"));
            }

            if (words.get(i).toLowerCase().contains("junio")) {
                words.set(i, words.get(i).toLowerCase().replace("junio", "06"));
            }

            if (words.get(i).toLowerCase().contains("julio")) {
                words.set(i, words.get(i).toLowerCase().replace("julio", "07"));
            }

            if (words.get(i).toLowerCase().contains("agosto")) {
                words.set(i, words.get(i).toLowerCase().replace("agosto", "08"));
            }

            if (words.get(i).toLowerCase().contains("septiembre")) {
                words.set(i, words.get(i).toLowerCase().replace("septiembre", "09"));
            }

            if (words.get(i).toLowerCase().contains("octubre")) {
                words.set(i, words.get(i).toLowerCase().replace("octubre", "10"));
            }

            if (words.get(i).toLowerCase().contains("noviembre")) {
                words.set(i, words.get(i).toLowerCase().replace("noviembre", "11"));
            }

            if (words.get(i).toLowerCase().contains("diciembre")) {
                words.set(i, words.get(i).toLowerCase().replace("diciembre", "12"));
            }

            fechas.add(DateTime.parse(input, DateTimeFormat.forPattern(pattern)));

        }

        return fechas;

    }

}
