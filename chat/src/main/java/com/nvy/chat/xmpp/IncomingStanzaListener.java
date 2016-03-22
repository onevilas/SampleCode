package com.nvy.chat.xmpp;

import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.sm.StreamManagementException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Created by Vilas on 9/12/15.
 *
 * Class to handle sent acknowledgement from server.
 */
public class IncomingStanzaListener implements StanzaListener {

    XMPPTCPConnection mConnection;
    XMPPHelper mXmppHelper;

    public IncomingStanzaListener(XMPPTCPConnection connection,XMPPHelper xmppHelper){
        mConnection=connection;
        mXmppHelper=xmppHelper;
    }
    @Override
    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
        try {
            mConnection.addStanzaIdAcknowledgedListener(packet.getStanzaId(), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    Log.d("IncomingStanzaListener", "Received ACK for packet " + packet.getStanzaId());

                    //Update the UI
                    mXmppHelper.setOutgoingMessageStatusSent(packet);
                }
            });
            Log.d("IncomingStanzaListener",
                    "Registered Stream Management ACK listener for stanza ID " + packet.getStanzaId());
        } catch (StreamManagementException.StreamManagementNotEnabledException e) {
            e.printStackTrace();
        }
    }
}
