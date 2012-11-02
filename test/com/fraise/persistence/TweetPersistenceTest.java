package com.fraise.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.runner.RunWith;

import com.fraise.domain.LineSource;
import com.fraise.domain.MessageType;
import com.fraise.domain.PoetryTweet;
import com.fraise.util.BaseTest;

public class TweetPersistenceTest extends BaseTest {

    private DataStorePoetryPersistenceManager testObject;

    public void setUp() throws Exception {
        super.setUp();
        testObject = new DataStorePoetryPersistenceManager();
    }

    public void testPopulateDatastoreAndRetrieveAllEntries() {
        List<PoetryTweet> poetryTweets = getMockTwitterTweets();

        testObject.setPersistenceManager(newPersistenceManager());

        testObject.persistPoetryTweets(poetryTweets);

        List<PoetryTweet> result = testObject.getAllTweets();

        assertNotNull(result);
        assertEquals(3, result.size());
        PoetryTweet tweet0 = result.get(0);
        assertEquals("dark was the night", tweet0.getText());
        assertEquals("david", tweet0.getAuthor());
        assertEquals(new Character('d'), tweet0.getFirstLetter());
        assertTrue(4 == tweet0.getTextLength());
    }

    public void testRetrieveLastModifiedInformation() {
        testObject.setPersistenceManager(newPersistenceManager());

        LineSource lineSource = new LineSource("poetryfeeder");
        lineSource.addMessageType(new MessageType("DirectMessage"));

        testObject.registerLineSource(lineSource);
        LineSource account = testObject.getLineSource("poetryfeeder");
        assertNotNull(account);
        assertTrue(1 == account.getMessageTypes().get(0).getLastModified());
        assertEquals("poetryfeeder", account.getName());
    }

    public void testUpdateLastModifiedAccountInfo() {
        testObject.setPersistenceManager(newPersistenceManager());

        LineSource lineSource = new LineSource("poetryfeeder");
        lineSource.addMessageType(new MessageType("DirectMessage"));
        testObject.registerLineSource(lineSource);

        lineSource = testObject.getLineSource("poetryfeeder");
        assertNotNull(lineSource);
        assertTrue(1 == lineSource.getMessageTypes().get(0).getLastModified());
        testObject.updateLastModifiedForMessageType(lineSource.getMessageTypes().get(0), 1001L);

        assertTrue(1001L == lineSource.getMessageTypes().get(0).getLastModified());

    }

    public void testFindDuplicates() {
        testObject.cleanup("john");
    }

    private List<PoetryTweet> getMockTwitterTweets() {

        List<PoetryTweet> tweets = new ArrayList<PoetryTweet>();

        PoetryTweet tweet0 = new PoetryTweet(1, "@poetryfeeder dark was the night", "david", new Date());
        PoetryTweet tweet1 = new PoetryTweet(2, "@poetryfeeder riding on my big black rocket", "david", new Date());
        PoetryTweet tweet2 = new PoetryTweet(3, "@poetryfeeder Take my mind away from this noise", "john", new Date());
        PoetryTweet tweet2Duplicate = new PoetryTweet(3, "@poetryfeeder Take my mind away from this noise", "john",
                new Date());

        tweets.add(tweet0);
        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet2Duplicate);

        return tweets;
    }

}
