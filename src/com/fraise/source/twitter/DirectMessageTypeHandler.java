package com.fraise.source.twitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.TwitterResponse;

import com.fraise.domain.MessageType;
import com.fraise.domain.PoetryTweet;

public class DirectMessageTypeHandler extends AbstractTwitterMessageTypeHandler {

    private TwitterWrapper twitterWrapper;

    public DirectMessageTypeHandler(TwitterWrapper twitterWrapper) {
        this.twitterWrapper = twitterWrapper;
    }

    @Override
    protected ResponseList<? extends TwitterResponse> getMessagesOfSomeType(int page, int pageSize)
            throws TwitterException {
        return twitterWrapper.getDirectMessages(new Paging(page, pageSize));
    }

    @Override
    protected ResponseList<? extends TwitterResponse> getMessagesOfSomeTypeSince(int page, int pageSize, long sinceId)
            throws TwitterException {
        return twitterWrapper.getDirectMessages(new Paging(page, pageSize, sinceId));
    }

    @Override
    protected Collection<? extends PoetryTweet> convertToPoetryTweets(ResponseList<? extends TwitterResponse> messages,
            MessageType messageType) {

        List<PoetryTweet> newMessages = new ArrayList<PoetryTweet>();
        if (messages != null) {
            for (TwitterResponse tr : messages) {
                DirectMessage message = (DirectMessage) tr;
                PoetryTweet pTweet = new PoetryTweet(message.getId(), message.getText(), message.getSenderScreenName(),
                        message.getCreatedAt());
                newMessages.add(pTweet);
            }
        }
        return newMessages;
    }
}
