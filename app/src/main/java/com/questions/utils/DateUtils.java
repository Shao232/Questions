package com.questions.utils;


/**
 * Created by 11470 on 2017/8/21.
 */

public class DateUtils {



    /**
     * 将输入的时间与当前时间比较
     * @param date1
     * @return
     */
    public static boolean DateCompare24(String date1){
        long currentTime = System.currentTimeMillis();
        long dateTime = MyUtils.getInstance().string2Date("yyyy-MM-dd HH:mm",date1);
        int hour24 = 1000 * 60 * 60 * 24;//一天的时间
        return dateTime - currentTime >= hour24;
    }

    public static boolean DateCompare(String date1){
        long currentTime = System.currentTimeMillis();
        long dateTime =  MyUtils.getInstance().string2Date("yyyy-MM-dd HH:mm",date1);
        return dateTime - currentTime >0;
    }


    /**
     * 比较两个时间大小
     * @param date1   开始时间
     * @param date2   结束时间
     * @return
     */
    public static boolean startDateCompareEndDate(String date1, String date2){
        long date1Time =   MyUtils.getInstance().string2Date("yyyy-MM-dd HH:mm",date1);
        long date2Time = MyUtils.getInstance().string2Date("yyyy-MM-dd HH:mm",date2);
        return date2Time - date1Time >0;
    }

}
