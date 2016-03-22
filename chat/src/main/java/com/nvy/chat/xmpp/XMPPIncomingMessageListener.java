package com.nvy.chat.xmpp;

import android.util.Log;


import com.nvy.chat.utils.AppConstants;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;

/**
 * Created by Vilas on 9/12/15.
 * <p>
 * Class to handle incoming messages
 */
public class XMPPIncomingMessageListener implements ChatMessageListener, ChatStateListener {

    private static final String TAG = "XMPPIncomingMsgListener";

    XMPPHelper mXmppHelper;
//    ChatDBHelper mChatDBHelper;

    public XMPPIncomingMessageListener(XMPPHelper xmppHelper) {
        mXmppHelper = xmppHelper;
//        mChatDBHelper = chatDBHelper;
    }

    @Override
    public void stateChanged(Chat chat, ChatState state) {
        String status = "Online";

        if (ChatState.composing.equals(state)) {
            Log.d("Chat State", chat.getParticipant() + " is typing..");
            status = AppConstants.STATUS_TYPING;
        } else if (ChatState.gone.equals(state)) {
            Log.d("Chat State", chat.getParticipant() + " has left the conversation.");
            status = AppConstants.STATUS_LEFT_CONVERSATION;
        } else if (ChatState.paused.equals(state)) {
            Log.d("Chat State", chat.getParticipant() + ": " + state.name());
            status = AppConstants.STATUS_PAUSED;
        } else if (ChatState.active.equals(state)) {
            status = AppConstants.STATUS_ONLINE;
        }

        mXmppHelper.setIncomingTypingState(status);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        Log.i(TAG, "Message type " + message.getType());
        Log.i(TAG, "Message body " + message.getBody());
        if (message.getType() == Message.Type.chat
                && message.getBody() != null) {
            Log.i(TAG, "Condition is true and valid message");
//            persistMessage(message);
            mXmppHelper.processIncomingMessage(message);
        }
    }

//    private void persistMessage(final Message message) {
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                mChatDBHelper.insertMessage(
//                        message.getStanzaId(),message.getBody(),
//                        message.get,message.getFrom(),message.getTo(),DBConstants.MESSAGE_STATUS_DELIVERED);
//            }
//        };
//        thread.start();
//    }
}
