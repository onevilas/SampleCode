package com.nvy.chat.callback;

import com.nvy.chat.entity.ChatMessage;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by Vilas on 4/12/15.
 */
public interface IncomingMessageListener {

    public void onMessagereceived(Message message);
}
