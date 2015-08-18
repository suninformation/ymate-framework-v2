/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.commons;

import net.ymate.platform.core.util.DateTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date（日期）类型数据处理相关的函数工具集合
 * <p>于 2015/8/14 下午2:58 由DateUtils类重构</p>
 *
 * @author 刘镇 (suninformation@163.com) on 2010-8-8 下午12:37:55
 * @version 1.0
 */
public class DateTimeHelper {

    private Calendar __calendar;

    public static DateTimeHelper bind(Date date) {
        return new DateTimeHelper(date);
    }

    public static DateTimeHelper bind(long date) {
        return new DateTimeHelper(date);
    }

    public static DateTimeHelper bind(String dateStr, String dateFormat) throws ParseException {
        return new DateTimeHelper(dateStr, dateFormat);
    }

    public static DateTimeHelper now() {
        return new DateTimeHelper();
    }

    private DateTimeHelper() {
        __calendar = Calendar.getInstance();
    }

    private DateTimeHelper(Date date) {
        __calendar = Calendar.getInstance();
        __calendar.setTime(date);
    }

    private DateTimeHelper(long date) {
        __calendar = Calendar.getInstance();
        if (String.valueOf(date).length() <= 10) {
            date *= 1000;
        }
        __calendar.setTimeInMillis(date);
    }

    private DateTimeHelper(String dateStr, String dateFormat) throws ParseException {
        __calendar = Calendar.getInstance();
        __calendar.setTime(new SimpleDateFormat(dateFormat).parse(dateStr));
    }

    private DateTimeHelper(int year, int month, int day) {
        __calendar = Calendar.getInstance();
        __calendar.set(year, month - 1, day);
    }

    private DateTimeHelper(int year, int month, int day, int hour, int minute, int second) {
        __calendar = Calendar.getInstance();
        __calendar.set(year, month - 1, day, hour, minute, second);
    }

    public DateTimeHelper timeZone(TimeZone timeZone) {
        __calendar.setTimeZone(timeZone);
        return this;
    }

    public TimeZone timeZone() {
        return __calendar.getTimeZone();
    }

    public Date time() {
        return __calendar.getTime();
    }

    public DateTimeHelper time(Date date) {
        __calendar.setTime(date);
        return this;
    }

    public int year() {
        return __calendar.get(Calendar.YEAR);
    }

    public DateTimeHelper year(int year) {
        __calendar.set(Calendar.YEAR, year);
        return this;
    }

    public int month() {
        return __calendar.get(Calendar.MONTH) + 1;
    }

    public DateTimeHelper month(int month) {
        __calendar.set(Calendar.MONTH, month - 1);
        return this;
    }

    public int day() {
        return __calendar.get(Calendar.DAY_OF_MONTH);
    }

    public DateTimeHelper day(int day) {
        __calendar.set(Calendar.DATE, day);
        return this;
    }

    public int hour() {
        return __calendar.get(Calendar.HOUR_OF_DAY);
    }

    public DateTimeHelper hour(int hour) {
        __calendar.set(Calendar.HOUR_OF_DAY, hour);
        return this;
    }

    public int minute() {
        return __calendar.get(Calendar.MINUTE);
    }

    public DateTimeHelper minute(int minute) {
        __calendar.set(Calendar.MINUTE, minute);
        return this;
    }

    public int second() {
        return __calendar.get(Calendar.SECOND);
    }

    public DateTimeHelper second(int second) {
        __calendar.set(Calendar.SECOND, second);
        return this;
    }

    public int dayOfWeek() {
        return __calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public int weekOfMonth() {
        return __calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public int weekOfYear() {
        return __calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public int dayOfWeekInMonth() {
        return __calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    public long timeMillis() {
        return __calendar.getTimeInMillis();
    }

    public DateTimeHelper timeMillis(long timeMillis) {
        __calendar.setTimeInMillis(timeMillis);
        return this;
    }

    public int timeUTC() {
        return (int) (timeMillis() / 1000);
    }

    public DateTimeHelper timeUTC(long timeUTC) {
        if (String.valueOf(timeUTC).length() <= 10) {
            timeUTC *= 1000;
        }
        __calendar.setTimeInMillis(timeUTC);
        return this;
    }

    public int daysOfMonth() {
        return __calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public long subtract(Date date) {
        return __calendar.getTimeInMillis() - date.getTime();
    }

    public long subtract(DateTimeHelper dateTimeHelper) {
        return subtract(dateTimeHelper.time());
    }

    public DateTimeHelper secondsAdd(int seconds) {
        __calendar.add(Calendar.SECOND, seconds);
        return this;
    }

    public DateTimeHelper minutesAdd(int minutes) {
        __calendar.add(Calendar.MINUTE, minutes);
        return this;
    }

    public DateTimeHelper hoursAdd(int hours) {
        __calendar.add(Calendar.HOUR, hours);
        return this;
    }

    public DateTimeHelper daysAdd(int days) {
        __calendar.add(Calendar.DATE, days);
        return this;
    }

    public DateTimeHelper weeksAdd(int weeks) {
        __calendar.add(Calendar.WEEK_OF_MONTH, weeks);
        return this;
    }

    public DateTimeHelper monthsAdd(int months) {
        __calendar.add(Calendar.MONTH, months);
        return this;
    }

    public DateTimeHelper yearsAdd(int years) {
        __calendar.add(Calendar.YEAR, years);
        return this;
    }

    @Override
    public String toString() {
        return toString(DateTimeUtils.YYYY_MM_DD_HH_MM_SS_SSS);
    }

    public String toString(String dateFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        fmt.setTimeZone(__calendar.getTimeZone());
        return fmt.format(__calendar.getTime());
    }
}
