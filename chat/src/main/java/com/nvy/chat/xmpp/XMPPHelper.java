package com.nvy.chat.xmpp;

import android.os.AsyncTask;
import android.util.Log;

import com.nvy.chat.callback.ConnectionListener;
import com.nvy.chat.callback.IncomingMessageListener;
import com.nvy.chat.callback.MessageStatusChangedListener;
import com.nvy.chat.callback.UserStatusChangeListener;
import com.nvy.chat.persistance.ChatDBHelper;
import com.nvy.chat.utils.AppConstants;
import com.nvy.chat.utils.RunTimeData;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Element;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Vilas on 3/12/15.
 */
public class XMPPHelper {

    private static final String TAG = "XMPPHelper";

    private static XMPPHelper xmppHelper;

    private boolean mConnected = false;
    private boolean mLoggedin = false;
    private boolean mIsConnecting = false;
    private boolean mChatCreated = false;
    private boolean mIsAuthenticated=false;

    private ChatStateManager mChatStateManager;
    private XMPPTCPConnection mXMPPTCPConnection;
    private Chat mChat;

    private MessageStatusChangedListener messageStatusChangedListener;
    private IncomingMessageListener incomingMessageListener;
    private UserStatusChangeListener userStatusChangeListener;
    private ConnectionListener connectionListener;

    public boolean ismConnected() {
        return mConnected;
    }

    public void setConnected(boolean mConnected) {
        this.mConnected = mConnected;
    }

    public boolean isLoggedin() {
        return mLoggedin;
    }

    public void setLoggedin(boolean mLoggedin) {
        this.mLoggedin = mLoggedin;
    }

    public boolean isIsConnecting() {
        return mIsConnecting;
    }

    public void setIsConnecting(boolean mIsConnecting) {
        this.mIsConnecting = mIsConnecting;
    }

    public boolean isAuthenticated() {
        return mIsAuthenticated;
    }

    public void setIsAuthenticated(boolean mIsAuthenticated) {
        this.mIsAuthenticated = mIsAuthenticated;
    }

    public boolean isChatCreated() {
        return mChatCreated;
    }

    public void setChatCreated(boolean mChatCreated) {
        this.mChatCreated = mChatCreated;
    }

    public XMPPTCPConnection getXMPPTCPConnection() {
        return mXMPPTCPConnection;
    }

    public void setXMPPTCPConnection(XMPPTCPConnection mXMPPTCPConnection) {
        this.mXMPPTCPConnection = mXMPPTCPConnection;
    }

    public ChatStateManager getChatStateManager() {
        return mChatStateManager;
    }

    public void setChatStateManager(ChatStateManager mChatStateManager) {
        this.mChatStateManager = mChatStateManager;
    }

    public Chat getChat() {
        if (isChatCreated()) {
            return mChat;
        }
        return null;
    }

    public void setChat(Chat chat) {
        mChat = chat;
    }

    public MessageStatusChangedListener getMessageStatusChangedListener() {
        return messageStatusChangedListener;
    }

    public void setMessageStatusChangedListener(MessageStatusChangedListener mscl) {
        messageStatusChangedListener = mscl;
    }

    public IncomingMessageListener getIncomingMessageListener() {
        return incomingMessageListener;
    }

    public void setIncomingMessageListener(IncomingMessageListener iml) {
        incomingMessageListener = iml;
    }

    public UserStatusChangeListener getUserStatusChangeListener() {
        return userStatusChangeListener;
    }

    public void setUserStatusChangeListener(UserStatusChangeListener uscl) {
        userStatusChangeListener = uscl;
    }

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setConnectionListener(ConnectionListener cal) {
        connectionListener = cal;
    }

    public static XMPPHelper getInstance() {
        if (xmppHelper == null) {
            xmppHelper = new XMPPHelper();
        }
        return xmppHelper;
    }

