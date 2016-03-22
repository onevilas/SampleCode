package com.nvy.chat.xmpp;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Vilas on 10/12/15.
 *
 * Represents a <b>message display receipt request</b> entry
 */
public class DisplayReceiptRequest implements ExtensionElement {

    public static final String ELEMENT = "markable";

    @Override
    public String getNamespace() {
        return DisplayReceipt.NAMESPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    @Override
    public CharSequence toXML() {
        return "<markable xmlns='" + DisplayReceipt.NAMESPACE + "'/>";
    }

    /**
     * Get the {@link DisplayReceiptRequest} extension of the packet, if any.
     *
     * @param packet the packet
     * @return the {@link DisplayReceiptRequest} extension or {@code null}
     */
    public static DisplayReceiptRequest from(Stanza packet) {
        return packet.getExtension(ELEMENT, DisplayReceipt.NAMESPACE);
    }

    /**
     * Add a display receipt request to an outgoing packet.
     *
     * Only message packets may contain receipt requests as of XEP-0184,
     * therefore only allow Message as the parameter type.
     *
     * @param message Message object to add a request to
     * @return the Message ID which will be used as receipt ID
     */
    public static String addTo(Message message) {
        if (message.getStanzaId() == null) {
            message.setStanzaId(StanzaIdUtil.newStanzaId());
        }
        message.addExtension(new DisplayReceiptRequest());
        return message.getStanzaId();
    }

    /**
     * This Provider parses and returns DisplayReceiptRequest packets.
     */
    public static class Provider extends ExtensionElementProvider<DisplayReceiptRequest> {
        @Override
        public DisplayReceiptRequest parse(XmlPullParser parser,
                                            int initialDepth) throws XmlPullParserException,
                IOException {
            return new DisplayReceiptRequest();
        }
    }
}
