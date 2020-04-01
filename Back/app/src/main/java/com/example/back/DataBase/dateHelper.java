package com.example.back.DataBase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class dateHelper {
    public static String getTime(){
        System.currentTimeMillis();
        Date date = new Date(); //获取当前的系统时间。
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm") ; //使用了默认的格式创建了一个日期格式化对象。
        String time = dateFormat.format(date); //可以把日期转换转指定格式的字符串
       return time;
    }

}
