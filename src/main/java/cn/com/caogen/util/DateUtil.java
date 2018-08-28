package cn.com.caogen.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author:huyanqing
 * Date:2018/4/19
 */
public class DateUtil {
    public static String getTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    public static String getDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }
}