    /**
     * Method to send message.
     *
     * @param to    to whom message to be sent
     * @param messageBody message to be sent
     */
    public String sendMessage(String from,String to, String messageBody) {
        Log.i(TAG, "SendMessage " + messageBody);

        if (!isChatCreated()) {
            createChat(to);
        }

        final Message message = new Message();
        message.setBody(messageBody);
        message.setType(Message.Type.chat);

        DeliveryReceiptRequest.addTo(message);
//        message.addExtension(DeliveryReceiptRequest.from(message));

        //Display receipt request

//        DisplayReceiptRequest.addExtensiondTo(message);

        Log.i(TAG,"Inserting values to database before sending message "+message.getStanzaId()+messageBody+ RunTimeData.groupId+from+to);
        ChatDBHelper.getInstance().insertMessage(message.getStanzaId(), messageBody, RunTimeData.groupId, from, to);

        DisplayReceipt displayReceipt = new DisplayReceipt(message.getStanzaId());
        message.addExtension(displayReceipt);
        try {
            if (mXMPPTCPConnection.isAuthenticated()) {

                getChat().sendMessage(message);
                return message.getStanzaId();
            }
        } catch (SmackException.NotConnectedException e) {
            Log.e(TAG, "Message Not sent-Not Connected!");
        } catch (Exception e) {
            Log.e(TAG, "Message Not sent!" + e.getMessage());
        }

        //Store outgoing message in DB
          return null;
    }

    /**
     * Method to create Chat
     *
     * @param receiver
     */
    protected void createChat(String receiver) {
        Chat chat = ChatManager.getInstanceFor(getXMPPTCPConnection()).createChat(
                receiver + "@"
                        + AppConstants.XMPP_SERVER_NAME,
                XMPPManager.getMessageListener());
        xmppHelper.setChatCreated(true);
        xmppHelper.setChat(chat);

    }

