package com.nvy.chat.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nvy.chat.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Vilas on 11/12/15.
 */
public class ChatDBHelper {

    private static final String TAG="ChatDBHelper";

    private static ChatDBHelper chatDBHelper;
    private static ChatDatabase chatDatabase;

    /**
     * Do not call this method. This method is used internally.
     * @return
     */
    public static ChatDBHelper createInstance() {
        if (chatDBHelper == null) {
            chatDBHelper = new ChatDBHelper();
        }
        return chatDBHelper;
    }

    /**
     * Get Instance of ChatDBHelper class.
     * @return
     */
    public static ChatDBHelper getInstance(){
        return chatDBHelper;
    }

    /**
     * Do not call this method. This method is called internally.
     * @param cd
     */
    public void setChatDatabase(ChatDatabase cd){
     chatDatabase=cd;
    }

    /**
     * Insert message to database.<br> Call this method explicitly after sending and receiving message every time.
     * @param id
     * @param body
     * @param groupId
     * @param from
     * @param to
     */
    public void insertMessage(String id, String body, String groupId, String from, String to) {
        Log.i(TAG,"Inside insertMessage ");
        SQLiteDatabase db = chatDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MESSAGE_ID, id);
        contentValues.put(DBConstants.MESSAGE_BODY, body);
        contentValues.put(DBConstants.MESSAGE_GROUP_ID, groupId);
        contentValues.put(DBConstants.MESSAGE_FROM_USER_ID, from);
        contentValues.put(DBConstants.MESSAGE_TO_USER_ID, to);
        contentValues.put(DBConstants.MESSAGE_STATUS, DBConstants.MESSAGE_STATUS_SENDING);
        long rowId=db.insert(DBConstants.TABLE_MESSAGE, null, contentValues);
        if(rowId!=-1){
            Log.i(TAG,"insertMessage for "+id);
        }else {
            Log.i(TAG,"Unable to insert insertMessage with id "+id);
        }
    }

    /**
     * Update message status as "Sent" in database. This method updates time and status of message to sent.<br>
     *     Call this method explicitly after sending every message.
     * @param msgId
     * @param date
     */
    public void updateMessageSent(String msgId, Date date) {
        Log.i(TAG,"updateSentMessage Message id "+msgId+" Date "+date.toString());
        SQLiteDatabase db = chatDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MESSAGE_STATUS, DBConstants.MESSAGE_STATUS_SENT);
        contentValues.put(DBConstants.MESSAGE_SENT_TS, DateUtils.persistDate(date));
        int rowCount=db.update(DBConstants.TABLE_MESSAGE, contentValues, DBConstants.MESSAGE_ID + "=?", new String[]{msgId});
        if(rowCount>0){
            Log.i(TAG,"updateMessageSent for "+msgId+" No of rows effected "+rowCount);
        }else {
            Log.i(TAG,"Unable to update updateMessageSent as no record found with "+msgId);
        }
    }

    /**
     * Update message status as "Delivered" in database. This method updates time and status of message to delivered.<br>
     *     Call this method explicitly after sending every message.
     * @param msgId
     * @param date
     */
    public void updateMessageDelivered(String msgId, Date date) {
        Log.i(TAG,"updateMessageDelivered Message id "+msgId+" Date "+date.toString());
        SQLiteDatabase db = chatDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MESSAGE_STATUS, DBConstants.MESSAGE_STATUS_DELIVERED);
        contentValues.put(DBConstants.MESSAGE_DISPLAYED_TS, DateUtils.persistDate(date));
       int rowCount= db.update(DBConstants.TABLE_MESSAGE, contentValues, DBConstants.MESSAGE_ID + "=?", new String[]{msgId});
        if(rowCount>0){
            Log.i(TAG,"updateMessageDelivered for "+msgId+" No of rows effected "+rowCount);
        }else {
            Log.i(TAG,"Unable to update updateMessageDelivered as no record found with "+msgId);
        }

    }

    /**
     * Update message status as "Displayed" in database. This method updates time and status of message to displayed.<br>
     *     Call this method explicitly after sending every message.
     * @param msgId
     * @param date
     */
    public void updateMessageDisplayed(String msgId, Date date) {
        Log.i(TAG,"updateMessageDisplayed Message id "+msgId+" Date "+date.toString());
        SQLiteDatabase db = chatDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MESSAGE_STATUS, DBConstants.MESSAGE_STATUS_DISPLAYED);
        contentValues.put(DBConstants.MESSAGE_DELIVERED_TS, DateUtils.persistDate(date));
        int rowCount=db.update(DBConstants.TABLE_MESSAGE, contentValues, DBConstants.MESSAGE_ID + "=?", new String[]{msgId});
        if(rowCount>0){
            Log.i(TAG,"updateMessageDisplayed for "+msgId+" No of rows effected "+rowCount);
        }else {
            Log.i(TAG,"Unable to update updateMessageDisplayed");
        }
    }

    /**
     * Get all the messages belongs to the group.
     * @param groupId Group Id of the group.
     * @return
     */
    public ArrayList<Message> getMessagesFromGroup(String groupId) {
        ArrayList<Message> messages = new ArrayList<Message>();
        SQLiteDatabase db = chatDatabase.getReadableDatabase();
        String query = "SELECT " + DBConstants.MESSAGE_ID + "," +
                DBConstants.MESSAGE_BODY + "," +
                DBConstants.MESSAGE_STATUS + "," +
                DBConstants.MESSAGE_FROM_USER_ID + "," +
                DBConstants.MESSAGE_TO_USER_ID + "," +
                DBConstants.MESSAGE_GROUP_ID + "," +
                DBConstants.MESSAGE_DISPLAYED_TS + "," +
                DBConstants.MESSAGE_DELIVERED_TS + "," +
                DBConstants.MESSAGE_SENT_TS + " FROM " + DBConstants.TABLE_MESSAGE +
                " WHERE " + DBConstants.MESSAGE_GROUP_ID + "=" + "'" + groupId + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isLast()) {
            Message message = new Message();
            message.setMessageBody(cursor.getString(cursor.getColumnIndex(DBConstants.MESSAGE_BODY)));
            message.setMessageId(cursor.getString(cursor.getColumnIndex(DBConstants.MESSAGE_ID)));
            message.setMessageFrom(cursor.getString(cursor.getColumnIndex(DBConstants.MESSAGE_FROM_USER_ID)));
            message.setMessageTo(cursor.getString(cursor.getColumnIndex(DBConstants.MESSAGE_TO_USER_ID)));
            message.setMessageGroupId(groupId);
            message.setMessageStatus(cursor.getString(cursor.getColumnIndex(DBConstants.MESSAGE_STATUS)));
            message.setMessageSentTs(cursor.getString(cursor.getColumnIndex(DBConstants.MESSAGE_SENT_TS)));
            message.setMessageDeliveredTs(cursor.getString(cursor.getColumnIndex(DBConstants.MESSAGE_DELIVERED_TS)));
            message.setMessageDisplayedTs(cursor.getString(cursor.getColumnIndex(DBConstants.MESSAGE_DISPLAYED_TS)));
            messages.add(message);
        }
        Log.i(TAG,"No of messages in getMessagesFromGroup "+messages.size());
        return messages;
    }
}
