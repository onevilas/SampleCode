package com.nvy.chat.xmpp;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.EmbeddedExtensionProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Vilas on 8/12/15.
 *
 * Class for Displayed Receipt
 */
public class DisplayReceipt implements ExtensionElement{

    public static final String NAMESPACE = "urn:xmpp:chat-markers:0";
    public static final String ELEMENT = "displayed";

    //Stanza Id of displayed message
    private String id;

    public DisplayReceipt(String sid){
        this.id=sid;
    }

    public String getId(){
        return this.id;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    @Override
    public CharSequence toXML() {
         return "<displayed xmlns='" + NAMESPACE + "' id='" + id + "'/>";
    }

    public static class ReadProvider extends EmbeddedExtensionProvider<DisplayReceipt>{

        @Override
        protected DisplayReceipt createReturnExtension(String currentElement, String currentNamespace, Map attributeMap, List content) {
            String eid=attributeMap.get("id").toString();
            return new DisplayReceipt(eid);
        }
    }
}
