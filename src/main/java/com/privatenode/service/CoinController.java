package com.privatenode.service;

import com.google.gson.Gson;
import com.privatenode.dao.MysqlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by qianzhiqin on 2017/5/3.
 */
@Controller
@RequestMapping("/")
public class CoinController {
    private final Logger logger = LoggerFactory.getLogger(CoinController.class);


    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "show.do", method = RequestMethod.POST)
    @ResponseBody
    public String show(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Map<String, List<String>>> etc = query("etc");
        Map<String, Map<String, List<String>>> eth = query("eth");
        Map<String, Map<String, List<String>>> zec = query("zec");
        etc.putAll(eth);
        etc.putAll(zec);
        System.out.println(etc.size());
        return new Gson().toJson(etc);
    }

    private Map<String, Map<String, List<String>>> query(String coin) {
        Map<String, Map<String, List<String>>> map = new HashMap<>();

        List<String> timelist = new ArrayList<>();
        List<String> diffList = new ArrayList<>();
        List<String> hashList = new ArrayList<>();

        try {
            String sql = "SELECT coin ,time ,diff ,hash FROM coin WHERE coin='" + coin + "'  ORDER BY TIME DESC LIMIT 300;";
            ResultSet rs = MysqlHelper.executeQuery(sql, null);
            while (rs.next()) {
                String time = rs.getString(2);
                String diff = rs.getString(3);
                String hash = rs.getString(4);
                timelist.add(time);
                diffList.add(diff);
                hashList.add(hash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        Map<String, List<String>> diff = new HashMap<>();
        Map<String, List<String>> hash = new HashMap<>();
        Collections.reverse(timelist);
        Collections.reverse(diffList);
        Collections.reverse(hashList);
        diff.put("x", timelist);
        diff.put("y", diffList);
        hash.put("x", timelist);
        hash.put("y", hashList);
        map.put(coin + "_diff", diff);
        map.put(coin + "_hash", hash);
        return map;
    }

    public static void main(String[] args) {
//        new CoinController().show();
    }

}
