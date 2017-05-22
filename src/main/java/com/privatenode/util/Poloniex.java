package com.privatenode.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianzhiqin on 2017/5/22.
 */
public class Poloniex {

    public static String getValue(String url) {
        String result = "";
        try {
            Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)")
                    .timeout(30000).followRedirects(true).get();
            Elements elements = doc.select("body");
            String json = elements.get(0).childNode(0).toString();
//            System.out.println(elements.get(0).childNode(0));
            result = parseJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }


    public static String getUnixtime() {
        long unixtime = System.currentTimeMillis() / 1000 - 1800;
        System.out.println(unixtime);
        return String.valueOf(unixtime);
    }

    public static String geturl(String coin, String unixtime) {
        String url = "https://poloniex.com/public?command=returnChartData&currencyPair=USDT_" + coin + "&start=" + unixtime + "&end=9999999999&period=300";
//        System.out.println(url);
        return url;
    }

    public static String parseJson(String json) {
        Type type = new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType();
        Gson gson = new Gson();
        ArrayList<Map<String, Object>> list = gson.fromJson(json, type);
        Map<String, Object> map = list.get(list.size() - 1);
        String val = map.get("weightedAverage").toString();
        System.out.println(val);
        return val;
    }

    public static Map<String, String> getPoloniex() {
        double huilv = usdcny();
        Map<String, String> map = new HashMap<>();

        String unixtime = getUnixtime();
        String zec = geturl("ZEC", unixtime);
        String etc = geturl("ETC", unixtime);
        String eth = geturl("ETH", unixtime);
        map.put("zec", Double.parseDouble(getValue(zec)) * huilv + "");
        map.put("etc", Double.parseDouble(getValue(etc)) * huilv + "");
        map.put("eth", Double.parseDouble(getValue(eth)) * huilv + "");
        System.out.println(map);
        return map;
    }

    public static double usdcny() {
        double result = 0d;
        try {

            String url = "http://quote.forex.hexun.com/USDCNY.shtml";
            Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)")
                    .timeout(30000).followRedirects(true).get();
            Elements select = doc.select("span#newprice");
            result = Double.parseDouble(select.text());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        usdcny();
//       getPoloniex();
    }
}
