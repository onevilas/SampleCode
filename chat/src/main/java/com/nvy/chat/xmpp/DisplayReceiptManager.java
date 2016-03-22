package com.nvy.chat.xmpp;

import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.filter.StanzaExtensionFilter;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by Vilas on 8/12/15.
 */
public class DisplayReceiptManager implements StanzaListener {

    private static Map<XMPPConnection, DisplayReceiptManager> instances =
            Collections.synchronizedMap(new WeakHashMap<XMPPConnection, DisplayReceiptManager>());
    private Set<ReadReceiptReceivedListener> receiptReceivedListeners =
            Collections.synchronizedSet(new HashSet<ReadReceiptReceivedListener>());

    static {
        XMPPConnectionRegistry.addConnectionCreationListener(new ConnectionCreationListener() {
            public void connectionCreated(XMPPConnection connection) {
                getInstanceFor(connection);
            }
        });
    }

    private DisplayReceiptManager(XMPPConnection connection)
    {
        ServiceDiscoveryManager sdm = ServiceDiscoveryManager.getInstanceFor(connection);
        sdm.addFeature(DisplayReceipt.NAMESPACE);
        instances.put(connection, this);

        connection.addAsyncStanzaListener(this, new StanzaExtensionFilter(DisplayReceipt.NAMESPACE));
    }

    public static synchronized DisplayReceiptManager getInstanceFor(XMPPConnection connection)
    {
        DisplayReceiptManager receiptManager = instances.get(connection);

        if (receiptManager == null)
        {
            receiptManager = new DisplayReceiptManager(connection);
        }

        return receiptManager;
    }
    @Override
    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {

        DisplayReceipt displayReceipt = (DisplayReceipt)packet.getExtension(DisplayReceipt.ELEMENT, DisplayReceipt.NAMESPACE);

        if (displayReceipt != null)
        {
            for (ReadReceiptReceivedListener listener : receiptReceivedListeners)
            {
                listener.onReceiptReceived(packet.getFrom(), packet.getTo(), displayReceipt.getId(), packet);
            }
        }
    }

    public void addDisplayedReceivedListener(ReadReceiptReceivedListener listener) {
        receiptReceivedListeners.add(listener);
    }

    public void removeDisplayedReceivedListener(ReadReceiptReceivedListener listener) {
        receiptReceivedListeners.remove(listener);
    }
}
