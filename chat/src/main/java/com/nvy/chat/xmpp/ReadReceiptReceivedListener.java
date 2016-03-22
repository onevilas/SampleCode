package com.nvy.chat.xmpp;

/**
 * Created by Vilas on 8/12/15.
 */

import org.jivesoftware.smack.packet.Stanza;

/**
 * Interface for received receipt notifications.
 *
 * Implement this and add a listener to get notified.
 */
public interface ReadReceiptReceivedListener {

    void onReceiptReceived(String fromJid, String toJid, String receiptId, Stanza receipt);
}
