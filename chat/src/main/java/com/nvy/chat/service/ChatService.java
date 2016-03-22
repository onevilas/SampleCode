package com.nvy.chat.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nvy.chat.xmpp.XMPPHelper;
import com.nvy.chat.xmpp.XMPPManager;

/**
 * Created by Vilas on 17/11/15.
 *
 * Chat Service. XMPP Connection will be established with this Service.
 */
public class ChatService extends Service {

    private static final String TAG="ChatService";

    String userName;
    String password;

    ConnectivityManager connectivityManager;
    public static XMPPManager xmpp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        userName =intent.getStringExtra("USERNAME");
        password =intent.getStringExtra("PASSWORD");

        xmpp = XMPPManager.getInstance(ChatService.this.getApplicationContext(), userName, password);

        xmpp.connect("onCreate");

        Log.i(TAG, "onBind");
        Log.i(TAG, userName + " " + password);

        return new ChatBinder<ChatService>(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return Service.START_NOT_STICKY;
    }

   @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XMPPHelper.getInstance().setChatStateOffline();
        xmpp.disconnect();
    }
}
