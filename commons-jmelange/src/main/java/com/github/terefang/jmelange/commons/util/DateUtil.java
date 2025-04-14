package com.github.terefang.jmelange.commons.util;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
    
    @SneakyThrows public static final Long toLong(Date _v) { return _v.getTime(); }
    
    @SneakyThrows public static final Long toLong(String _v) { return _DATE_TIME_STAMP_FORMAT.parse(_v).getTime(); }
    
    @SneakyThrows public static final Long toLong(Instant _v) { return _v.toEpochMilli(); }
    
    @SneakyThrows public static final Long toLong(LocalDate _v) { return toInstant(_v).toEpochMilli(); }
    
    @SneakyThrows public static final Long toLong(LocalDateTime _v) { return toInstant(_v).toEpochMilli(); }
    
    @SneakyThrows public static final Long toLong(ZonedDateTime _v) { return _v.toInstant().toEpochMilli(); }
    
    @SneakyThrows public static final Long toLong(OffsetDateTime _v) { return _v.toInstant().toEpochMilli(); }
    
    @SneakyThrows public static final Date toDate(Long _v) { return new Date(_v); }
    
    @SneakyThrows public static final Date toDate(String _v) { return _DATE_TIME_STAMP_FORMAT.parse(_v); }
    
    @SneakyThrows public static final Date toDate(Instant _v) { return new Date(_v.toEpochMilli()); }
    
    @SneakyThrows public static final Date toDate(LocalDate _v) { return new Date(toLong(_v)); }
    
    @SneakyThrows public static final Date toDate(LocalDateTime _v) { return new Date(toLong(_v)); }
    
    @SneakyThrows public static final Date toDate(ZonedDateTime _v) { return new Date(toLong(_v)); }
    
    @SneakyThrows public static final Date toDate(OffsetDateTime _v) { return new Date(toLong(_v)); }
    
    @SneakyThrows public static final String toString(Long _v) { return _DATE_TIME_STAMP_FORMAT.format(new Date(_v)); }
    
    @SneakyThrows public static final String toString(Date _v) { return _DATE_TIME_STAMP_FORMAT.format(_v); }
    
    @SneakyThrows public static final String toString(Instant _v) { return _DATE_TIME_STAMP_FORMAT.format(new Date(_v.toEpochMilli())); }
    
    @SneakyThrows public static final String toString(LocalDate _v) { return _DATE_TIME_STAMP_FORMAT.format(new Date(_v.getYear(), _v.getMonthValue(), _v.getDayOfMonth())); }
    
    @SneakyThrows public static final String toString(LocalDateTime _v) { return _DATE_TIME_STAMP_FORMAT.format(new Date(_v.getYear(), _v.getMonthValue(), _v.getDayOfMonth(), _v.getHour(), _v.getMinute(), _v.getSecond())); }
    
    @SneakyThrows public static final String toString(ZonedDateTime _v) { return _DATE_TIME_STAMP_FORMAT.format(new Date(_v.toInstant().toEpochMilli())); }
    
    @SneakyThrows public static final String toString(OffsetDateTime _v) { return _DATE_TIME_STAMP_FORMAT.format(new Date(_v.toInstant().toEpochMilli())); }
    
    @SneakyThrows public static final Instant toInstant(Long _v) { return Instant.ofEpochMilli(_v); }
    
    @SneakyThrows public static final Instant toInstant(Date _v) { return _v.toInstant(); }
    
    @SneakyThrows public static final Instant toInstant(String _v) { return _DATE_TIME_STAMP_FORMAT.parse(_v).toInstant(); }
    
    @SneakyThrows public static final Instant toInstant(LocalDate _v) { return new Date(_v.getYear(), _v.getMonthValue(), _v.getDayOfMonth()).toInstant(); }
    
    @SneakyThrows public static final Instant toInstant(LocalDateTime _v) { return new Date(_v.getYear(), _v.getMonthValue(), _v.getDayOfMonth(), _v.getHour(), _v.getMinute(), _v.getSecond()).toInstant(); }
    
    @SneakyThrows public static final Instant toInstant(ZonedDateTime _v) { return _v.toInstant(); }
    
    @SneakyThrows public static final Instant toInstant(OffsetDateTime _v) { return _v.toInstant(); }
    
    @SneakyThrows public static final LocalDate toLocalDate(Long _v) { return toLocalDate(new Date(_v)); }
    
    @SneakyThrows public static final LocalDate toLocalDate(Date _v) { return LocalDate.of(1900+_v.getYear(), _v.getMonth()+1, _v.getDate()); }
    
    @SneakyThrows public static final LocalDate toLocalDate(String _v) { return toLocalDate(_DATE_TIME_STAMP_FORMAT.parse(_v)); }
    
    @SneakyThrows public static final LocalDate toLocalDate(Instant _v) { return toLocalDate(new Date(_v.toEpochMilli())); }
    
    @SneakyThrows public static final LocalDate toLocalDate(LocalDateTime _v) { return null; }
    
    @SneakyThrows public static final LocalDate toLocalDate(ZonedDateTime _v) { return null; }
    
    @SneakyThrows public static final LocalDate toLocalDate(OffsetDateTime _v) { return null; }
    
    @SneakyThrows public static final LocalDateTime toLocalDateTime(Long _v) { return toLocalDateTime(new Date(_v)); }
    
    @SneakyThrows public static final LocalDateTime toLocalDateTime(Date _v) { return LocalDateTime.of(1900+_v.getYear(), _v.getMonth()+1, _v.getDate(), _v.getHours(), _v.getMinutes(), _v.getSeconds()); }
    
    @SneakyThrows public static final LocalDateTime toLocalDateTime(String _v) { return toLocalDateTime(_DATE_TIME_STAMP_FORMAT.parse(_v)); }
    
    @SneakyThrows public static final LocalDateTime toLocalDateTime(Instant _v) { return toLocalDateTime(new Date(_v.toEpochMilli())); }
    
    @SneakyThrows public static final LocalDateTime toLocalDateTime(LocalDate _v) { return LocalDateTime.of(_v.getYear(), _v.getMonthValue(), _v.getDayOfMonth(), 0, 0); }
    
    @SneakyThrows public static final LocalDateTime toLocalDateTime(ZonedDateTime _v) { return _v.toLocalDateTime(); }
    
    @SneakyThrows public static final LocalDateTime toLocalDateTime(OffsetDateTime _v) { return _v.toLocalDateTime(); }
    
    @SneakyThrows public static final ZonedDateTime toZonedDateTime(Long _v) { return toZonedDateTime(new Date(_v)); }
    
    @SneakyThrows public static final ZonedDateTime toZonedDateTime(Date _v) { return ZonedDateTime.of(1900+_v.getYear(), _v.getMonth()+1, _v.getDate(), _v.getHours(), _v.getMinutes(), _v.getSeconds(), 0, ZoneId.systemDefault()); }
    
    @SneakyThrows public static final ZonedDateTime toZonedDateTime(String _v) { return toZonedDateTime(_DATE_TIME_STAMP_FORMAT.parse(_v)); }
    
    @SneakyThrows public static final ZonedDateTime toZonedDateTime(Instant _v) { return ZonedDateTime.ofInstant(_v , ZoneId.systemDefault()); }
    
    @SneakyThrows public static final ZonedDateTime toZonedDateTime(LocalDate _v) { return ZonedDateTime.of(_v.getYear(), _v.getMonthValue(), _v.getDayOfMonth(), 0, 0, 0, 0, ZoneId.systemDefault()); }
    
    @SneakyThrows public static final ZonedDateTime toZonedDateTime(LocalDateTime _v) { return ZonedDateTime.of(_v.getYear(), _v.getMonthValue(), _v.getDayOfMonth(), _v.getHour(), _v.getMinute(), _v.getSecond(), 0, ZoneId.systemDefault()); }
    
    @SneakyThrows public static final ZonedDateTime toZonedDateTime(OffsetDateTime _v) { return _v.toZonedDateTime(); }
    
    @SneakyThrows public static final OffsetDateTime toOffsetDateTime(Long _v) { return toZonedDateTime(_v).toOffsetDateTime(); }
    
    @SneakyThrows public static final OffsetDateTime toOffsetDateTime(Date _v) { return toZonedDateTime(_v).toOffsetDateTime(); }
    
    @SneakyThrows public static final OffsetDateTime toOffsetDateTime(String _v) { return toZonedDateTime(_v).toOffsetDateTime(); }
    
    @SneakyThrows public static final OffsetDateTime toOffsetDateTime(Instant _v) { return toZonedDateTime(_v).toOffsetDateTime(); }
    
    @SneakyThrows public static final OffsetDateTime toOffsetDateTime(LocalDate _v) { return toZonedDateTime(_v).toOffsetDateTime(); }
    
    @SneakyThrows public static final OffsetDateTime toOffsetDateTime(LocalDateTime _v) { return toZonedDateTime(_v).toOffsetDateTime(); }
    
    @SneakyThrows public static final OffsetDateTime toOffsetDateTime(ZonedDateTime _v) { return _v.toOffsetDateTime(); }
    
    @SneakyThrows public static final Long addSeconds(Long _v, int _n) { return toOffsetDateTime(_v).plusSeconds(_n).toInstant().toEpochMilli(); }
    
    @SneakyThrows public static final Long addMinutes(Long _v, int _n) { return toOffsetDateTime(_v).plusMinutes(_n).toInstant().toEpochMilli(); }
    
    @SneakyThrows public static final Long addHours(Long _v, int _n) { return toOffsetDateTime(_v).plusHours(_n).toInstant().toEpochMilli(); }
    
    @SneakyThrows public static final Long addDays(Long _v, int _n) { return toOffsetDateTime(_v).plusDays(_n).toInstant().toEpochMilli(); }
    
    @SneakyThrows public static final Long addMonths(Long _v, int _n) { return toOffsetDateTime(_v).plusMonths(_n).toInstant().toEpochMilli(); }
    
    @SneakyThrows public static final Long addYears(Long _v, int _n) { return toOffsetDateTime(_v).plusYears(_n).toInstant().toEpochMilli(); }
    
    @SneakyThrows public static final Date addSeconds(Date _v, int _n) { return toDate(toOffsetDateTime(_v).plusSeconds(_n)); }
    
    @SneakyThrows public static final Date addMinutes(Date _v, int _n) { return toDate(toOffsetDateTime(_v).plusMinutes(_n)); }
    
    @SneakyThrows public static final Date addHours(Date _v, int _n) { return toDate(toOffsetDateTime(_v).plusHours(_n)); }
    
    @SneakyThrows public static final Date addDays(Date _v, int _n) { return toDate(toOffsetDateTime(_v).plusDays(_n)); }
    
    @SneakyThrows public static final Date addMonths(Date _v, int _n) { return toDate(toOffsetDateTime(_v).plusMonths(_n)); }
    
    @SneakyThrows public static final Date addYears(Date _v, int _n) { return toDate(toOffsetDateTime(_v).plusYears(_n)); }
    
    @SneakyThrows public static final String addSeconds(String _v, int _n) { return toString(toOffsetDateTime(_v).plusSeconds(_n)); }
    
    @SneakyThrows public static final String addMinutes(String _v, int _n) { return toString(toOffsetDateTime(_v).plusMinutes(_n)); }
    
    @SneakyThrows public static final String addHours(String _v, int _n) { return toString(toOffsetDateTime(_v).plusHours(_n)); }
    
    @SneakyThrows public static final String addDays(String _v, int _n) { return toString(toOffsetDateTime(_v).plusDays(_n)); }
    
    @SneakyThrows public static final String addMonths(String _v, int _n) { return toString(toOffsetDateTime(_v).plusMonths(_n)); }
    
    @SneakyThrows public static final String addYears(String _v, int _n) { return toString(toOffsetDateTime(_v).plusYears(_n)); }
    
    @SneakyThrows public static final Instant addSeconds(Instant _v, int _n) { return null; }
    
    @SneakyThrows public static final Instant addMinutes(Instant _v, int _n) { return null; }
    
    @SneakyThrows public static final Instant addHours(Instant _v, int _n) { return null; }
    
    @SneakyThrows public static final Instant addDays(Instant _v, int _n) { return null; }
    
    @SneakyThrows public static final Instant addMonths(Instant _v, int _n) { return null; }
    
    @SneakyThrows public static final Instant addYears(Instant _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDate addSeconds(LocalDate _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDate addMinutes(LocalDate _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDate addHours(LocalDate _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDate addDays(LocalDate _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDate addMonths(LocalDate _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDate addYears(LocalDate _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDateTime addSeconds(LocalDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDateTime addMinutes(LocalDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDateTime addHours(LocalDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDateTime addDays(LocalDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDateTime addMonths(LocalDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final LocalDateTime addYears(LocalDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final ZonedDateTime addSeconds(ZonedDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final ZonedDateTime addMinutes(ZonedDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final ZonedDateTime addHours(ZonedDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final ZonedDateTime addDays(ZonedDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final ZonedDateTime addMonths(ZonedDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final ZonedDateTime addYears(ZonedDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final OffsetDateTime addSeconds(OffsetDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final OffsetDateTime addMinutes(OffsetDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final OffsetDateTime addHours(OffsetDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final OffsetDateTime addDays(OffsetDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final OffsetDateTime addMonths(OffsetDateTime _v, int _n) { return null; }
    
    @SneakyThrows public static final OffsetDateTime addYears(OffsetDateTime _v, int _n) { return null; }
    
    
    @SneakyThrows public static final Long setStartOfSecond(Long _v) { return toLong(toInstant(_v).truncatedTo(ChronoUnit.SECONDS)); }
    
    @SneakyThrows public static final Long setStartOfMinute(Long _v) { return toLong(toInstant(_v).truncatedTo(ChronoUnit.MINUTES)); }
    
    @SneakyThrows public static final Long setStartOfHour(Long _v) { return toLong(toInstant(_v).truncatedTo(ChronoUnit.HOURS)); }
    
    @SneakyThrows public static final Long setStartOfDay(Long _v) { return toLong(toInstant(_v).truncatedTo(ChronoUnit.DAYS)); }
    
    @SneakyThrows public static final Long setStartOfMonth(Long _v) {
        Date _t = toDate(_v);
        _t.setDate(1);
        _t.setHours(0);
        _t.setMinutes(0);
        _t.setSeconds(0);
        return toLong(_t);
    }
    
    @SneakyThrows public static final Long setStartOfYear(Long _v) {
        Date _t = toDate(_v);
        _t.setMonth(0);
        _t.setDate(1);
        _t.setHours(0);
        _t.setMinutes(0);
        _t.setSeconds(0);
        return toLong(_t);
    }
    
    @SneakyThrows public static final Date setStartOfSecond(Date _v) { return toDate(toInstant(_v).truncatedTo(ChronoUnit.SECONDS)); }
    
    @SneakyThrows public static final Date setStartOfMinute(Date _v) { return toDate(toInstant(_v).truncatedTo(ChronoUnit.MINUTES)); }
    
    @SneakyThrows public static final Date setStartOfHour(Date _v) { return toDate(toInstant(_v).truncatedTo(ChronoUnit.HOURS)); }
    
    @SneakyThrows public static final Date setStartOfDay(Date _v) { return toDate(toInstant(_v).truncatedTo(ChronoUnit.DAYS)); }
    
    @SneakyThrows public static final Date setStartOfMonth(Date _v) { return toDate(toInstant(_v).truncatedTo(ChronoUnit.MONTHS)); }
    
    @SneakyThrows public static final Date setStartOfYear(Date _v) { return toDate(toInstant(_v).truncatedTo(ChronoUnit.YEARS)); }
    
    @SneakyThrows public static final String setStartOfSecond(String _v) { return toString(toInstant(_v).truncatedTo(ChronoUnit.SECONDS)); }
    
    @SneakyThrows public static final String setStartOfMinute(String _v) { return toString(toInstant(_v).truncatedTo(ChronoUnit.MINUTES)); }
    
    @SneakyThrows public static final String setStartOfHour(String _v) { return toString(toInstant(_v).truncatedTo(ChronoUnit.HOURS)); }
    
    @SneakyThrows public static final String setStartOfDay(String _v) { return toString(toInstant(_v).truncatedTo(ChronoUnit.DAYS)); }
    
    @SneakyThrows public static final String setStartOfMonth(String _v) { return toString(toInstant(_v).truncatedTo(ChronoUnit.MONTHS)); }
    
    @SneakyThrows public static final String setStartOfYear(String _v) { return toString(toInstant(_v).truncatedTo(ChronoUnit.YEARS)); }
    
    @SneakyThrows public static final Instant setStartOfSecond(Instant _v) { return _v.truncatedTo(ChronoUnit.SECONDS); }
    
    @SneakyThrows public static final Instant setStartOfMinute(Instant _v) { return _v.truncatedTo(ChronoUnit.MINUTES); }
    
    @SneakyThrows public static final Instant setStartOfHour(Instant _v) { return _v.truncatedTo(ChronoUnit.HOURS); }
    
    @SneakyThrows public static final Instant setStartOfDay(Instant _v) { return _v.truncatedTo(ChronoUnit.DAYS); }
    
    @SneakyThrows public static final Instant setStartOfMonth(Instant _v) { return _v.truncatedTo(ChronoUnit.MONTHS); }
    
    @SneakyThrows public static final Instant setStartOfYear(Instant _v) { return _v.truncatedTo(ChronoUnit.YEARS); }
    
    @SneakyThrows public static final LocalDate setStartOfSecond(LocalDate _v) { return toLocalDate(toInstant(_v).truncatedTo(ChronoUnit.SECONDS)); }
    
    @SneakyThrows public static final LocalDate setStartOfMinute(LocalDate _v) { return toLocalDate(toInstant(_v).truncatedTo(ChronoUnit.MINUTES)); }
    
    @SneakyThrows public static final LocalDate setStartOfHour(LocalDate _v) { return toLocalDate(toInstant(_v).truncatedTo(ChronoUnit.HOURS)); }
    
    @SneakyThrows public static final LocalDate setStartOfDay(LocalDate _v) { return toLocalDate(toInstant(_v).truncatedTo(ChronoUnit.DAYS)); }
    
    @SneakyThrows public static final LocalDate setStartOfMonth(LocalDate _v) { return toLocalDate(toInstant(_v).truncatedTo(ChronoUnit.MONTHS)); }
    
    @SneakyThrows public static final LocalDate setStartOfYear(LocalDate _v) { return toLocalDate(toInstant(_v).truncatedTo(ChronoUnit.YEARS)); }
    
    @SneakyThrows public static final LocalDateTime setStartOfSecond(LocalDateTime _v) { return toLocalDateTime(toInstant(_v).truncatedTo(ChronoUnit.SECONDS)); }
    
    @SneakyThrows public static final LocalDateTime setStartOfMinute(LocalDateTime _v) { return toLocalDateTime(toInstant(_v).truncatedTo(ChronoUnit.MINUTES)); }
    
    @SneakyThrows public static final LocalDateTime setStartOfHour(LocalDateTime _v) { return toLocalDateTime(toInstant(_v).truncatedTo(ChronoUnit.HOURS)); }
    
    @SneakyThrows public static final LocalDateTime setStartOfDay(LocalDateTime _v) { return toLocalDateTime(toInstant(_v).truncatedTo(ChronoUnit.DAYS)); }
    
    @SneakyThrows public static final LocalDateTime setStartOfMonth(LocalDateTime _v) { return toLocalDateTime(toInstant(_v).truncatedTo(ChronoUnit.MONTHS)); }
    
    @SneakyThrows public static final LocalDateTime setStartOfYear(LocalDateTime _v) { return toLocalDateTime(toInstant(_v).truncatedTo(ChronoUnit.YEARS)); }
    
    @SneakyThrows public static final ZonedDateTime setStartOfSecond(ZonedDateTime _v) { return toZonedDateTime(toInstant(_v).truncatedTo(ChronoUnit.SECONDS)); }
    
    @SneakyThrows public static final ZonedDateTime setStartOfMinute(ZonedDateTime _v) { return toZonedDateTime(toInstant(_v).truncatedTo(ChronoUnit.MINUTES)); }
    
    @SneakyThrows public static final ZonedDateTime setStartOfHour(ZonedDateTime _v) { return toZonedDateTime(toInstant(_v).truncatedTo(ChronoUnit.HOURS)); }
    
    @SneakyThrows public static final ZonedDateTime setStartOfDay(ZonedDateTime _v) { return toZonedDateTime(toInstant(_v).truncatedTo(ChronoUnit.DAYS)); }
    
    @SneakyThrows public static final ZonedDateTime setStartOfMonth(ZonedDateTime _v) { return toZonedDateTime(toInstant(_v).truncatedTo(ChronoUnit.MONTHS)); }
    
    @SneakyThrows public static final ZonedDateTime setStartOfYear(ZonedDateTime _v) { return toZonedDateTime(toInstant(_v).truncatedTo(ChronoUnit.YEARS)); }
    
    @SneakyThrows public static final OffsetDateTime setStartOfSecond(OffsetDateTime _v) { return toOffsetDateTime(toInstant(_v).truncatedTo(ChronoUnit.SECONDS)); }
    
    @SneakyThrows public static final OffsetDateTime setStartOfMinute(OffsetDateTime _v) { return toOffsetDateTime(toInstant(_v).truncatedTo(ChronoUnit.MINUTES)); }
    
    @SneakyThrows public static final OffsetDateTime setStartOfHour(OffsetDateTime _v) { return toOffsetDateTime(toInstant(_v).truncatedTo(ChronoUnit.HOURS)); }
    
    @SneakyThrows public static final OffsetDateTime setStartOfDay(OffsetDateTime _v) { return toOffsetDateTime(toInstant(_v).truncatedTo(ChronoUnit.DAYS)); }
    
    @SneakyThrows public static final OffsetDateTime setStartOfMonth(OffsetDateTime _v) { return toOffsetDateTime(toInstant(_v).truncatedTo(ChronoUnit.MONTHS)); }
    
    @SneakyThrows public static final OffsetDateTime setStartOfYear(OffsetDateTime _v) { return toOffsetDateTime(toInstant(_v).truncatedTo(ChronoUnit.YEARS)); }
    
    @SneakyThrows public static final Long setStartOfWeek(Long _v) { return toLong(toInstant(_v).truncatedTo(ChronoUnit.WEEKS)); }
    
    @SneakyThrows public static final Date setStartOfWeek(Date _v) { return toDate(toInstant(_v).truncatedTo(ChronoUnit.WEEKS)); }
    
    @SneakyThrows public static final String setStartOfWeek(String _v) { return toString(toInstant(_v).truncatedTo(ChronoUnit.WEEKS)); }
    
    @SneakyThrows public static final Instant setStartOfWeek(Instant _v) { return _v.truncatedTo(ChronoUnit.WEEKS); }
    
    @SneakyThrows public static final LocalDate setStartOfWeek(LocalDate _v) { return toLocalDate(toInstant(_v).truncatedTo(ChronoUnit.WEEKS)); }
    
    @SneakyThrows public static final LocalDateTime setStartOfWeek(LocalDateTime _v) { return toLocalDateTime(toInstant(_v).truncatedTo(ChronoUnit.WEEKS)); }
    
    @SneakyThrows public static final ZonedDateTime setStartOfWeek(ZonedDateTime _v) { return toZonedDateTime(toInstant(_v).truncatedTo(ChronoUnit.WEEKS)); }
    
    @SneakyThrows public static final OffsetDateTime setStartOfWeek(OffsetDateTime _v) { return toOffsetDateTime(toInstant(_v).truncatedTo(ChronoUnit.WEEKS)); }
    
    @SneakyThrows public static final Long addWeeks(Long _v, int _n) { return addDays(_v, _n*7); }
    
    @SneakyThrows public static final Date addWeeks(Date _v, int _n) { return addDays(_v, _n*7); }
    
    @SneakyThrows public static final String addWeeks(String _v, int _n) { return addDays(_v, _n*7); }
    
    @SneakyThrows public static final Instant addWeeks(Instant _v, int _n) { return addDays(_v, _n*7); }
    
    @SneakyThrows public static final LocalDate addWeeks(LocalDate _v, int _n) { return addDays(_v, _n*7); }
    
    @SneakyThrows public static final LocalDateTime addWeeks(LocalDateTime _v, int _n) { return addDays(_v, _n*7); }
    
    @SneakyThrows public static final ZonedDateTime addWeeks(ZonedDateTime _v, int _n) { return addDays(_v, _n*7); }
    
    @SneakyThrows public static final OffsetDateTime addWeeks(OffsetDateTime _v, int _n) { return addDays(_v, _n*7); }
    
    @SneakyThrows public static final Long setEndOfSecond(Long _v) { return addSeconds(setStartOfSecond(_v),1)-1L; }
    
    @SneakyThrows public static final Long setEndOfMinute(Long _v) { return addMinutes(setStartOfMinute(_v),1)-1L; }
    
    @SneakyThrows public static final Long setEndOfHour(Long _v) { return addHours(setStartOfHour(_v),1)-1L; }
    
    @SneakyThrows public static final Long setEndOfDay(Long _v) { return addDays(setStartOfDay(_v),1)-1L; }
    
    @SneakyThrows public static final Long setEndOfMonth(Long _v) { return addMonths(setStartOfMonth(_v),1)-1L; }
    
    @SneakyThrows public static final Long setEndOfYear(Long _v) { return addYears(setStartOfYear(_v),1)-1L; }
    
    @SneakyThrows public static final Date setEndOfSecond(Date _v) { return toDate(toLong(addSeconds(setStartOfSecond(_v),1))-1L); }
    
    @SneakyThrows public static final Date setEndOfMinute(Date _v) { return toDate(toLong(addMinutes(setStartOfMinute(_v),1))-1L); }
    
    @SneakyThrows public static final Date setEndOfHour(Date _v) { return toDate(toLong(addHours(setStartOfHour(_v),1))-1L); }
    
    @SneakyThrows public static final Date setEndOfDay(Date _v) { return toDate(toLong(addDays(setStartOfDay(_v),1))-1L); }
    
    @SneakyThrows public static final Date setEndOfMonth(Date _v) { return toDate(toLong(addMonths(setStartOfMonth(_v),1))-1L); }
    
    @SneakyThrows public static final Date setEndOfYear(Date _v) { return toDate(toLong(addYears(setStartOfYear(_v),1))-1L); }
    
    @SneakyThrows public static final String setEndOfSecond(String _v) { return toString(toLong(addSeconds(setStartOfSecond(_v),1))-1L); }
    
    @SneakyThrows public static final String setEndOfMinute(String _v) { return toString(toLong(addMinutes(setStartOfMinute(_v),1))-1L); }
    
    @SneakyThrows public static final String setEndOfHour(String _v) { return toString(toLong(addHours(setStartOfHour(_v),1))-1L); }
    
    @SneakyThrows public static final String setEndOfDay(String _v) { return toString(toLong(addDays(setStartOfDay(_v),1))-1L); }
    
    @SneakyThrows public static final String setEndOfMonth(String _v) { return toString(toLong(addMonths(setStartOfMonth(_v),1))-1L); }
    
    @SneakyThrows public static final String setEndOfYear(String _v) { return toString(toLong(addYears(setStartOfYear(_v),1))-1L); }
    
    @SneakyThrows public static final Instant setEndOfSecond(Instant _v) { return toInstant(toLong(addSeconds(setStartOfSecond(_v),1))-1L); }
    
    @SneakyThrows public static final Instant setEndOfMinute(Instant _v) { return toInstant(toLong(addMinutes(setStartOfMinute(_v),1))-1L); }
    
    @SneakyThrows public static final Instant setEndOfHour(Instant _v) { return toInstant(toLong(addHours(setStartOfHour(_v),1))-1L); }
    
    @SneakyThrows public static final Instant setEndOfDay(Instant _v) { return toInstant(toLong(addDays(setStartOfDay(_v),1))-1L); }
    
    @SneakyThrows public static final Instant setEndOfMonth(Instant _v) { return toInstant(toLong(addMonths(setStartOfMonth(_v),1))-1L); }
    
    @SneakyThrows public static final Instant setEndOfYear(Instant _v) { return toInstant(toLong(addYears(setStartOfYear(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDate setEndOfSecond(LocalDate _v) { return toLocalDate(toLong(addSeconds(setStartOfSecond(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDate setEndOfMinute(LocalDate _v) { return toLocalDate(toLong(addMinutes(setStartOfMinute(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDate setEndOfHour(LocalDate _v) { return toLocalDate(toLong(addHours(setStartOfHour(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDate setEndOfDay(LocalDate _v) { return toLocalDate(toLong(addDays(setStartOfDay(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDate setEndOfMonth(LocalDate _v) { return toLocalDate(toLong(addMonths(setStartOfMonth(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDate setEndOfYear(LocalDate _v) { return toLocalDate(toLong(addYears(setStartOfYear(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDateTime setEndOfSecond(LocalDateTime _v) { return toLocalDateTime(toLong(addSeconds(setStartOfSecond(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDateTime setEndOfMinute(LocalDateTime _v) { return toLocalDateTime(toLong(addMinutes(setStartOfMinute(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDateTime setEndOfHour(LocalDateTime _v) { return toLocalDateTime(toLong(addHours(setStartOfHour(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDateTime setEndOfDay(LocalDateTime _v) { return toLocalDateTime(toLong(addDays(setStartOfDay(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDateTime setEndOfMonth(LocalDateTime _v) { return toLocalDateTime(toLong(addMonths(setStartOfMonth(_v),1))-1L); }
    
    @SneakyThrows public static final LocalDateTime setEndOfYear(LocalDateTime _v) { return toLocalDateTime(toLong(addYears(setStartOfYear(_v),1))-1L); }
    
    @SneakyThrows public static final ZonedDateTime setEndOfSecond(ZonedDateTime _v) { return toZonedDateTime(toLong(addSeconds(setStartOfSecond(_v),1))-1L); }
    
    @SneakyThrows public static final ZonedDateTime setEndOfMinute(ZonedDateTime _v) { return toZonedDateTime(toLong(addMinutes(setStartOfMinute(_v),1))-1L); }
    
    @SneakyThrows public static final ZonedDateTime setEndOfHour(ZonedDateTime _v) { return toZonedDateTime(toLong(addHours(setStartOfHour(_v),1))-1L); }
    
    @SneakyThrows public static final ZonedDateTime setEndOfDay(ZonedDateTime _v) { return toZonedDateTime(toLong(addDays(setStartOfDay(_v),1))-1L); }
    
    @SneakyThrows public static final ZonedDateTime setEndOfMonth(ZonedDateTime _v) { return toZonedDateTime(toLong(addMonths(setStartOfMonth(_v),1))-1L); }
    
    @SneakyThrows public static final ZonedDateTime setEndOfYear(ZonedDateTime _v) { return toZonedDateTime(toLong(addYears(setStartOfYear(_v),1))-1L); }
    
    @SneakyThrows public static final OffsetDateTime setEndOfSecond(OffsetDateTime _v) { return toOffsetDateTime(toLong(addSeconds(setStartOfSecond(_v),1))-1L); }
    
    @SneakyThrows public static final OffsetDateTime setEndOfMinute(OffsetDateTime _v) { return toOffsetDateTime(toLong(addMinutes(setStartOfMinute(_v),1))-1L); }
    
    @SneakyThrows public static final OffsetDateTime setEndOfHour(OffsetDateTime _v) { return toOffsetDateTime(toLong(addHours(setStartOfHour(_v),1))-1L); }
    
    @SneakyThrows public static final OffsetDateTime setEndOfDay(OffsetDateTime _v) { return toOffsetDateTime(toLong(addDays(setStartOfDay(_v),1))-1L); }
    
    @SneakyThrows public static final OffsetDateTime setEndOfMonth(OffsetDateTime _v) { return toOffsetDateTime(toLong(addMonths(setStartOfMonth(_v),1))-1L); }
    
    @SneakyThrows public static final OffsetDateTime setEndOfYear(OffsetDateTime _v) { return toOffsetDateTime(toLong(addYears(setStartOfYear(_v),1))-1L); }
    
    @SneakyThrows public static final ZonedDateTime toZone(Long _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSameInstant(_z); }
    
    @SneakyThrows public static final ZonedDateTime toZone(Date _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSameInstant(_z); }
    
    @SneakyThrows public static final ZonedDateTime toZone(String _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSameInstant(_z); }
    
    @SneakyThrows public static final ZonedDateTime toZone(Instant _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSameInstant(_z); }
    
    @SneakyThrows public static final ZonedDateTime toZone(LocalDate _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSameInstant(_z); }
    
    @SneakyThrows public static final ZonedDateTime toZone(LocalDateTime _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSameInstant(_z); }
    
    @SneakyThrows public static final ZonedDateTime toZone(ZonedDateTime _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSameInstant(_z); }
    
    @SneakyThrows public static final ZonedDateTime toZone(OffsetDateTime _v, ZoneId _z) { return _v.atZoneSameInstant(_z); }
    
    @SneakyThrows public static final ZonedDateTime setZone(Long _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSimilarLocal(_z); }
    
    @SneakyThrows public static final ZonedDateTime setZone(Date _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSimilarLocal(_z); }
    
    @SneakyThrows public static final ZonedDateTime setZone(String _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSimilarLocal(_z); }
    
    @SneakyThrows public static final ZonedDateTime setZone(Instant _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSimilarLocal(_z); }
    
    @SneakyThrows public static final ZonedDateTime setZone(LocalDate _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSimilarLocal(_z); }
    
    @SneakyThrows public static final ZonedDateTime setZone(LocalDateTime _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSimilarLocal(_z); }
    
    @SneakyThrows public static final ZonedDateTime setZone(ZonedDateTime _v, ZoneId _z) { return toOffsetDateTime(_v).atZoneSimilarLocal(_z); }
    
    @SneakyThrows public static final ZonedDateTime setZone(OffsetDateTime _v, ZoneId _z) { return _v.atZoneSimilarLocal(_z); }
    
    public static void main(String[] args)
    {
        System.out.println(toString(setStartOfYear(0L)));
        System.out.println(toString(setEndOfYear(0L)));
    }
}
