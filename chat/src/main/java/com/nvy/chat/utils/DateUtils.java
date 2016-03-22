package com.nvy.chat.utils;

import android.database.Cursor;

import java.util.Date;

/**
 * Created by Vilas on 11/12/15.
 */
public class DateUtils {

    public static Long persistDate(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }

    public static Date loadDate(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return new Date(cursor.getLong(index));
    }
}
