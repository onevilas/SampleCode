package com.nvy.chat.xmpp;

import android.content.Context;

import com.nvy.chat.persistance.ChatDBHelper;
import com.nvy.chat.persistance.ChatDatabase;
import com.nvy.chat.utils.AppConstants;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;

/**
 * Created by Vilas on 17/11/15.
 * <p/>
 * Class to handle all xmpp related operations, which is part of Service class.
 */
public class XMPPManager {

    private final String TAG = "XMPPConnection";

    Context context;

    private String mUsername;
    private String mPassword;

    private static XMPPTCPConnection mConnection;
    private static XMPPManager mConnectionInstance = null;
    private static ChatEventManagerListener mChatManagerListener;
    private static XMPPIncomingMessageListener mMessageListener;
    private static ChatStateManager chatStateManager;
    private static DeliveryReceiptManager mDeliveryManager;
    private XMPPHelper xmppHelper;

    private static ChatDatabase sChatDatabase;
    private ChatDBHelper mChatDBHelper;

    public static XMPPIncomingMessageListener getMessageListener() {
        return mMessageListener;
    }

    /**
     * Singleton class initialization
     */
    public static XMPPManager getInstance(Context context,
                                          String user, String pass) {
        if (mConnectionInstance == null) {
            mConnectionInstance = new XMPPManager(context, user, pass);

        }
        return mConnectionInstance;
    }

    /**
     * Constructor
     */
    public XMPPManager(Context context, String logiUser,
                       String password) {
        this.mUsername = logiUser;
        this.mPassword = password;
        this.context = context;

        init();
    }



    //Keep the connection alive

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (ClassNotFoundException ex) {
            // problem loading reconnection manager
        }
    }

    private void init() {
        xmppHelper = XMPPHelper.getInstance();
        sChatDatabase=ChatDatabase.getInstance(this.context);

        mChatDBHelper=ChatDBHelper.createInstance();
        mChatDBHelper.setChatDatabase(sChatDatabase);

        mMessageListener = new XMPPIncomingMessageListener(xmppHelper);
        mChatManagerListener = new ChatEventManagerListener(mMessageListener);
        initializeConnection();
    }

    /**
     * Initialize the connection
     */
    private void initializeConnection() {

        final XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                .builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(AppConstants.XMPP_SERVER_NAME);
//        config.setConnectTimeout(10);
//        config.setHost(AppConstants.XMPP_SERVER_NAME);
        config.setPort(AppConstants.PORT);
        config.setDebuggerEnabled(true);
        XMPPTCPConnection.setUseStreamManagementResumptionDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);
        mConnection = new XMPPTCPConnection(config.build());

        XMPPConnectionListener connectionListener =
                new XMPPConnectionListener(mConnection, mChatManagerListener, xmppHelper, mUsername, mPassword);
        mConnection.addConnectionListener(connectionListener);

        mConnection.setUseStreamManagement(true);
        mConnection.setUseStreamManagementResumption(true);
        mConnection.setPreferredResumptionTime(5);//Seconds

//        mConnection.addRequestAckPredicate()

        //Used for mark message sent, find other way.

        mConnection.addPacketSendingListener(
                new IncomingStanzaListener(mConnection, xmppHelper), MessageTypeFilter.CHAT);

        chatStateManager = ChatStateManager.getInstance(mConnection);

        mDeliveryManager = DeliveryReceiptManager.getInstanceFor(mConnection);

        mDeliveryManager.addReceiptReceivedListener(new MessageReceiptReceivedListener(xmppHelper));

        ReconnectionManager reconnectionManager=ReconnectionManager.getInstanceFor(mConnection);
        reconnectionManager.enableAutomaticReconnection();

//        DisplayReceiptManager displayReceiptManager=DisplayReceiptManager.getInstanceFor(mConnection);
//        displayReceiptManager.addDisplayedReceivedListener(new MessageDisplayedReceiptListener(xmppHelper));


//        mDeliveryManager.autoAddDeliveryReceiptRequests();
//        mDeliveryManager.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);

        ProviderManager.addExtensionProvider(
                DisplayReceipt.ELEMENT, DisplayReceipt.NAMESPACE, new DisplayReceipt.ReadProvider());

        DisplayReceiptManager.getInstanceFor(mConnection)
                .addDisplayedReceivedListener(new MessageDisplayedReceiptListener(xmppHelper));

        xmppHelper.setXMPPTCPConnection(mConnection);
        xmppHelper.setChatStateManager(chatStateManager);

//        ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
//        ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, new DeliveryReceiptRequest().getNamespace(), new DeliveryReceiptRequest.Provider());
    }

    /**
     * Method to disconnect the connection
     */
    public void disconnect() {
        xmppHelper.disconnect();
    }

    public void connect(String caller){
        xmppHelper.connect(caller);
    }


}
