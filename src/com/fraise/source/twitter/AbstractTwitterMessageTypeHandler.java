package com.fraise.source.twitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.mortbay.log.Log;

import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.TwitterResponse;

import com.fraise.domain.MessageType;
import com.fraise.domain.PoetryTweet;
import com.fraise.persistence.PoetryPersistenceManager;

public abstract class AbstractTwitterMessageTypeHandler {

    private static final Logger log = Logger.getLogger(AbstractTwitterMessageTypeHandler.class.getName());

    protected PoetryPersistenceManager persistenceManager;
    private int pageSize = 50;

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPersistenceManager(PoetryPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    protected abstract ResponseList<? extends TwitterResponse> getMessagesOfSomeType(int page, int pageSize)
            throws TwitterException;

    protected abstract ResponseList<? extends TwitterResponse> getMessagesOfSomeTypeSince(int page, int pageSize,
            long sinceId) throws TwitterException;

    protected abstract Collection<? extends PoetryTweet> convertToPoetryTweets(
            ResponseList<? extends TwitterResponse> messages, MessageType messageType);

    public List<PoetryTweet> getAllMessages(MessageType messageType) {
        List<PoetryTweet> result = new ArrayList<PoetryTweet>();
        try {
            int page = 1;
            ResponseList<? extends TwitterResponse> someMessages = null;
            do {
                someMessages = getMessagesOfSomeType(page, pageSize);
                result.addAll(convertToPoetryTweets(someMessages, messageType));
                page++;
            } while (someMessages.size() == pageSize);

        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<PoetryTweet> getNewMessages(MessageType messageType) {
        List<PoetryTweet> result = new ArrayList<PoetryTweet>();
        try {
            int page = 1;
            ResponseList<? extends TwitterResponse> someMessages = null;
            long lmi = messageType.getLastModified();
            do {
                log.info("Get new messages for: " + messageType.getName() + " since: " + lmi);
                someMessages = getMessagesOfSomeTypeSince(page, pageSize, messageType.getLastModified());
                Collection<? extends PoetryTweet> poetryTweets = convertToPoetryTweets(someMessages, messageType);
                result.addAll(poetryTweets);
                page++;
                long upperLmi = getLastModified(poetryTweets);
                lmi = upperLmi > lmi ? upperLmi : lmi;
            } while (someMessages.size() == pageSize);
            persistenceManager.updateLastModifiedForMessageType(messageType, lmi);
        } catch (TwitterException e) {
            System.out.println(e.getCause());
            log.info(e.getMessage());
            log.info(e.getLocalizedMessage());
        }
        log.info("Retrieved: " + result.size() + " " + messageType.getName() + "'s");
        return result;
    }

    private long getLastModified(Collection<? extends PoetryTweet> poetryTweets) {
        long highestLmi = 1;
        for (PoetryTweet tweet : poetryTweets) {
            highestLmi = tweet.getSourceId() > highestLmi ? tweet.getSourceId() : highestLmi;
        }
        return highestLmi;
    }
}
