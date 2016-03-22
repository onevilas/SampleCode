package com.nvy.chat.persistance;

/**
 * Created by Vilas on 11/12/15.
 */
public class DBConstants {

    public static final String DB_NAME="CHAT_DB";
    public static final int DB_VERSION=1;

    public static final String TABLE_USER="TABLE_USER";
    public static final String TABLE_MESSAGE="TABLE_MESSAGE";

    public static final String USER_NAME="USER_NAME";
    public static final String USER_ID="USER_ID";

    public static final String MESSAGE_GROUP_ID="MESSAGE_GROUP_ID";
    public static final String MESSAGE_FROM_USER_ID="MESSAGE_FROM_USER_ID";
    public static final String MESSAGE_TO_USER_ID="MESSAGE_TO_USER_ID";
    public static final String MESSAGE_SENT_TS="MESSAGE_SENT_TS";
    public static final String MESSAGE_DELIVERED_TS ="MESSAGE_DELIVERED_TS";
    public static final String MESSAGE_DISPLAYED_TS="MESSAGE_READ_TS";
    public static final String MESSAGE_BODY="MESSAGE_BODY";
    public static final String MESSAGE_STATUS="MESSAGE_STATUS";
    public static final String MESSAGE_ID="MESSAGE_ID";

    public static final int MESSAGE_STATUS_SENDING=0;
    public static final int MESSAGE_STATUS_SENT=1;
    public static final int MESSAGE_STATUS_DELIVERED=2;
    public static final int MESSAGE_STATUS_DISPLAYED=3;

}
