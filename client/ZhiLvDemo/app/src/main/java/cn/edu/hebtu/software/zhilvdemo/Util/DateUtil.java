package cn.edu.hebtu.software.zhilvdemo.Util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @ProjectName:    ZhiLv
 * @Description:    日期和字符串之间转换
 * @Author:         张璐婷
 * @CreateDate:     2021/1/19  15:25
 * @Version:        1.0
 */
public class DateUtil {
    private static SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat formatTime2 =  new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat formatMonthDayTime =  new SimpleDateFormat("MM-dd HH:mm");
    private static SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date getTime(String str) {
        try {
            return formatTime.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDate(String str){
        try {
            return formatDate.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateTime(String str){
        try {
            return formatDateTime.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimeStr(Date date) {
        return formatTime.format(date);
    }

    public static String getDateStr(Date date){
        if(null != date) {
            return formatDate.format(date);
        }else{
            return null;
        }
    }

    public static String getDateTimeStr(Date date){
        if(null != date) {
            return formatDateTime.format(date);
        }else{
            return null;
        }
    }

    public static String showTime(Date date){
        Calendar startCalendar = Calendar.getInstance(Locale.CHINA);
        startCalendar.setTime(date);
        Calendar endCalendar = Calendar.getInstance(Locale.CHINA);
        endCalendar.setTime(new Date(System.currentTimeMillis()));
        int sYear = startCalendar.get(Calendar.YEAR);
        int sMonth = startCalendar.get(Calendar.MONTH);
        int sDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        int eYear = endCalendar.get(Calendar.YEAR);
        int eMonth = endCalendar.get(Calendar.MONTH);
        int eDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        String str = null;
        if(sYear < eYear){  //不是同一年
           str = formatDate.format(date);
        }else if(sMonth < eMonth){ //不是同一月
            str = formatMonthDayTime.format(date);
        }else if(sDay < eDay){ //不是同一天
            int k = eDay - sDay;
            if(k == 2){
                str = "前天"+formatTime2.format(date);
            }else if(k == 1){
                str = "昨天"+formatTime2.format(date);
            }else{
                str =  formatMonthDayTime.format(date);
            }
        }else {
            str = formatTime2.format(date);
        }
        return str;
    }

}
