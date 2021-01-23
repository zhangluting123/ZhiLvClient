package cn.edu.hebtu.software.zhilvdemo.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ProjectName:    ZhiLv
 * @Description:    日期和字符串之间转换
 * @Author:         张璐婷
 * @CreateDate:     2021/1/19  15:25
 * @Version:        1.0
 */
public class DateUtil {
    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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

}
