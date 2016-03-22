package com.connected.harman.todoapp.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.connected.harman.todoapp.entity.Task;

import java.util.ArrayList;

/**
 * Created by Vilas on 2/20/2016.
 *
 * Database helper class to manage CRUD operations.
 */
public class DBHelper  {

    /**
     * Class to create actual database.
     */
    static class ToDoHelper extends SQLiteOpenHelper{

        private ToDoHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.e(TAG,CREATE_TABLE_TODO);
            db.execSQL(CREATE_TABLE_TODO);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private static final String TAG="DBHelper";

    private static DBHelper mDbHelper;
    private static ToDoHelper toDoHelper;

    private static final String DB_NAME="TODODB";
    private static final int DB_VERSION=1;

    private static final String TABLE_TODO="TABLE_TODO";

    private static final String KEY_TASK_ID="KEY_TASK_ID";
    private static final String KEY_TASK_NAME="KEY_TASK_NAME";
    private static final String KEY_TASK_DATE="KEY_TASK_DATE";
    private static final String KEY_TASK_DESCRIPTION="KEY_TASK_DESC";
    private static final String CREATE_TABLE_TODO="CREATE TABLE "+TABLE_TODO+
            "("+KEY_TASK_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            KEY_TASK_NAME+" TEXT,"+
            KEY_TASK_DESCRIPTION+" TEXT,"+
            KEY_TASK_DATE+" LONG"+")";

    /**
     * Creating singleton DB instance.
     * @param context
     * @return
     */
    public static DBHelper getInstance(Context context){
        if(mDbHelper==null){
            mDbHelper=new DBHelper();
            toDoHelper=new ToDoHelper(context);
        }
        return mDbHelper;
    }

    /**
     * Insert new Task to database
     * @param task Task to be inserted
     * @return
     */
    public long insertTask(Task task){
        SQLiteDatabase db=toDoHelper.getWritableDatabase();

        ContentValues value=new ContentValues();
        value.put(KEY_TASK_NAME,task.getTaskName());
        value.put(KEY_TASK_DESCRIPTION,task.getTaskDescription());
        value.put(KEY_TASK_DATE,task.getTaskDate());

        return db.insert(TABLE_TODO,null,value);
    }

    /**
     * Delete task from database
     * @param taskId TaskID of Task to be deleted
     * @return
     */
    public long deleteTask(int taskId){

        SQLiteDatabase db=toDoHelper.getWritableDatabase();
        return db.delete(TABLE_TODO, KEY_TASK_ID + "=?", new String[]{String.valueOf(taskId)});

    }

    /**
     * Update Task in database
     * @param task Task to be updated
     * @return
     */
    public long updateTask(Task task){
        SQLiteDatabase db=toDoHelper.getWritableDatabase();

        ContentValues value=new ContentValues();
        value.put(KEY_TASK_NAME,task.getTaskName());
        value.put(KEY_TASK_DESCRIPTION,task.getTaskDescription());
        value.put(KEY_TASK_DATE,task.getTaskDate());

       return db.update(TABLE_TODO, value, KEY_TASK_ID + "=?", new String[]{String.valueOf(task.getTaskId())});
    }

    /**
     * Get All Tasks stored in the database
     * @return List of Tasks elase null
     */
    public ArrayList<Task> getAllTasks(){

        ArrayList<Task> taskList=null;
        SQLiteDatabase db=toDoHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            taskList=new ArrayList<Task>();
            do {
                Task task = new Task();
                task.setTaskId(cursor.getInt((cursor.getColumnIndex(KEY_TASK_ID))));
                task.setTaskDescription((cursor.getString(cursor.getColumnIndex(KEY_TASK_DESCRIPTION))));
                task.setTaskDate(cursor.getLong(cursor.getColumnIndex(KEY_TASK_DATE)));
                task.setTaskName(cursor.getString(cursor.getColumnIndex(KEY_TASK_NAME)));

                // adding to todo list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }

    /**
     * Get a particular Task from database
     * @param task_id TaskID of the Task to be fetched.
     * @return
     */
    public Task getTask(int task_id){
        SQLiteDatabase db=toDoHelper.getReadableDatabase();

        String selectQuery="SELECT * FROM "+TABLE_TODO+" WHERE "+KEY_TASK_ID+"="+task_id;

        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor!=null){
            cursor.moveToFirst();
        }else {
            return null;
        }
        Task task=new Task();

        task.setTaskName(cursor.getString(cursor.getColumnIndex(KEY_TASK_NAME)));
        task.setTaskDate(cursor.getLong(cursor.getColumnIndex(KEY_TASK_DATE)));
        task.setTaskDescription(cursor.getString(cursor.getColumnIndex(KEY_TASK_DESCRIPTION)));
        task.setTaskId(cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID)));

        return task;
    }

    /**
     * Close database
     */
    public void closeDB() {
        SQLiteDatabase db = toDoHelper.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
