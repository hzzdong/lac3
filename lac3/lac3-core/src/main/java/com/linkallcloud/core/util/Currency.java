package com.linkallcloud.core.util;

import com.linkallcloud.core.lang.Strings;

public class Currency {

    /**
     * 分转换成元
     * 
     * @param fen
     * @return 元
     */
    public static String fen2Yuan(long fen) {
        if (fen <= 0) {
            return "0.00";
        } else if (fen < 10) {// 1-9
            return "0.0" + fen;
        } else if (fen < 100) {// 10-99
            return "0." + fen;
        } else {
            String s = String.valueOf(fen);
            return s.substring(0, s.length() - 2) + "." + s.substring(s.length() - 2);
        }
    }

    /**
     * 把money转换成元的字符串
     * 
     * @param money
     *            整形表示的钱
     * @param exponential
     *            钱被放大的幂指数，通常情况下“元”转化成分的时候（比如5元表示为momey=500），exponential=2。
     *            例如：exponential=4，则5分钱表示为money=500,5元钱表示为money=50000
     * @return 元的字符串，例如：exponential=4，则5分钱表示为money=500，return 0.0500；5元钱表示为money=50000，return 5.0000。
     */
    public static String money2Yuan(long money, int exponential) {
        if (money <= 0L) {
            return Strings.alignLeft("0.", exponential + 2, '0');
        }

        long power = 1;
        for (int i = 1; i <= exponential; i++) {
            power = 10 * power;
            if (money < power) {
                return Strings.alignLeft("0.", exponential - i + 2, '0') + money;
            }
        }

        String s = String.valueOf(money);
        return (new StringBuilder()).append(s.substring(0, s.length() - exponential)).append(".")
                .append(s.substring(s.length() - exponential)).toString();
    }

    public static void main(String[] args) throws Exception {
        long money = 0;
        System.out.println("Money:" + money);
        System.out.println(Currency.money2Yuan(money, 1));
        System.out.println(Currency.money2Yuan(money, 2));
        System.out.println(Currency.money2Yuan(money, 3));
        System.out.println(Currency.money2Yuan(money, 4));
        System.out.println(Currency.money2Yuan(money, 5));
        System.out.println(Currency.money2Yuan(money, 6));
        System.out.println("---------------------------------------");
        money = 5;
        System.out.println("Money:" + money);
        System.out.println(Currency.money2Yuan(money, 1));
        System.out.println(Currency.money2Yuan(money, 2));
        System.out.println(Currency.money2Yuan(money, 3));
        System.out.println(Currency.money2Yuan(money, 4));
        System.out.println(Currency.money2Yuan(money, 5));
        System.out.println(Currency.money2Yuan(money, 6));
        System.out.println("---------------------------------------");
        money = 55;
        System.out.println("Money:" + money);
        System.out.println(Currency.money2Yuan(money, 1));
        System.out.println(Currency.money2Yuan(money, 2));
        System.out.println(Currency.money2Yuan(money, 3));
        System.out.println(Currency.money2Yuan(money, 4));
        System.out.println(Currency.money2Yuan(money, 5));
        System.out.println(Currency.money2Yuan(money, 6));
        System.out.println("---------------------------------------");
        money = 555;
        System.out.println("Money:" + money);
        System.out.println(Currency.money2Yuan(money, 1));
        System.out.println(Currency.money2Yuan(money, 2));
        System.out.println(Currency.money2Yuan(money, 3));
        System.out.println(Currency.money2Yuan(money, 4));
        System.out.println(Currency.money2Yuan(money, 5));
        System.out.println(Currency.money2Yuan(money, 6));
        System.out.println("---------------------------------------");
        money = 5555;
        System.out.println("Money:" + money);
        System.out.println(Currency.money2Yuan(money, 1));
        System.out.println(Currency.money2Yuan(money, 2));
        System.out.println(Currency.money2Yuan(money, 3));
        System.out.println(Currency.money2Yuan(money, 4));
        System.out.println(Currency.money2Yuan(money, 5));
        System.out.println(Currency.money2Yuan(money, 6));
        System.out.println("---------------------------------------");
        money = 55555;
        System.out.println("Money:" + money);
        System.out.println(Currency.money2Yuan(money, 1));
        System.out.println(Currency.money2Yuan(money, 2));
        System.out.println(Currency.money2Yuan(money, 3));
        System.out.println(Currency.money2Yuan(money, 4));
        System.out.println(Currency.money2Yuan(money, 5));
        System.out.println(Currency.money2Yuan(money, 6));
        System.out.println("---------------------------------------");
    }

}
