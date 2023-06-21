package com.zhangjun.quyi.utils;

import com.zhangjun.quyi.constans.PressureConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    private static final String DATA_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss SS";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATA_TIME_FORMAT);
    private static final Integer DEFAULT_APPEND_NUMBER  = 10;

    /**
     * 时间戳转String
     * @param date
     * @return
     */
    public static String dateForString(Date date){
        return simpleDateFormat.format(date);
    }

    /**
     * 字符串转Date
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date stringForDate(String dateStr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
    }


    /**
     * 生成事务id、不好含毫秒
     * @return
     */
    public static long getTransactionSecondId(){
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder(String.valueOf(calendar.get(Calendar.YEAR)));
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        sb.append( month<DEFAULT_APPEND_NUMBER ? PressureConstant.ZERO_STR +String.valueOf(month) : String.valueOf(month))
                .append(day<DEFAULT_APPEND_NUMBER ? PressureConstant.ZERO_STR +String.valueOf(day) : String.valueOf(day))
                .append(hour<DEFAULT_APPEND_NUMBER ? PressureConstant.ZERO_STR +String.valueOf(hour) : String.valueOf(hour))
                .append(minute<DEFAULT_APPEND_NUMBER ? PressureConstant.ZERO_STR +String.valueOf(minute) : String.valueOf(minute))
                .append(second<DEFAULT_APPEND_NUMBER ? PressureConstant.ZERO_STR +String.valueOf(second) : String.valueOf(second));
        return Long.valueOf(sb.toString());
    }

    /**
     * 生成事务id、包含毫秒
     * @return
     */
    public static long getTransactionMillisecondId(){
        String substring = dateForString(new Date());
        return Long.valueOf(getTransactionSecondId()
                + substring.substring(substring.length()-3));
    }


}
