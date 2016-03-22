package com.nvy.chat.xmpp;

import android.util.Log;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * Created by Vilas on 9/12/15.
 *
 * Class to handle all connection related events
 */
public class XMPPConnectionListener implements ConnectionListener {

    private final String TAG = "XMPPConctnListenr";

    XMPPHelper mXmppHelper;
    XMPPTCPConnection mConnection;
    ChatEventManagerListener mChatManagerListener;

    String mUsername;
    String mPassword;
    public XMPPConnectionListener(XMPPTCPConnection connection, ChatEventManagerListener chatManagerListener, XMPPHelper xmppHelper, String userName, String password){
        mXmppHelper=xmppHelper;
        mConnection=connection;
        mChatManagerListener=chatManagerListener;
        mUsername=userName;
        mPassword=password;
    }

    @Override
    public void connected(XMPPConnection connection) {
        Log.d("xmpp", "Connected!");
        mXmppHelper.setConnected(true);

        if (!connection.isAuthenticated()) {
            login();
        }
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.d("xmpp", "Authenticated!");
        mXmppHelper.setLoggedin(true);
        ChatManager.getInstanceFor(connection).addChatListener(mChatManagerListener);
        mXmppHelper.setChatCreated(false);
        mXmppHelper.setIsAuthenticated(true);
        //Notify UI on successfull connection

        mXmppHelper.setConnectionSuccessful();
        Log.i(TAG, "Connected!");
    }



    @Override
    public void connectionClosed() {
        Log.d("xmpp", "ConnectionCLosed!");
        mXmppHelper.setConnected(false);
        mXmppHelper.setChatCreated(false);
        mXmppHelper.setLoggedin(false);
        mXmppHelper.setIsAuthenticated(false);
        mXmppHelper.setConnectionClosed();
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d("xmpp", "ConnectionClosedOn Error!");;
        mXmppHelper.setConnected(false);
        mXmppHelper.setChatCreated(false);
        mXmppHelper.setLoggedin(false);
        mXmppHelper.setIsAuthenticated(false);
        mXmppHelper.setConnectionClosedOnError();
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d("xmpp", "ReconnectionSuccessful");;
        mXmppHelper.setConnected(true);
        mXmppHelper.setChatCreated(false);
        mXmppHelper.setLoggedin(false);
        mXmppHelper.setConnectionReconnected();
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d("xmpp", "Reconnectingin " + seconds);
        mXmppHelper.setLoggedin(false);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d("xmpp", "ReconnectionFailed!");
        mXmppHelper.setConnected(false);
        mXmppHelper.setChatCreated(false);
        mXmppHelper.setLoggedin(false);
    }

    /**
     * Method to login
     */
    public void login() {

        try {
            mConnection.login(mUsername, mPassword);

            Log.i(TAG + " LOGIN", "Connected to the Xmpp server!");

        } catch (XMPPException | SmackException | IOException e) {
            mXmppHelper.setConnectionClosedOnError();
            e.printStackTrace();
        } catch (Exception e) {
            mXmppHelper.setConnectionClosedOnError();
        }
    }
}
