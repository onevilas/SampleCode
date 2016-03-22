package com.nvy.chat.entity;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Vilas on 17/11/15.
 */
public class ChatMessage implements Serializable {

    private String body;
    private String sender;
    private String receiver;
    private String senderName;
    private String Date, Time;
    private String msgid;
    private String status;
    private boolean isMine;// Did I send the message.
    private int resourceID;
    private boolean isDeliveryReceiptSent=false;

    public ChatMessage(String Sender, String Receiver, String messageString,
                       String ID, boolean isMINE) {
        body = messageString;
        isMine = isMINE;
        sender = Sender;
        msgid = ID;
        receiver = Receiver;
        senderName = sender;
    }

    public boolean isDeliveryReceiptSent() {
        return isDeliveryReceiptSent;
    }

    public void setDeliveryReceiptSent(boolean isDeliveryReceiptSent) {
        this.isDeliveryReceiptSent = isDeliveryReceiptSent;
    }

    public void setMsgID() {
        msgid += "-" + String.format("%02d", new Random().nextInt(100));

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
