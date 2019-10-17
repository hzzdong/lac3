package com.linkallcloud.core.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateOrderId {
    private static StringBuffer id = new StringBuffer("000001");
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    private static final String STR_FORMAT = "0000";

    /**
     * 生成订单号
     */
    public static String getOrderId() {

        StringBuffer sb = new StringBuffer(sdf.format(new Date()));
        sb.append((int) (Math.random() * 10));
        sb.append((int) (Math.random() * 10));
        int b = Integer.parseInt(id.toString());
        b += 1;
        String s = b + "";
        switch (s.length()) {
        case 1:
            s = "00000" + s;
            break;
        case 2:
            s = "0000" + s;
            break;
        case 3:
            s = "000" + s;
            break;
        case 4:
            s = "00" + s;
            break;
        case 5:
            s = "0" + s;
            break;
        }
        id.replace(1, 7, s);
        sb.append(id);
        sb.append((int) (Math.random() * 10));
        if ("999999".equals(s)) {
            id.replace(1, 7, "000001");
        }
        return sb.toString();
    }
    
    /**
     * 生成订单号
     */
    public static String formatRankNumber(Integer rankNmuber) {
        rankNmuber++;
        DecimalFormat df = new DecimalFormat(STR_FORMAT);  
        return df.format(rankNmuber);
    }
    
}