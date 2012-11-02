package com.fraise.source;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import twitter4j.TwitterException;

import com.fraise.domain.LineSource;
import com.fraise.domain.MessageType;
import com.fraise.domain.PoetryTweet;
import com.fraise.persistence.DataStorePoetryPersistenceManager;
import com.fraise.source.twitter.AccountAccessToken;
import com.fraise.source.twitter.ApplicationToken;
import com.fraise.source.twitter.DirectMessageTypeHandler;
import com.fraise.source.twitter.StatusMessageTypeHandler;
import com.fraise.source.twitter.TwitterPoetryTweetSource;
import com.fraise.source.twitter.TwitterWrapper;
import com.fraise.util.BaseTest;

public class TwitterSourceIntegrationTests extends BaseTest {

    private TwitterPoetryTweetSource testObject;

    private DataStorePoetryPersistenceManager pfPersistenceManager;

    private TwitterWrapper twitterForPF;
    private String consumerKey = "o7Mni3cZVXbkDupXDqK6ag";
    private String consumerSecret = "uUrbyPxOMPmIknY2ydrIrSShlvXWBnXB3iSXxUUqVk";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        testObject = new TwitterPoetryTweetSource();
        pfPersistenceManager = new DataStorePoetryPersistenceManager();
        pfPersistenceManager.setPersistenceManager(newPersistenceManager());

        setUpPoetryFeederTwitter();
    }

    @Before
    public void setUpPoetryFeederTwitter() {
        String tokenPF = "51423404-C24silbhobslmrFK1sgnWeQPSU395yOfuiI8soanz";
        String tokenSecretPF = "Bi9foBXG3O5vG8BEY7JyOLOEsd0kbkSHoEfw5i28";

        twitterForPF = new TwitterWrapper(new ApplicationToken("poetryfeeder", consumerKey, consumerSecret),
                new AccountAccessToken("poetryfeeder", tokenPF, tokenSecretPF));
        testObject.setTwitterWrapper(twitterForPF);
    }

    // TODO SOCs? persistency mixed with source
    // twitteraccount:poetryfeeder
    @Test
    public void testFindAllMessages() throws TwitterException {

        DirectMessageTypeHandler directMessageTypeHandler = new DirectMessageTypeHandler(twitterForPF);
        directMessageTypeHandler.setPersistenceManager(pfPersistenceManager);
        StatusMessageTypeHandler statusMessageTypeHandler = new StatusMessageTypeHandler(twitterForPF);
        statusMessageTypeHandler.setPersistenceManager(pfPersistenceManager);

        testObject.setDirectMessageTypeHandler(directMessageTypeHandler);
        testObject.setStatusMessageTypeHandler(statusMessageTypeHandler);

        LineSource lineSource = new LineSource("poetryfeeder");
        lineSource.addMessageType(new MessageType("DirectMessage"));
        lineSource.addMessageType(new MessageType("StatusMessage"));
        pfPersistenceManager.registerLineSource(lineSource);

        List<PoetryTweet> messages = testObject.getAllMessages(lineSource);

        for (PoetryTweet message : messages) {
            System.out.println(message.getSourceId() + ":" + message.getText() + ":" + message.getAuthoredOn());
        }
        System.out.println("***");

    }

    @Test
    public void testFindNewMessages() throws TwitterException {
        DirectMessageTypeHandler directMessageTypeHandler = new DirectMessageTypeHandler(twitterForPF);
        directMessageTypeHandler.setPersistenceManager(pfPersistenceManager);
        StatusMessageTypeHandler statusMessageTypeHandler = new StatusMessageTypeHandler(twitterForPF);
        statusMessageTypeHandler.setPersistenceManager(pfPersistenceManager);

        testObject.setDirectMessageTypeHandler(directMessageTypeHandler);
        testObject.setStatusMessageTypeHandler(statusMessageTypeHandler);

        LineSource lineSource = new LineSource("poetryfeeder");
        MessageType statusMessageType = new MessageType("StatusMessage");
        statusMessageType.setLastModified(13788126688l);
        lineSource.addMessageType(statusMessageType);
        MessageType directMessageType = new MessageType("DirectMessage");
        directMessageType.setLastModified(1214696608l);
        lineSource.addMessageType(directMessageType);
        pfPersistenceManager.registerLineSource(lineSource);

        List<PoetryTweet> messages = testObject.getNewMessages(lineSource);

        System.out.println(messages.size());

        for (PoetryTweet message : messages) {
            System.out.println(message.getSourceId() + ":" + message.getText());
        }
    }
}
