package com.fraise.domain;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class CinquainTests {

    @Test
    public void notValidForIncorrectLineLength() {
        Cinquain cinquain = new Cinquain(Language.en);
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one", "david", new Date())));
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one two", "david", new Date())));
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one two three", "david", new Date())));
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one two three four", "david", new Date())));
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one two", "david", new Date())));

        assertFalse(cinquain.isValid());

        cinquain = new Cinquain(Language.en);
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one", "david", new Date())));
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one", "david", new Date())));
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one two three", "david", new Date())));
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one two three four", "david", new Date())));
        cinquain.addLine(new CinquainLine(new PoetryTweet(1111l, "one two", "david", new Date())));

        assertFalse(cinquain.isValid());

    }

}
