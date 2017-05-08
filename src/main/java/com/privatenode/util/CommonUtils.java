package com.privatenode.util;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qianzhiqin on 2017/5/3.
 */
public class CommonUtils {
    public static Map<String, Double> etcInfo() {
        String url = "http://gastracker.io";
        double diff = 0d;
        double hash = 0d;
        Map<String, Double> map = new HashMap<String, Double>();
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)").timeout(30000).followRedirects(true).get();
//            System.out.println(doc);
            Elements diffElements = doc.select("dd:containsOwn(TH)");
            for (Element ele : diffElements) {
                List<Node> nodes = ele.childNodes();
                Node node = ele.childNode(1);
                if ("TH".equals(node.toString().trim())) {
                    String diffStr = ele.childNode(0).childNode(0).toString();
                    System.out.println(diffStr);
                    diff = Double.parseDouble(diffStr.replace(",", ""));
                    System.out.println(diff);
                    map.put("diff", diff);
                }

            }
            Elements hashElements = doc.select("dd:containsOwn(GH/s)");
            for (Element ele : hashElements) {
                List<Node> nodes = ele.childNodes();
                Node node = ele.childNode(1);
                System.out.println(node.toString().trim());
                if ("GH/s".equals(node.toString().trim())) {
                    String hashStr = ele.childNode(0).childNode(0).toString();
                    System.out.println(hashStr);
                    hash = Double.parseDouble(hashStr.replace(",", ""));
                    System.out.println(hash);
                    map.put("hash", hash);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return map;
    }

    public static Map<String, Double> ethInfo() {
        String url = "https://etherscan.io";
        double diff = 0d;
        double hash = 0d;
        Map<String, Double> map = new HashMap<String, Double>();
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)").timeout(30000).followRedirects(true).get();
//            System.out.println(doc);
            Elements diffElements = doc.select("span[title=Average Difficulty]");
            for (Element ele : diffElements) {
                String diffStr = ele.childNode(0).childNode(0).toString();
                System.out.println(diffStr);
//                String th = diffStr.replace("TH", "").trim();
                diff = Double.parseDouble(diffStr.replace("TH", "").replace(",", "").trim());
                map.put("diff", diff);

            }
            Elements hashElements = doc.select("span[title=Avg Hash Rate of the last 5000 Blocks]");
            for (Element ele : hashElements) {
                String hashStr = ele.childNode(0).toString();
                System.out.println(hashStr);
                hash = Double.parseDouble(hashStr.replace("GH/s", "").replace(",", "").trim());
                map.put("hash", hash);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return map;
    }

    public static Map<String, Double> zecInfo() {
        String url = "https://api.zcha.in/v2/mainnet/network";
        double diff = 0d;
        double hash = 0d;
        Map<String, Double> map = new HashMap<String, Double>();
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)").ignoreContentType(true).timeout(30000).followRedirects(true).get();
//            System.out.println(doc);
            Elements elements = doc.select("body");
            for (Element ele : elements) {
                String str = ele.childNode(0).toString();
                System.out.println(str);
                Gson gson = new Gson();
                Map valMap = gson.fromJson(str, Map.class);
                String difficulty = valMap.get("difficulty").toString().replace(",", "");
                String hashrate = valMap.get("hashrate").toString().replace(",", "");
//                System.out.println(difficulty);
//                System.out.println(hashrate);
                diff = parse(difficulty);
                hash = parse(hashrate);

                map.put("diff", diff);
                map.put("hash", hash);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return map;
    }

    public static Double parse(String str) {
        double val = 0d;
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");//��ʽ������
        if (str.indexOf(".") > 0 && str.toUpperCase().indexOf("E") > 0) {//��ѧ������ 1.459305030742E12
            BigDecimal bd = new BigDecimal(str);
            System.out.println(decimalFormat.format(bd));
            val = Double.parseDouble(decimalFormat.format(bd));
        } else {
            BigDecimal bd = new BigDecimal(str);
            val = Double.parseDouble(decimalFormat.format(bd).toString());
        }
        return val;
    }

    public static void main(String[] args) {
        Map<String, Double> etcInfo = zecInfo();
        System.out.println(etcInfo);
//        Double parse = parse("1.052579610813618e+06");
//        System.out.println(parse);
    }
}
