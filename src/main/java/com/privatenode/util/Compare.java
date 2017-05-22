package com.privatenode.util;

import java.util.Map;

/**
 * Created by qianzhiqin on 2017/5/22.
 */
public class Compare {

    public static void main(String[] args) {
        double init = 10000d;
        Map<String, String> yunbi = Yunbi.getYunbi();
        Map<String, String> poloniex = Poloniex.getPoloniex();
        double etcYunbiPrice = Double.parseDouble(yunbi.get("etc"));
        double ethYunbiPrice = Double.parseDouble(yunbi.get("eth"));
        double zecYunbiPrice = Double.parseDouble(yunbi.get("zec"));

        double etcPoloniexPrice = Double.parseDouble(yunbi.get("etc"));
        double ethPoloniexPrice = Double.parseDouble(yunbi.get("eth"));
        double zecPoloniexPrice = Double.parseDouble(yunbi.get("zec"));
        //方案一
        double etcYunbiBuy = init * (1 - 0.001) / etcYunbiPrice;
        double etcPoniexSell = (etcYunbiBuy - 0.01) * etcPoloniexPrice * (1 - 0.0015);
        double ethPoniexBuy = etcPoniexSell * (1 - 0.0015) / ethPoloniexPrice;
        double ethYunbiSell = ethPoniexBuy * ethYunbiPrice * (1 - 0.001);
        System.out.println("方案1：" + (etcPoniexSell - init));
    }

}
