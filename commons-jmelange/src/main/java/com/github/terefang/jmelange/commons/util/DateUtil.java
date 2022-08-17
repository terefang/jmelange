package com.github.terefang.jmelange.commons.util;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
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
