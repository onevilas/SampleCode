package com.nvy.chat.persistance;

/**
 * Created by Vilas on 11/12/15.
 */
public class Message {

    private String messageId;
    private String messageBody;
    private String messageStatus;
    private String messageFrom;
    private String messageTo;
    private String messageGroupId;
    private String messageSentTs;
    private String messageDeliveredTs;
    private String messageDisplayedTs;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    public String getMessageGroupId() {
        return messageGroupId;
    }

    public void setMessageGroupId(String messageGroupId) {
        this.messageGroupId = messageGroupId;
    }

    public String getMessageSentTs() {
        return messageSentTs;
    }

    public void setMessageSentTs(String messageSentTs) {
        this.messageSentTs = messageSentTs;
    }

    public String getMessageDeliveredTs() {
        return messageDeliveredTs;
    }

    public void setMessageDeliveredTs(String messageDeliveredTs) {
        this.messageDeliveredTs = messageDeliveredTs;
    }

    public String getMessageDisplayedTs() {
        return messageDisplayedTs;
    }

    public void setMessageDisplayedTs(String messageDisplayedTs) {
        this.messageDisplayedTs = messageDisplayedTs;
    }
}
