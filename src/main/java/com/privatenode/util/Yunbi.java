package com.privatenode.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianzhiqin on 2017/5/22.
 */
public class Yunbi {
    public static Map<String, String> getYunbi() {
        Map<String, String> map = new HashMap<>();
//        Map<String, String> map = new HashMap<>();
        String url = "https://yunbi.com/api/v2/tickers.json";
        try {
            Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)")
                    .timeout(30000).followRedirects(true).get();
            System.out.println(doc);
            Elements elements = doc.select("body");
            String json = elements.get(0).childNode(0).toString();
            System.out.println(elements.get(0).childNode(0));
           map = parseJson(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return map;
    }

    public static Map<String, String> parseJson(String json) {
        Map<String, String> result = new HashMap<>();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, type);
        String ethcny = ((Map<String, Object>) ((Map<String, Object>) map.get("ethcny")).get("ticker")).get("last").toString();
        String etccny = ((Map<String, Object>) ((Map<String, Object>) map.get("etccny")).get("ticker")).get("last").toString();
        String zeccny = ((Map<String, Object>) ((Map<String, Object>) map.get("zeccny")).get("ticker")).get("last").toString();
        result.put("eth", ethcny);
        result.put("etc", etccny);
        result.put("zec", zeccny);
        System.out.println(result);
        return result;

    }

    public static void main(String[] args) {
        getYunbi();
    }
}
