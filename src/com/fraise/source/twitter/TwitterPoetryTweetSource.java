package com.fraise.source.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import twitter4j.User;

import com.fraise.domain.LineSource;
import com.fraise.domain.MessageType;
import com.fraise.domain.PoetryTweet;
import com.fraise.source.PoetryTweetSource;

public class TwitterPoetryTweetSource implements PoetryTweetSource {

    private static final Logger log = Logger.getLogger(TwitterPoetryTweetSource.class.getName());

    private DirectMessageTypeHandler directMessageTypeHandler;
    private StatusMessageTypeHandler statusMessageTypeHandler;
    private TwitterWrapper twitterWrapper;

    // private MentionMessageTypeHandler mentionMessageTypeHandler;

    /**
     * {@inheritDoc}
     */
    @Override
    // TODO, retest
    public List<PoetryTweet> getNewMessages(LineSource lineSource) {
        log.info("get new messages");
        List<PoetryTweet> result = new ArrayList<PoetryTweet>();
        // MessageType statusMessageType = lineSource.getMessageTypes().get(0);
        // result.addAll(statusMessageTypeHandler.getNewMessages(statusMessageType));
        MessageType directMessageType = lineSource.getMessageTypes().get(1);
        result.addAll(directMessageTypeHandler.getNewMessages(directMessageType));
        // MessageType mentionMessageType = lineSource.getMessageTypes().get(2);
        // result.addAll(mentionMessageTypeHandler.getNewMessages(mentionMessageType));
        log.info("new messages:" + result.size());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PoetryTweet> getAllMessages(LineSource lineSource) {
        List<PoetryTweet> result = new ArrayList<PoetryTweet>();
        // result.addAll(statusMessageTypeHandler.getAllMessages(lineSource.getMessageTypes().get(0)));
        result.addAll(directMessageTypeHandler.getAllMessages(lineSource.getMessageTypes().get(1)));
        // result.addAll(mentionMessageTypeHandler.getAllMessages(lineSource.getMessageTypes().get(2)));
        return result;
    }

    @Override
    public void refreshFollowers() {
        twitterWrapper.followUserFollowers();
    }

    public void setDirectMessageTypeHandler(DirectMessageTypeHandler directMessageTypeHandler) {
        this.directMessageTypeHandler = directMessageTypeHandler;
    }

    public void setStatusMessageTypeHandler(StatusMessageTypeHandler statusMessageTypeHandler) {
        this.statusMessageTypeHandler = statusMessageTypeHandler;
    }

    public void setTwitterWrapper(TwitterWrapper twitterWrapper) {
        this.twitterWrapper = twitterWrapper;
    }

    // public void setMentionMessageTypeHandler(MentionMessageTypeHandler
    // mentionMessageTypeHandler) {
    // this.mentionMessageTypeHandler = mentionMessageTypeHandler;
    // }

}
