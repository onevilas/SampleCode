package com.nvy.chat.xmpp;

import android.util.Log;

import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by Vilas on 9/12/15.
 *
 * Class to handle message displayed("Read") to user
 */
public class MessageDisplayedReceiptListener implements ReadReceiptReceivedListener {

    XMPPHelper mXmppHelper;

    public MessageDisplayedReceiptListener(XMPPHelper xmppHelper){
        mXmppHelper=xmppHelper;
    }

    @Override
    public void onReceiptReceived(String fromJid, String toJid, String receiptId, Stanza receipt) {
        Log.i("MessageDisplayedListenr", "Message " + receiptId + " Read by " + toJid);
        mXmppHelper.setIncomingMessageStatusDisplayed(fromJid, toJid, receiptId, receipt);
    }
}
