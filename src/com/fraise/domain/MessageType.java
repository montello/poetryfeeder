package com.fraise.domain;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class MessageType implements Serializable {

    private static final long serialVersionUID = 7057041392654770061L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long lastModified = 1;

    @Persistent
    private String name;

    public MessageType(String name) {
        this.name = name;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MessageType) {
            MessageType other = (MessageType) obj;
            if (other.getName().equals(this.getName())) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }
}
