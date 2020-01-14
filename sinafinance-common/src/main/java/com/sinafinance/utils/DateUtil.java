/*
 * Copyright (C) 2016. Attractor, Inc. All Rights Reserved.
 */

package com.sinafinance.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description
 */
public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    private static final ZoneId zone = ZoneId.systemDefault();

    /**
     * 将时间转化为字符串
     *
     * @param date
     * @param pattern
     *
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public class DatePattern {
        public static final String PATTERN_YYYYMMDD = "yyyyMMdd";
        public static final String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
        public static final String PATTERN_HHMMSS = "HHmmss";
        public static final String PATTERN_YYYYMM = "yyyyMM";
        public static final String PATTERN_YYYY_MM = "yyyy-MM";

    }

    /**
     * 时间字符串格式化为date
     *
     * @param dateString
     * @param pattern
     *
     * @return
     */
    public static Date strToDate(String dateString, String pattern) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            logger.error("DateUtil#strToDate error: " + e);
        }
        return date;
    }

    /**
     * 将date转换成LocalDate
     *
     * @param date
     *
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {

        return dateToLocalDateTime(date).toLocalDate();
    }

    /**
     * 将date转换成LocalDateTime
     *
     * @param date
     *
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return localDateTime;
    }

    /**
     * 将LocalDateTime转换成date
     *
     * @param localDateTime
     *
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return null;
        }
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 将LocalDate转换成date
     *
     * @param localDate
     *
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        return localDateTimeToDate(localDate.atStartOfDay());
    }

    /**
     * 计算两个日期之间的毫秒数
     *
     * @param startDate
     * @param endDate
     *
     * @return
     */
    public static Long millisDuration(Date startDate, Date endDate) {

        if (null == startDate || null == endDate) {
            return null;
        }
        LocalDateTime start = dateToLocalDateTime(startDate);
        LocalDateTime end = dateToLocalDateTime(endDate);

        Duration duration = Duration.between(start, end);

        return duration.toMillis();
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.PATTERN_YYYY_MM_DD_HH_MM_SS);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 获得某天最小时间 2017-10-15 00:00:00
     *
     * @param date
     *
     * @return
     */
    public static LocalDateTime getStartOfDay(Date date) {
        return dateToLocalDateTime(date).with(LocalTime.MIN);
    }

    /**
     * 获得某天最大时间 2017-10-15 23:59:59
     *
     * @param date
     *
     * @return
     */
    public static LocalDateTime getEndOfDay(Date date) {
        return dateToLocalDateTime(date).with(LocalTime.MAX);
    }

    public static void main(String[] args) {
        LocalDateTime startOfDay = getEndOfDay(new Date());
        System.out.println(DateUtil.dateToString(DateUtil.localDateTimeToDate(startOfDay),
                DatePattern.PATTERN_YYYY_MM_DD_HH_MM_SS));
    }
}