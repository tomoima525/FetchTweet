package com.tomoima.fetchtweet.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tomoaki on 5/13/16.
 */
public class DateMapperUtil {

    public static Date getTwitterDate(String date) {
        final String TWITTER_DATE = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER_DATE, Locale.ENGLISH);
        sf.setLenient(true);
        Date parseDate;
        try {
            parseDate = sf.parse(date);
        } catch (ParseException e){
            parseDate = null;
        }
        return parseDate;
    }
}
