package com.fraise.domain;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Poem {

    private List<PoetryTweet> lines;

    public Poem(List<PoetryTweet> poemLines) {
        this.lines = poemLines;
    }

    public List<PoetryTweet> getLines() {
        return lines;
    }

    public void setLines(List<PoetryTweet> lines) {
        this.lines = lines;
    }

    // TODO find a better solution.
    public Date getBookmark() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(1);
        Date bookmark = calendar.getTime();
        if (null != lines && lines.size() > 0) {
            for (PoetryTweet line : lines) {
                if (line.getAuthoredOn().after(bookmark)) {
                    bookmark = line.getAuthoredOn();
                }
            }
        }
        return bookmark;
    }

}
