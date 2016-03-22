package com.nvy.chat.callback;

/**
 * Created by Vilas on 3/12/15.
 */
public  interface ConnectionListener {

    public void onConnectionSuccessful();
    public void onConnectionClosed();
    public void onConnectionClosedOnError();
    public void onReconnectionSuccessful();
}
