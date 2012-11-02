package com.fraise.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fraise.domain.PoetryTweet;
import com.fraise.util.BaseTest;

public class GetPoetryByLetterTests extends BaseTest {

    private DataStorePoetryPersistenceManager testObject;

    public void setUp() throws Exception {
        super.setUp();
        testObject = new DataStorePoetryPersistenceManager();
        testObject.setPersistenceManager(newPersistenceManager());
        testObject.persistPoetryTweets(getMockPoetryTweets());
    }

    public void testGetLinesForLetterF() {
        List<PoetryTweet> fLines = testObject.getLinesByFirstLetter("f");
        assertEquals(2, fLines.size());
        assertEquals("flush out all false notions", fLines.get(0).getText());
        assertEquals("face aid through the cracks", fLines.get(1).getText());
    }

    private List<PoetryTweet> getMockPoetryTweets() {
        List<PoetryTweet> allTweets = new ArrayList<PoetryTweet>();
        allTweets.add(new PoetryTweet(1, "dark was the night", "cheryl", new Date()));
        allTweets.add(new PoetryTweet(2, "riding on my big black rocket", "david", new Date()));
        allTweets.add(new PoetryTweet(3, "Take my mind away from this noise", "cheryl", new Date()));
        allTweets.add(new PoetryTweet(4, "blueprint of despise and demise", "david", new Date()));
        allTweets.add(new PoetryTweet(5, "flush out all false notions", "david", new Date()));
        allTweets.add(new PoetryTweet(6, "face aid through the cracks", "cheryl", new Date()));
        return allTweets;
    }

}
