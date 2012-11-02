package com.fraise.domain;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class LineSource implements Serializable {

    private static final long serialVersionUID = -2028834906339589894L;

    @PrimaryKey
    @Persistent
    private String name;

    @Persistent
    private ArrayList<MessageType> messageTypes;

    public LineSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addMessageType(MessageType messageType) {
        if (messageTypes == null) {
            messageTypes = new ArrayList<MessageType>();
        }
        messageTypes.add(messageType);
    }

    public ArrayList<MessageType> getMessageTypes() {
        return messageTypes;
    }

}
