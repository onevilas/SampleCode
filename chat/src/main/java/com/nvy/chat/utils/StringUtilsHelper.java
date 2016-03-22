package com.nvy.chat.utils;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by Vilas on 25/11/15.
 */
public class StringUtilsHelper extends StringUtils {

    /**
     * Returns the name portion of a XMPP address. For example, for the
     * address "vilas@little.com/Smack", "vilas" would be returned. If no
     * username is present in the address, the empty string will be returned.
     *
     * @param XMPPAddress the XMPP address.
     * @return the name portion of the XMPP address.
     */
    public static String parseName(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int atIndex = XMPPAddress.lastIndexOf("@");
        if (atIndex <= 0) {
            return "";
        }
        else {
            return XMPPAddress.substring(0, atIndex);
        }
    }
    /**
     * Returns the server portion of a XMPP address. For example, for the
     * address "vilas@little.com/Smack", "little.com" would be returned.
     * If no server is present in the address, the empty string will be returned.
     *
     * @param XMPPAddress the XMPP address.
     * @return the server portion of the XMPP address.
     */
    public static String parseServer(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int atIndex = XMPPAddress.lastIndexOf("@");
        // If the String ends with '@', return the empty string.
        if (atIndex + 1 > XMPPAddress.length()) {
            return "";
        }
        int slashIndex = XMPPAddress.indexOf("/");
        if (slashIndex > 0 && slashIndex > atIndex) {
            return XMPPAddress.substring(atIndex + 1, slashIndex);
        }
        else {
            return XMPPAddress.substring(atIndex + 1);
        }
    }
    /**
     * Returns the resource portion of a XMPP address. For example, for the
     * address "vilas@little.com/Smack", "Smack" would be returned. If no
     * resource is present in the address, the empty string will be returned.
     *
     * @param XMPPAddress the XMPP address.
     * @return the resource portion of the XMPP address.
     */
    public static String parseResource(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int slashIndex = XMPPAddress.indexOf("/");
        if (slashIndex + 1 > XMPPAddress.length() || slashIndex < 0) {
            return "";
        }
        else {
            return XMPPAddress.substring(slashIndex + 1);
        }
    }
    /**
     * Returns the XMPP address with any resource information removed. For example,
     * for the address "vilas@little.com/Smack", "vilas@little.com" would
     * be returned.
     *
     * @param XMPPAddress the XMPP address.
     * @return the bare XMPP address without resource information.
     */
    public static String parseFullJID(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int slashIndex = XMPPAddress.indexOf("/");
        if (slashIndex < 0) {
            return XMPPAddress;
        }
        else if (slashIndex == 0) {
            return "";
        }
        else {
            return XMPPAddress.substring(0, slashIndex);
        }
    }

    /**
     * Returns true if jid is a full JID (i.e. a JID with resource part).
     *
     * @param jid
     * @return true if full JID, false otherwise
     */
    public static boolean isFullJID(String jid) {
        if (parseName(jid).length() <= 0 || parseServer(jid).length() <= 0
                || parseResource(jid).length() <= 0) {
            return false;
        }
        return true;
    }
}
