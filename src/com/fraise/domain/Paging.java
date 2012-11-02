package com.fraise.domain;

import java.util.Date;

public class Paging {

    private int pageNumber;
    private int poemSize;
    private Date bookmark;

    public Paging(int poemSize) {
        this.poemSize = poemSize;
    }

    public Paging(int poemSize, Date bookmark) {
        this.poemSize = poemSize;
        this.bookmark = bookmark;
    }

    public Date getBookmark() {
        return bookmark;
    }

    public void setBookmark(Date bookmark) {
        this.bookmark = bookmark;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPoemSize() {
        return poemSize;
    }

    public void setPoemSize(int poemSize) {
        this.poemSize = poemSize;
    }

}
