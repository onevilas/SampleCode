package com.nvy.chat.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nvy.chat.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Vilas on 11/12/15.
 *
 * Do not access this class Directly. Use ChatDBHelper.getInstance to access db helper methods.
 */
public class ChatDatabase extends SQLiteOpenHelper {

    private static final String TAG="ChatDatabase";

    private static ChatDatabase sInstance;

    static final String CREATE_TABLE_USER="CREATE TABLE "+DBConstants.TABLE_USER+
            " ("+DBConstants.USER_ID+" TEXT PRIMARY KEY NOT NULL,"+
            DBConstants.USER_NAME+" TEXT );";
    static final String CREATE_TABLE_MESSAGE="CREATE TABLE "+DBConstants.TABLE_MESSAGE+
            " ("+DBConstants.MESSAGE_ID+" TEXT PRIMARY KEY NOT NULL,"+
            DBConstants.MESSAGE_BODY+" TEXT,"+
            DBConstants.MESSAGE_FROM_USER_ID+" TEXT,"+
            DBConstants.MESSAGE_TO_USER_ID+" TEXT,"+
            DBConstants.MESSAGE_GROUP_ID+" TEXT,"+
            DBConstants.MESSAGE_STATUS+" TEXT,"+
            DBConstants.MESSAGE_SENT_TS+" INTEGER,"+
            DBConstants.MESSAGE_DELIVERED_TS +" INTEGER,"+
            DBConstants.MESSAGE_DISPLAYED_TS+" INTEGER );";

    /**
     * Do not call this method. This method is called internally.
     * @param context
     * @return
     */
    public static synchronized ChatDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ChatDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    private ChatDatabase(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,CREATE_TABLE_MESSAGE);
        Log.i(TAG,CREATE_TABLE_USER);

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_MESSAGE);

        Log.i(TAG,"Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
