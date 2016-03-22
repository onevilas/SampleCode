package com.nvy.chat.xmpp;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;

/**
 * Created by Vilas on 9/12/15.
 *
 *Class for chat related events.
 */
public class ChatEventManagerListener implements ChatManagerListener {

    XMPPIncomingMessageListener mMessageListener;

    public ChatEventManagerListener(XMPPIncomingMessageListener messageListener)
    {
        mMessageListener=messageListener;
    }
    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        if (!createdLocally) {
            chat.addMessageListener(mMessageListener);
        }
    }
}
