package com.fraise.control;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.fraise.domain.Cinquain;
import com.fraise.domain.CinquainLineBookmarks;
import com.fraise.domain.Language;
import com.fraise.domain.LineSource;
import com.fraise.domain.MessageType;
import com.fraise.domain.Paging;
import com.fraise.domain.Poem;
import com.fraise.domain.PoetryTweet;
import com.fraise.domain.SortOrder;
import com.fraise.persistence.PoetryPersistenceManager;
import com.fraise.source.PoetryTweetSource;

public class PoetryFeederManager {

    // make this configurable, allow for multiple LineSource instances
    private static final String POETRYFEEDER_TWITTER_LINE_SOURCE = "poetryfeeder";
    private static final Logger log = Logger.getLogger(PoetryFeederManager.class.getName());

    private PoetryPersistenceManager persistenceManager;
    private PoetryTweetSource source;
    private PoetrySorter poetrySorter;

    // TODO clean this up, move persistence logic to persistence layer
    public void retrieveAndPersistNewPoetryLines() {
        log.info("Retrieve and persist new lines");
        LineSource lineSource = persistenceManager.getLineSource(POETRYFEEDER_TWITTER_LINE_SOURCE);
        if (lineSource == null) {
            log.info("** Creating new LineSource");
            LineSource newLineSource = new LineSource(POETRYFEEDER_TWITTER_LINE_SOURCE);
            // newLineSource.addMessageType(new MessageType("statusMessage"));
            newLineSource.addMessageType(new MessageType("directMessage"));
            // newLineSource.addMessageType(new MessageType("mentionMessage"));
            persistenceManager.registerLineSource(newLineSource);
            lineSource = persistenceManager.getLineSource(POETRYFEEDER_TWITTER_LINE_SOURCE);
        }
        List<PoetryTweet> tweets = source.getNewMessages(lineSource);
        persistenceManager.persistPoetryTweets(tweets);
    }

    public List<PoetryTweet> getRecentSortedBy(SortOrder sortOrder, Paging paging, Language language) {
        List<PoetryTweet> mostRecent = persistenceManager.getMostRecent(paging, language);
        System.out.println("language:" + language.toString());
        return poetrySorter.sortPoem(mostRecent, sortOrder);
    }

    public List<Poem> getArchivedPoems(int numberOfPoems, Paging paging, SortOrder sortOrder, Language language) {
        List<Poem> poems = persistenceManager.getArchivedPoems(numberOfPoems, paging, language);
        return poetrySorter.sortPoems(poems, sortOrder);
    }

    public List<Cinquain> getCinquant(int count, Paging paging, Language language) {
        return persistenceManager.getCinquain(count, paging, language);
    }

    public void collateAndPersistCinquain() {
        persistenceManager.collateAndPersistCinquain(Language.en);
        persistenceManager.collateAndPersistCinquain(Language.nl);
    }

    public void refreshFollowers() {
        source.refreshFollowers();
    }

    public List<PoetryTweet> getLinesByAuthor(String author) {
        return persistenceManager.getTweetsByAuthor(author);
    }

    // Ripe for the slaughter
    public List<PoetryTweet> getAllLinesOfPoetry() {
        return persistenceManager.getAllTweets();
    }

    public List<PoetryTweet> getLinesByLetter(String letter) {
        return persistenceManager.getLinesByFirstLetter(letter);
    }

    public List<PoetryTweet> getLinesByDate() {
        return persistenceManager.getLinesInDateOrder();
    }

    // Ripe for the slaughter

    public void setPoetrySorter(PoetrySorter poetrySorter) {
        this.poetrySorter = poetrySorter;
    }

    public void setPersistenceManager(PoetryPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public void setSource(PoetryTweetSource source) {
        this.source = source;
    }

    public void setup() {
        persistenceManager.setup();
    }

    public void cleanup(String byName) {
        persistenceManager.cleanup(byName);
    }

}
