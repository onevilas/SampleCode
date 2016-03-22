package com.nvy.chat.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by Vilas on 24/11/15.
 *
 * Interface to monitor ChatService
 */
public class ChatServiceConnection implements ServiceConnection {

    private static ChatService mService;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((ChatBinder<ChatService>) service).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
    }

    public static ChatService getChatService() {
        return mService;
    }
}
