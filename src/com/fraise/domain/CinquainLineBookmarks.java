package com.fraise.domain;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class CinquainLineBookmarks {

    @PrimaryKey
    @Persistent
    private String key;

    @Persistent
    private Date bookmarkOne;
    @Persistent
    private Date bookmarkTwo;
    @Persistent
    private Date bookmarkThree;
    @Persistent
    private Date bookmarkFour;
    @Persistent
    private Date bookmarkFive;

    public CinquainLineBookmarks() {
    }

    public CinquainLineBookmarks(String key) {
        this.key = key;
    }

    public CinquainLineBookmarks(String key, Date date) {
        this.key = key;
        this.bookmarkOne = date;
        this.bookmarkTwo = date;
        this.bookmarkThree = date;
        this.bookmarkFour = date;
        this.bookmarkFive = date;
    }

    public Date getBookmarkOne() {
        return bookmarkOne;
    }

    public void setBookmarkOne(Date bookmarkOne) {
        this.bookmarkOne = bookmarkOne;
    }

    public Date getBookmarkTwo() {
        return bookmarkTwo;
    }

    public void setBookmarkTwo(Date bookmarkTwo) {
        this.bookmarkTwo = bookmarkTwo;
    }

    public Date getBookmarkThree() {
        return bookmarkThree;
    }

    public void setBookmarkThree(Date bookmarkThree) {
        this.bookmarkThree = bookmarkThree;
    }

    public Date getBookmarkFour() {
        return bookmarkFour;
    }

    public void setBookmarkFour(Date bookmarkFour) {
        this.bookmarkFour = bookmarkFour;
    }

    public Date getBookmarkFive() {
        return bookmarkFive;
    }

    public void setBookmarkFive(Date bookmarkFive) {
        this.bookmarkFive = bookmarkFive;
    }

    public String getKey() {
        return key;
    }

}
