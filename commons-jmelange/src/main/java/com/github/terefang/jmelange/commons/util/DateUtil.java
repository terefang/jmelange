package com.github.terefang.jmelange.commons.util;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
    static SimpleDateFormat _DATE_TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    static SimpleDateFormat _DATE_TIME_STAMP_FORMAT_SAFE = new SimpleDateFormat("yyyy'_'MM'_'dd'_'HH'_'mm'_'ss");
    
    public static String timestamp()
    {
        return timestamp(getDate());
    }
    
    public static String timestamp(Date _d)
    {
        return _DATE_TIME_STAMP_FORMAT.format(_d);
    }
    
    public static String timestamp(Long _d)
    {
        return _DATE_TIME_STAMP_FORMAT.format(_d);
    }
    
    public static String safestamp()
    {
        return safestamp(getDate());
    }
    
    public static String safestamp(Date _d)
    {
        return _DATE_TIME_STAMP_FORMAT_SAFE.format(_d);
    }
    
    public static String safestamp(Long _d)
    {
        return _DATE_TIME_STAMP_FORMAT_SAFE.format(_d);
    }
    
    @SneakyThrows
    public static Date dateToTime(String _format, String _text)
    {
        SimpleDateFormat _sdf = new SimpleDateFormat(_format);
        return _sdf.parse(_text);
    }

    @SneakyThrows
    public static Long dateToLong(String _format, String _text)
    {
        Date _d = dateToTime(_format, _text);
        return (_d==null) ? null : _d.getTime();
    }

    public static Date getDate()
    {
        return new Date();
    }

    public static Long getDateLong()
    {
        return new Date().getTime();
    }

    public static String timeToDate(String _format, Date _time)
    {
        SimpleDateFormat _sdf = new SimpleDateFormat(_format);
        return _sdf.format(_time);
    }

    public static String timeToDate(String _format, long _time)
    {
        SimpleDateFormat _sdf = new SimpleDateFormat(_format);
        return _sdf.format(new Date(_time));
    }

}
