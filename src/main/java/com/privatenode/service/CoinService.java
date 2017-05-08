package com.privatenode.service;

import com.privatenode.dao.MysqlHelper;
import com.privatenode.util.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by qianzhiqin on 2017/5/3.
 */
public class CoinService {
    public void spider(String time) {
        Map<String, Double> etcInfo = CommonUtils.etcInfo();
        insert(etcInfo, "etc", time);
        Map<String, Double> ethInfo = CommonUtils.ethInfo();
        insert(ethInfo, "eth", time);
        Map<String, Double> zecInfo = CommonUtils.zecInfo();
        insert(zecInfo, "zec", time);
    }

    public boolean check(Map<String, Double> map) {
        boolean flag = true;
        for (String str : map.keySet()) {
            if (map.get(str) == 0) {
                flag = false;
            }
        }
        return flag;
    }

    public void insert(Map<String, Double> map, String coin, String time) {
        if (check(map)) {
            Double diff = map.get("diff");
            Double hash = map.get("hash");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String time = sdf.format(new Date());
            String date = time.substring(0, 10);
            String[] parameters = new String[2];
            parameters[0] = diff.toString();
            parameters[1] = hash.toString();
            String sql = "insert into coin (coin, time,date, diff,hash) values ('" + coin + "', '" + time + "','" + date + "', ?,?)";
            System.out.println(sql);
            MysqlHelper.executeUpdate(sql, parameters);
        }
    }

    public static String changeTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        int hour = Integer.parseInt(time.substring(11, 13));
        int hourChange = hour % 2 == 0 ? hour : hour - 1;
        String hourChangeStr = hourChange+"";
        if(hourChange<10){
            hourChangeStr="0"+hourChange;
        }
        System.out.println(hourChangeStr);
        time = time.substring(0, 11) + hourChangeStr + ":00:00";
        System.out.println(time);
        return time;

    }

    public static void main(String[] args) {
        String time = changeTime();
        new CoinService().spider(time);
//        System.out.println(hour);
//        new CoinService().spider();
    }
}
