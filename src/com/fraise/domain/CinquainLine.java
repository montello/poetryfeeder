package com.fraise.domain;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class CinquainLine {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long sourceId;

    @Persistent
    private String text;

    @Persistent
    private int textLength;

    @Persistent
    private String author;

    @Persistent
    private Date authoredOn;

    @Persistent
    private String language;

    public CinquainLine() {
    }

    public CinquainLine(PoetryTweet source) {
        this.sourceId = source.getSourceId();
        this.text = source.getText();
        this.textLength = source.getTextLength();
        this.author = source.getAuthor();
        this.authoredOn = source.getAuthoredOn();
        this.language = source.getLanguage();
    }

    public long getSourceId() {
        return sourceId;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public Date getAuthoredOn() {
        return authoredOn;
    }

    public String getLanguage() {
        return language;
    }

    public Key getKey() {
        return key;
    }

    public int getTextLength() {
        return textLength;
    }

}
