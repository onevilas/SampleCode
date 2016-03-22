package com.nvy.chat.service;

import android.os.Binder;

import java.lang.ref.WeakReference;

/**
 * Created by Vilas on 17/11/15.
 */
public class ChatBinder<S> extends Binder {
    private final WeakReference<S> mService;

    public ChatBinder(final S service) {
        mService = new WeakReference<S>(service);
    }

    public S getService() {
        return mService.get();
    }
}
