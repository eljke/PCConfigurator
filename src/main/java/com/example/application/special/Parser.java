package com.example.application.special;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.*;


public class Parser {

    private static int totalPages = 0;

    private static final List<Component> componentsList = new ArrayList<>();

    public void parse(String curName, String PARSING_SOURCE) throws IOException {

        final String[] description = new String[1];

        Map<String, String> dictionary = new HashMap<>();
        dictionary.put("Материнская плата", "187");
        dictionary.put("Процессор", "186");
        dictionary.put("Видеокарта", "189");
        dictionary.put("Оперативная память", "188");
        dictionary.put("HDD", "190");
        dictionary.put("SSD", "61");
        dictionary.put("Корпус", "193");
        dictionary.put("Блок питания", "351");
        dictionary.put("Звуковая карта", "192");
        dictionary.put("Охлаждение процессора", "303/pr-7151");

        String value = dictionary.get(curName);

        Document doc = Jsoup.connect(String.format(PARSING_SOURCE + "/list/%s/", value)).get();

        Elements pages = doc.getElementsByAttributeValue("class", "ib page-num");

        String base_url = String.format(PARSING_SOURCE + "/list/%s/", value);
        StringBuilder baseUrls = new StringBuilder();

        baseUrls.append(base_url).append(" ");

        pages.forEach(page -> totalPages = page.childrenSize());

        for (int i = 0; i < totalPages; i++) {
            if (i == 0) {
                continue;
            } else {
                base_url += i;
            }
            baseUrls.append(base_url).append(" ");
            base_url = base_url.substring(0, base_url.length() - 1);
        }

        String[] strings = baseUrls.toString().split(" ");

        for (String possibleUrl : strings) {

            doc = Jsoup.connect(possibleUrl).get();
            Elements table = doc.getElementsByAttributeValue("class", "model-short-block");
            final Element[] shortDescription = new Element[1];

            try {
                table.forEach(element -> {

                    Elements imgSrc = element.getElementsByTag("img");

                    Element urlName = element.child(0).child(0).child(1).child(0).child(0).child(0).child(0).child(0);

                    if (element.child(0).child(0).child(1).child(1).childrenSize() > 1) {
                         shortDescription[0] = element.child(0).child(0).child(1).child(1).child(1);
                    }
                    else {
                        shortDescription[0] = element.child(0).child(0).child(1).child(1).child(0);
                    }
                    Element pricesAndShops = element.child(0).child(0).child(2);
                    Elements priceRanging = pricesAndShops.getElementsByAttributeValue("class", "model-price-range");
                    Element priceRange = priceRanging.get(0).child(0);

                    String image = PARSING_SOURCE + imgSrc.attr("src");
                    String url = PARSING_SOURCE + urlName.attr("href");
                    String name = urlName.text();
                    String price = "от " + priceRange.text();

                    description[0] = "";
                    for (int i = 0; i < shortDescription[0].childrenSize(); i++){
                         description[0] += shortDescription[0].child(i).text().replaceAll(":", ": ") + "\n";
                    }

                    componentsList.add(new Component(image, url, name, price, description[0]));
                });

            } catch (IndexOutOfBoundsException e) {
                assert true;
            }

        }
    }

    public static List<Component> getComponentsList() {
        return componentsList;
    }

    public static void deleteComponentsList(){
        componentsList.clear();
    }
}