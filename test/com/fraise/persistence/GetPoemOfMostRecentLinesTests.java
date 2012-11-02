package com.fraise.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fraise.domain.Language;
import com.fraise.domain.Paging;
import com.fraise.domain.PoetryTweet;
import com.fraise.util.BaseTest;

public class GetPoemOfMostRecentLinesTests extends BaseTest {

    private DataStorePoetryPersistenceManager testObject;

    public void setUp() throws Exception {
        super.setUp();
        testObject = new DataStorePoetryPersistenceManager();
        testObject.setPersistenceManager(newPersistenceManager());
        testObject.persistPoetryTweets(getMockPoetryTweets());
    }

    public void testGetMostRecent3() {

        Paging paging = new Paging(3);
        List<PoetryTweet> poetryLines = testObject.getMostRecent(paging, Language.EN);

        assertTrue(3 == poetryLines.size());
    }

    public void testGetMostRecent10() {

        Paging paging = new Paging(10);
        List<PoetryTweet> poetryLines = testObject.getMostRecent(paging, Language.EN);

        assertTrue(6 == poetryLines.size());
    }

    private List<PoetryTweet> getMockPoetryTweets() {
        List<PoetryTweet> allTweets = new ArrayList<PoetryTweet>();
        allTweets.add(new PoetryTweet(1, "dark was the night", "cheryl", new Date(2010, 0, 1, 10, 0)));
        allTweets.add(new PoetryTweet(2, "riding on my big black rocket", "david", new Date(2010, 0, 2, 10, 0)));
        allTweets.add(new PoetryTweet(3, "Take my mind away from this", "cheryl", new Date(2010, 0, 3, 10, 0)));
        allTweets.add(new PoetryTweet(1, "blueprint despise and demise", "david", new Date(2010, 0, 4, 10, 0)));
        allTweets.add(new PoetryTweet(2, "flush out all false notions", "david", new Date(2010, 0, 5, 10, 0)));
        allTweets.add(new PoetryTweet(3, "face aid through the cracks", "cheryl", new Date(2010, 0, 6, 10, 0)));
        return allTweets;
    }

}
