package com.yqm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String nowTime = sdf.format(new Date());
        String index = nowTime.substring(0, 1);
        System.out.println(nowTime + "  " + index);
    }

}
