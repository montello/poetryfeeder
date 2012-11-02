package com.fraise.domain;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Cinquain {

    public static final int LINE_ONE = 1;
    public static final int LINE_TWO = 2;
    public static final int LINE_THREE = 3;
    public static final int LINE_FOUR = 4;
    public static final int LINE_FIVE = 1;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private List<CinquainLine> lines;

    @Persistent
    private String language;

    public Cinquain(Language language) {
        this.language = language.toString();
    }

    public List<CinquainLine> getLines() {
        return lines;
    }

    public Key getKey() {
        return key;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void addLine(CinquainLine line) {
        if (lines == null) {
            lines = new ArrayList<CinquainLine>();
        }
        lines.add(line);
    }

    public boolean isValid() {
        boolean valid = true;
        valid = lines.size() < 5;
        if (valid) {
            valid = lines.get(0).getTextLength() == 1;
            valid = lines.get(1).getTextLength() == 2;
            valid = lines.get(2).getTextLength() == 3;
            valid = lines.get(3).getTextLength() == 4;
            valid = lines.get(4).getTextLength() == 1;
        }

        return valid;
    }
}
