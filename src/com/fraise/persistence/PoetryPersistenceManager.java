package com.fraise.persistence;

import java.util.List;

import com.fraise.domain.Cinquain;
import com.fraise.domain.Language;
import com.fraise.domain.LineSource;
import com.fraise.domain.MessageType;
import com.fraise.domain.Paging;
import com.fraise.domain.Poem;
import com.fraise.domain.PoetryTweet;

public interface PoetryPersistenceManager {

    /**
     * Persist the list of {@link PoetryTweet} objects to the persistence
     * provider.
     * 
     * @param poetryTweets
     *            Tweets to persist.
     */
    public void persistPoetryTweets(List<PoetryTweet> poetryTweets);

    /**
     * Retrieve all the {@link PoetryTweet} objects in the persistence provider.
     * 
     * @return List of PoetryTweet objects.
     */
    public List<PoetryTweet> getAllTweets();

    /**
     * TODO
     * 
     * @param author
     * @return
     */
    public List<PoetryTweet> getTweetsByAuthor(String author);

    /**
     * TODO
     * 
     * @param author
     * @param paging
     * @return
     */
    public List<PoetryTweet> getTweetsByAuthor(String author, Paging paging);

    /**
     * TODO
     * 
     * @param letter
     * @return
     */
    public List<PoetryTweet> getLinesByFirstLetter(String letter);

    /**
     * TODO
     * 
     * @return
     */
    public List<PoetryTweet> getLinesInDateOrder();

    /**
     * Retrieve the {@link LineSource} for the provided name.
     * 
     * @param lineSourceName
     *            The matching LineSource if it exists.
     * @return
     */
    public LineSource getLineSource(String lineSourceName);

    /**
     * TODO
     * 
     * @param paging
     * @param language
     *            TODO
     * @return
     */
    public List<PoetryTweet> getMostRecent(Paging paging, Language language);

    /**
     * TODO
     * 
     * @param numberOfPoems
     * @param paging
     * @param language
     *            TODO
     * @return
     */
    public List<Poem> getArchivedPoems(int numberOfPoems, Paging paging, Language language);

    /**
     * Register a new {@link LineSource} in the persistence provider.
     * 
     * @param lineSource
     *            The LineSource to register.
     */
    public void registerLineSource(LineSource lineSource);

    /**
     * Update the LMI (last modified ID) for the MessageType instance.
     * 
     * @param messageType
     *            MessageType being updated.
     * @param lastModified
     *            The new LMI.
     */
    public void updateLastModifiedForMessageType(MessageType messageType, long lastModified);

    public void collateAndPersistCinquain(Language language);

    public List<Cinquain> getCinquain(Paging paging, Language language);

    public List<Cinquain> getCinquain(int count, Paging paging, Language language);

    public void setup();

    public void cleanup(String byName);

}