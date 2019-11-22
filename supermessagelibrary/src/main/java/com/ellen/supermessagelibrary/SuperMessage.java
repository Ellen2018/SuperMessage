package com.ellen.supermessagelibrary;

import android.support.annotation.Nullable;

public class SuperMessage {
    /**
     * 消息发送方(可为null)
     */
    private String sendUserName;
    /**
     * 消息接收者(可为null)
     */
    private String receiveName;
    /**
     * 消息id
     */
    private String messageId;
    //消息内容
    public int what;
    public int arg1;
    public int arg2;
    public Object object;

    public SuperMessage(String messageId) {
        this.messageId = messageId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof SuperMessage){
            SuperMessage message = (SuperMessage) obj;
            return this.messageId.equals(message.messageId);
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
