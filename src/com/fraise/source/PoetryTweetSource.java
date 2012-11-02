package com.fraise.source;

import java.util.List;

import com.fraise.domain.LineSource;
import com.fraise.domain.PoetryTweet;

public interface PoetryTweetSource {

    /**
     * Retrieve the new {@link PoetryTweet} for the given {@link LineSource}.
     * 
     * @param lineSource
     *            The LineSource.
     * @return List of {@link PoetryTweet} objects.
     */
    List<PoetryTweet> getNewMessages(LineSource lineSource);

    /**
     * Retrieve all {@link PoetryTweet} for the given {@link LineSource}.
     * 
     * @param lineSource
     *            The LineSource.
     * @return List of {@link PoetryTweet} objects.
     */
    List<PoetryTweet> getAllMessages(LineSource lineSource);

    void refreshFollowers();

}
