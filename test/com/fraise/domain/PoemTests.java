package com.fraise.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

public class PoemTests extends TestCase {

    public void testGetBookmark() {

        List<PoetryTweet> allTweets = new ArrayList<PoetryTweet>();
        allTweets.add(new PoetryTweet(1, "dark was the night", "cheryl", new Date(2010, 0, 1, 10, 0)));
        allTweets.add(new PoetryTweet(2, "riding on my big black rocket", "david", new Date(2010, 0, 2, 10, 0)));
        allTweets.add(new PoetryTweet(3, "Take my mind away from this", "cheryl", new Date(2010, 0, 3, 10, 0)));
        allTweets.add(new PoetryTweet(1, "blueprint despise and demise", "david", new Date(2010, 0, 4, 10, 0)));

        Poem testObject = new Poem(allTweets);

        assertNotNull(testObject.getLines());
        assertNotNull(testObject.getBookmark());
        assertEquals(new Date(2010, 0, 4, 10, 0), testObject.getBookmark());
    }
}
