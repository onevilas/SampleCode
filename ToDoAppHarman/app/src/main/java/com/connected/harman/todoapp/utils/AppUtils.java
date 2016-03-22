package com.connected.harman.todoapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vilas on 2/20/2016.
 *
 * Class to define utility methods used throught application
 */
public class AppUtils {

    /**
     * Convert current date to miliseconds
     * @return
     */
    public static long getLongDate(){
        return System.currentTimeMillis();
    }

    /**
     * Convert long miliseconds to String date
     * @param miliseconds
     * @return Date in String formate
     */
    public static String convertLongToDate(long miliseconds){
        Date dateObj=new Date(miliseconds);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa");
        return format.format(dateObj);
    }

    /**
     * Method to display a alert dialog.
     * @param context
     * @param title
     * @param message
     */
    public static void showDialog(Context context,String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setCancelable(false)
        .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog=builder.create();
        dialog.show();
    }

    /**
     * Method to hide soft keyboard when responsible view is not in focus.
     * @param context
     * @param view
     */
    public static void hideSoftKeyboard(Activity context,View view){
        InputMethodManager inputMethodManager= (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
