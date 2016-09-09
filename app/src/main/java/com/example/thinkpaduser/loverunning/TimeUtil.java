package com.example.thinkpaduser.loverunning;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created by ThinkPad User on 2016/8/10.
 */
public class TimeUtil { //方法，把跑步时间格式化成00:00:00形式，
    public static String getRunTime(long runtime){
        int time = (int)(runtime/1000);
        int hour = time/3600;//取商
        int minute = time%3600/60;//取余再取商
        int second = time%60;//取余
        StringBuilder sb = new StringBuilder();//用StringBuil拼接字符串
        //使用NumberFormat格式化数据
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);//数据显示的方式最小是两位
        numberFormat.setMaximumIntegerDigits(2);//最大也是两位，
        if (hour > 0){
            sb.append(numberFormat.format(hour));
            sb.append(":");
        }else{
            sb.append("00:");
        }
        if (minute > 0){
            sb.append(numberFormat.format(minute));
            sb.append(":");
        }else{
            sb.append("00:");
        }
            sb.append(numberFormat.format(second));
        return new String(sb);
    }

    public static String getCurrentDate(){//把时间戳格式化成当前日期字符串
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(System.currentTimeMillis());
    }
    public static String getCurrentMonthAndDay(){
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        return format.format(System.currentTimeMillis());
    }

    public static String getDate(long time){//把时间戳格式化成任意日期字符串
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time);
    }
    public static String getTime(long time){//得到当前时间以小时分钟来计算
        SimpleDateFormat format = new SimpleDateFormat("HH：mm");
        return format.format(time);
    }

}
