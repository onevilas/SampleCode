package com.nvy.chat.xmpp;

import android.util.Log;

import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

/**
 * Created by Vilas on 9/12/15.
 *
 *
 * Class to handle delivered state of message and update UI
 *
 */
public class MessageReceiptReceivedListener implements ReceiptReceivedListener {

    XMPPHelper mXmppHelper;

    public MessageReceiptReceivedListener(XMPPHelper xmppHelper){
        mXmppHelper=xmppHelper;
    }

    @Override
    public void onReceiptReceived(String fromJid, String toJid, String receiptId, Stanza receipt) {
        Log.i("MesRecptReceivedListenr", "Message " + receiptId + " Delivered to " + toJid + " from " + fromJid);
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        mXmppHelper.setIncomingMessageStatusDelivered(fromJid, toJid, receiptId, receipt);
    }
}
