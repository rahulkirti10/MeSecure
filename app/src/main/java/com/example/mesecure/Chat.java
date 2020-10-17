package com.example.mesecure;

public class Chat {
    String message,senderUid,receiverUid;
    public Chat(){

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public Chat(String message, String senderUid, String receiverUid) {
        this.message = message;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
    }
}
