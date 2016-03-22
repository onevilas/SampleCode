package com.nvy.chat.callback;

import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by Vilas on 4/12/15.
 */
public interface MessageStatusChangedListener {

    public void onMessageSent(Stanza packet);
    public void onMessageDelivered(String fromJid, String toJid, String receiptId, Stanza receipt);
    public void onMessageDisplayed(String fromJid, String toJid, String receiptId, Stanza receipt);
}
