package com.fraise.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fraise.domain.PoetryTweet;
import com.fraise.util.BaseTest;

public class GetPoetryInDateOrderTests extends BaseTest {

    private DataStorePoetryPersistenceManager testObject;

    public void setUp() throws Exception {
        super.setUp();
        testObject = new DataStorePoetryPersistenceManager();
        testObject.setPersistenceManager(newPersistenceManager());
        testObject.persistPoetryTweets(getMockPoetryTweets());
    }

    public void testGetLinesInDateOrder() {
        List<PoetryTweet> fLines = testObject.getLinesInDateOrder();
        assertEquals(6, fLines.size());
        assertEquals("riding on my big black rocket", fLines.get(0).getText());
        assertEquals("dark was the night", fLines.get(5).getText());
    }

    private List<PoetryTweet> getMockPoetryTweets() {
        List<PoetryTweet> allTweets = new ArrayList<PoetryTweet>();
        Calendar latest = Calendar.getInstance();
        latest.add(Calendar.YEAR, 10);
        Date laterTimeInserted = latest.getTime();

        allTweets.add(new PoetryTweet(1, "dark was the night", "cheryl", laterTimeInserted));
        Date timeInserted = Calendar.getInstance().getTime();
        allTweets.add(new PoetryTweet(2, "riding on my big black rocket", "david", timeInserted));
        allTweets.add(new PoetryTweet(3, "Take my mind away from this noise", "cheryl", timeInserted));
        allTweets.add(new PoetryTweet(4, "blueprint of despise and demise", "david", timeInserted));
        allTweets.add(new PoetryTweet(5, "flush out all false notions", "david", timeInserted));
        allTweets.add(new PoetryTweet(6, "face aid through the cracks", "cheryl", timeInserted));
        return allTweets;
    }

}