    /**
     * Method to establish connection.
     *
     * @param caller
     */
    public void connect(final String caller) {

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {
                if (getXMPPTCPConnection().isConnected()) {
                    return false;
                }
                xmppHelper.setIsConnecting(true);
                Log.d("Connect() Function", caller + "=>connecting....");
                try {
                    getXMPPTCPConnection().connect();
                    xmppHelper.setConnected(true);
                } catch (IOException e) {
                    Log.e("(" + caller + ")", "IOException: " + e.getMessage());
                } catch (SmackException e) {
                    Log.e("(" + caller + ")",
                            "SMACKException: " + e.getMessage());
                } catch (XMPPException e) {
                    Log.e("connect(" + caller + ")",
                            "XMPPException: " + e.getMessage());

                }
                xmppHelper.setIsConnecting(false);
                return false;
            }
        };
        connectionThread.execute();
    }

    /**
     * Method to disconnect the connection
     */
    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getXMPPTCPConnection().disconnect();
            }
        }).start();
    }

    /**
     * Method to send Delivery confirmation
     *
     * @param message
     */
    public void sendMessageDeliveryConfirmation(Message message) {

        Stanza delStanza = new Message();
        delStanza.addExtension(new DeliveryReceipt(message.getStanzaId()));
        delStanza.setTo(message.getFrom());
        try {
            mXMPPTCPConnection.sendStanza(delStanza);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageDisplayedConfirmation(String from,String messageId) {

        Stanza readStanza = new Message();
        readStanza.addExtension(new DisplayReceipt(messageId));
        readStanza.setTo(from);
        try {
            mXMPPTCPConnection.sendStanza(readStanza);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    protected void setIncomingMessageStatusDelivered(String fromJid, String toJid, String receiptId, Stanza receipt) {
        if (messageStatusChangedListener != null) {
            Log.i("setMessageStatDeliver", "Executing callback");

//            ExtensionElement extensionElement=receipt.getExtension(DeliveryReceipt.NAMESPACE);

            DeliveryReceipt deliveryReceipt =(DeliveryReceipt) receipt.getExtension(DeliveryReceipt.NAMESPACE);
            String deliveredMessageId=deliveryReceipt.getId();

            //Update message status and time info in DB

            ChatDBHelper.getInstance().updateMessageDelivered(deliveredMessageId, new Date());
            messageStatusChangedListener.onMessageDelivered(fromJid, toJid, receiptId, receipt);
        }
    }

    protected void setIncomingMessageStatusDisplayed(String fromJid, String toJid, String receiptId, Stanza receipt) {
//        Log.i(TAG,"Incoming stanza "+receipt);
        if (messageStatusChangedListener != null) {
            Log.i("setMessageRead", "Executing callback");

            DisplayReceipt displayReceipt=(DisplayReceipt)receipt.getExtension(DisplayReceipt.NAMESPACE);
            String displayMessageId=displayReceipt.getId();

            //Update message status and time info in DB
            ChatDBHelper.getInstance().updateMessageDisplayed(displayMessageId, new Date());
            messageStatusChangedListener.onMessageDisplayed(fromJid, toJid, receiptId, receipt);
        }
    }

    protected void setOutgoingMessageStatusSent(Stanza packet) {
        if (messageStatusChangedListener != null) {
            Log.i("setMessageStatusSent", "callback");
            //Update message status and time info in DB
            ChatDBHelper.getInstance().updateMessageSent(packet.getStanzaId(), new Date());
            messageStatusChangedListener.onMessageSent(packet);
        }
    }

    protected void processIncomingMessage(Message message) {
        Log.i(TAG, "Is chatMessage null " + (message == null));

        sendMessageDeliveryConfirmation(message);

        Log.i(TAG, "Processing incoming message");

//        /Store Incoming message in DB
        Log.i(TAG, "Inserting values to database before sending message " + message.getStanzaId() + message.getBody() + RunTimeData.groupId + message.getFrom() + message.getTo());
        ChatDBHelper.getInstance().insertMessage(message.getStanzaId(), message.getBody(), RunTimeData.groupId, message.getFrom(), message.getTo());

        if (incomingMessageListener != null) {
            incomingMessageListener.onMessagereceived(message);
        }
    }

    protected void setConnectionSuccessful() {
        if (connectionListener != null) {
            Log.i(TAG + "onUserConnect", "Executing callback");
            connectionListener.onConnectionSuccessful();
        }
    }

    protected void setConnectionClosed(){
        if (connectionListener != null) {
            Log.i(TAG + "onUserConnect", "Executing callback");
            connectionListener.onConnectionClosed();
        }
    }

    protected void setConnectionClosedOnError(){
        if (connectionListener != null) {
            Log.i(TAG + "onUserConnect", "Executing callback");
            connectionListener.onConnectionClosedOnError();
        }
    }

    protected void setConnectionReconnected(){
        if (connectionListener != null) {
            Log.i(TAG + "onUserConnect", "Executing callback");
            connectionListener.onReconnectionSuccessful();
        }
    }


    /**
     * Send typing... status when the user starts typing.
     */
    public void setChatStateTyping() {
        if (mXMPPTCPConnection != null) {
            if (isChatCreated()) {
                try {
                    getChatStateManager().setCurrentState(ChatState.composing, getChat());
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Mark user offline, as he is ended the session
     */
    public void setChatStateOffline() {
        if (mXMPPTCPConnection != null) {
            if (isChatCreated()) {
                try {
                    getChatStateManager().setCurrentState(ChatState.gone, getChat());
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Send user's Online Presence
     */
    public void sendPresenceOnline() {

        Presence onlinePresence = new Presence(Presence.Type.available);
        onlinePresence.setStatus("Online");
        try {
            mXMPPTCPConnection.sendStanza(onlinePresence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Send user's offline Presence
     *
     * @param status
     */
    public void sendPresenceOffline(String status) {

        Presence offlinePresence = new Presence(Presence.Type.unavailable);
        offlinePresence.setStatus(status);
        try {
            mXMPPTCPConnection.sendStanza(offlinePresence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    protected void setIncomingTypingState(String status) {
        Log.i(" setIncomingTypingState", "Executing callback");
        if (userStatusChangeListener != null) {
            userStatusChangeListener.onTypingStatusReceived(status);
        }
    }
}
