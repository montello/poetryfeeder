package com.fraise.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.hamcrest.internal.ArrayIterator;

import com.fraise.domain.Cinquain;
import com.fraise.domain.CinquainLine;
import com.fraise.domain.CinquainLineBookmarks;
import com.fraise.domain.Language;
import com.fraise.domain.LineSource;
import com.fraise.domain.MessageType;
import com.fraise.domain.Paging;
import com.fraise.domain.Poem;
import com.fraise.domain.PoetryTweet;
import com.google.appengine.api.datastore.Key;

public class DataStorePoetryPersistenceManager implements PoetryPersistenceManager {

    private static final Logger log = Logger.getLogger(DataStorePoetryPersistenceManager.class.getName());

    private PersistenceManager persistenceManager;

    public DataStorePoetryPersistenceManager() {
        PersistenceManagerFactory pmf = PMF.get();
        setPersistenceManager(pmf.getPersistenceManager());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fraise.persistence.PoetryPersistenceManager#persistPoetryTweets(java
     * .util.List)
     */
    public void persistPoetryTweets(List<PoetryTweet> poetryTweets) {
        Transaction tx = persistenceManager.currentTransaction();
        for (PoetryTweet tweet : poetryTweets) {
            try {
                tx.begin();
                persistenceManager.makePersistent(tweet);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fraise.persistence.PoetryPersistenceManager#getAllTweets()
     */
    @SuppressWarnings("unchecked")
    public List<PoetryTweet> getAllTweets() {
        String query = "select from " + PoetryTweet.class.getName();
        List<PoetryTweet> tweets = (List<PoetryTweet>) persistenceManager.newQuery(query).execute();
        return tweets;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fraise.persistence.PoetryPersistenceManager#getLineSource(java.lang
     * .String)
     */
    public LineSource getLineSource(String thisAccount) {
        LineSource lineSource = null;
        try {
            lineSource = persistenceManager.getObjectById(LineSource.class, thisAccount);
        } catch (Exception e) {
            //
        }
        return lineSource;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fraise.persistence.PoetryPersistenceManager#registerAccount(com.fraise
     * .domain.LineSource)
     */
    public void registerLineSource(LineSource lineSource) {
        Transaction tx = persistenceManager.currentTransaction();
        try {
            tx.begin();
            persistenceManager.makePersistent(lineSource);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }

    }

    public void updateLastModifiedForMessageType(MessageType messageType, long lastModified) {
        Transaction tx = persistenceManager.currentTransaction();
        try {
            tx.begin();
            MessageType updated = persistenceManager.getObjectById(MessageType.class, messageType.getKey());
            updated.setLastModified(lastModified);
            persistenceManager.makePersistent(updated);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<PoetryTweet> getTweetsByAuthor(String author) {
        Query query = persistenceManager.newQuery(PoetryTweet.class, "author==authorParam");
        query.declareParameters("String authorParam");
        return (List<PoetryTweet>) query.execute(author);
    }

    @SuppressWarnings("unchecked")
    public List<PoetryTweet> getTweetsByAuthor(String author, Paging paging) {
        Query query = persistenceManager.newQuery(PoetryTweet.class, "author==authorParam");
        query.declareParameters("String authorParam");

        return (List<PoetryTweet>) query.execute(author);
    }

    @SuppressWarnings("unchecked")
    public List<PoetryTweet> getLinesByFirstLetter(String letter) {
        Query query = persistenceManager.newQuery(PoetryTweet.class, "firstLetter==firstLetterParam");
        query.declareParameters("String firstLetterParam");
        return (List<PoetryTweet>) query.execute(letter);

    }

    @SuppressWarnings("unchecked")
    public List<PoetryTweet> getLinesInDateOrder() {
        Query query = persistenceManager.newQuery(PoetryTweet.class);
        query.setOrdering("authoredOn DESC");
        return (List<PoetryTweet>) query.execute();
    }

    @SuppressWarnings("unchecked")
    public List<PoetryTweet> getMostRecent(Paging paging, Language language) {
        Query query = persistenceManager.newQuery(PoetryTweet.class, "language==languageParam");
        query.declareParameters("String languageParam");

        query.setOrdering("authoredOn DESC");
        query.setRange(0, paging.getPoemSize());

        List<PoetryTweet> result = (List<PoetryTweet>) query.execute(language.toString().toLowerCase());

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Poem> getArchivedPoems(int numberOfPoems, Paging paging, Language language) {
        log.info("numberOfPoems: " + numberOfPoems);
        List<Poem> result = new ArrayList<Poem>();
        Query query = persistenceManager.newQuery(PoetryTweet.class, "authoredOn>bookmark && language==languageParam");
        query.declareParameters("java.util.Date bookmark, String languageParam");
        query.setOrdering("authoredOn ASC");
        query.setRange(0, paging.getPoemSize());
        Date bookmark = paging.getBookmark();
        for (int i = 0; i < numberOfPoems; i++) {
            List<PoetryTweet> poemLines = (List<PoetryTweet>) query.execute(bookmark, language);
            if (poemLines.size() == paging.getPoemSize()) {
                Poem poem = new Poem(poemLines);
                result.add(poem);
                bookmark = poem.getBookmark();
                log.info("bookmark: " + bookmark);
            }
        }
        log.info("poems returned: " + result.size());
        return result;
    }

    public void collateAndPersistCinquain(Language language) {

        PoetryTweet lineOne = null;
        PoetryTweet lineTwo = null;
        PoetryTweet lineThree = null;
        PoetryTweet lineFour = null;
        PoetryTweet lineFive = null;

        Transaction tx = persistenceManager.currentTransaction();
        log.info("tx active: " + tx.isActive());
        try {
            tx.begin();
            CinquainLineBookmarks cinquainLineBookmarks = persistenceManager.getObjectById(CinquainLineBookmarks.class,
                    "twitterCinquain_" + language.toString());
            lineOne = getTweetBySize(Cinquain.LINE_ONE, new Paging(1, cinquainLineBookmarks.getBookmarkOne()), language);
            if (lineOne == null) {
                log.info("lineOne is null, bookmark:" + cinquainLineBookmarks.getBookmarkOne());
            }
            lineTwo = getTweetBySize(Cinquain.LINE_TWO, new Paging(1, cinquainLineBookmarks.getBookmarkTwo()), language);
            if (lineTwo == null) {
                log.info("lineTwo is null, bookmark:" + cinquainLineBookmarks.getBookmarkTwo());
            }
            lineThree = getTweetBySize(Cinquain.LINE_THREE, new Paging(1, cinquainLineBookmarks.getBookmarkThree()),
                    language);
            if (lineThree == null) {
                log.info("lineThree is null, bookmark:" + cinquainLineBookmarks.getBookmarkThree());
            }
            lineFour = getTweetBySize(Cinquain.LINE_FOUR, new Paging(1, cinquainLineBookmarks.getBookmarkFour()),
                    language);
            if (lineFour == null) {
                log.info("lineFour is null, bookmark:" + cinquainLineBookmarks.getBookmarkFour());
            }
            if (lineOne != null) {
                lineFive = getTweetBySize(Cinquain.LINE_FIVE, new Paging(1, lineOne.getAuthoredOn()), language);
                if (lineFive == null) {
                    log.info("lineFive is null, bookmark:" + lineOne.getAuthoredOn());
                }
            }
            if (null == lineOne || null == lineTwo || null == lineThree || null == lineFour || null == lineFive) {
                log.info("One of the Cinquain lines is *null*");
                if (tx.isActive()) {
                    tx.rollback();
                }
                return;
            }

            log.info("6");
            cinquainLineBookmarks.setBookmarkOne(lineFive.getAuthoredOn());
            log.info("7");
            cinquainLineBookmarks.setBookmarkTwo(lineTwo.getAuthoredOn());
            log.info("8");
            cinquainLineBookmarks.setBookmarkThree(lineThree.getAuthoredOn());
            log.info("9");
            cinquainLineBookmarks.setBookmarkFour(lineFour.getAuthoredOn());
            log.info("10");
            cinquainLineBookmarks.setBookmarkFive(lineFive.getAuthoredOn());

            persistenceManager.makePersistent(cinquainLineBookmarks);

            tx.commit();
        } catch (Exception e) {
            log.info("Exception collating/persisting Cinquain: " + e.getCause());
            log.info("message: " + e.getMessage());
            log.info("loc message: " + e.getLocalizedMessage());
            log.info("toString(): " + e.toString());
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            return;
        }

        tx = persistenceManager.currentTransaction();
        try {
            Cinquain cinquain = new Cinquain(language);
            cinquain.addLine(new CinquainLine(lineOne));
            cinquain.addLine(new CinquainLine(lineTwo));
            cinquain.addLine(new CinquainLine(lineThree));
            cinquain.addLine(new CinquainLine(lineFour));
            cinquain.addLine(new CinquainLine(lineFive));
            tx.begin();

            persistenceManager.makePersistent(cinquain);

            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Cinquain> getCinquain(Paging paging, Language language) {
        Query query = persistenceManager.newQuery(Cinquain.class, "language==languageParam");
        query.declareParameters("String languageParam");
        return (List<Cinquain>) query.execute(language.toString());
    }

    @SuppressWarnings("unchecked")
    public List<Cinquain> getCinquain(int count, Paging paging, Language language) {
        Query query = persistenceManager.newQuery(Cinquain.class, "language==languageParam");
        query.declareParameters("String languageParam");
        query.setRange(0, count);
        return (List<Cinquain>) query.execute(language.toString());
    }

    @SuppressWarnings("unchecked")
    private PoetryTweet getTweetBySize(int size, Paging paging, Language language) {
        Query query = persistenceManager.newQuery(PoetryTweet.class,
                "authoredOn>bookmark && language==languageParam && textLength==textLengthParam");
        query.declareParameters("java.util.Date bookmark, String languageParam, int textLengthParam");
        query.setOrdering("authoredOn ASC");
        query.setRange(0, paging.getPoemSize());

        List<PoetryTweet> poetryTweetList = (List<PoetryTweet>) query.execute(paging.getBookmark(), language, size);
        PoetryTweet result = null;
        if (poetryTweetList.size() > 0) {
            result = poetryTweetList.get(0);
        }
        return result;
    }

    public CinquainLineBookmarks persistCinquainLineBookmarks(CinquainLineBookmarks cinquainLineBookmarks) {
        CinquainLineBookmarks persisted;
        Transaction tx = persistenceManager.currentTransaction();
        try {
            tx.begin();
            persisted = persistenceManager.makePersistent(cinquainLineBookmarks);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        return persisted;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fraise.persistence.PoetryPersistenceManager#setPersistenceManager
     * (javax.jdo.PersistenceManager)
     */
    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    @Override
    public void setup() {
        Transaction tx = persistenceManager.currentTransaction();
        try {
            log.info("Setup");
            tx.begin();
            persistenceManager.makePersistent(new CinquainLineBookmarks("twitterCinquain_" + Language.en.toString(),
                    new Date(2009, 0, 1, 9, 0)));
            tx.commit();
        } catch (Exception e) {
            log.info("Exception persisting CinquainLineBookmarks :" + e.getMessage());
            log.info("localalizedMessage: " + e.getLocalizedMessage());
            log.info("cause: " + e.getCause());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
                log.info("rolled back");
            }
        }

        tx = persistenceManager.currentTransaction();
        try {
            log.info("Setup");
            tx.begin();
            persistenceManager.makePersistent(new CinquainLineBookmarks("twitterCinquain_" + Language.nl.toString(),
                    new Date(2009, 0, 1, 9, 0)));
            tx.commit();
        } catch (Exception e) {
            log.info("Exception persisting CinquainLineBookmarks :" + e.getMessage());
            log.info("localalizedMessage: " + e.getLocalizedMessage());
            log.info("cause: " + e.getCause());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
                log.info("rolled back");
            }
        }
    }

    @Override
    public void cleanup(String byName) {
        // Transaction currentTransaction =
        // persistenceManager.currentTransaction();
        try {
            // currentTransaction.begin();

            List<PoetryTweet> allTweets = getAllTweets(byName);
            Iterator<PoetryTweet> outside = allTweets.iterator();
            PoetryTweet current;
            PoetryTweet next;
            while (outside.hasNext()) {
                Iterator<PoetryTweet> inside = allTweets.iterator();
                List<PoetryTweet> matches = new ArrayList<PoetryTweet>();
                current = outside.next();
                while (inside.hasNext()) {
                    next = inside.next();
                    if (next.getSourceId() == current.getSourceId()) {
                        matches.add(next);
                    }
                }
                if (matches.size() > 1) {
                    log.info("DPK: removing count:" + matches.size());
                    Iterator<PoetryTweet> removeIterator = matches.iterator();
                    removeIterator.next();
                    while (removeIterator.hasNext()) {
                        PoetryTweet toRemove = removeIterator.next();
                        Query deleteQuery = persistenceManager.newQuery(PoetryTweet.class);
                        deleteQuery.setFilter("key == theKey");
                        deleteQuery.declareParameters(Key.class.getName() + " theKey");
                        deleteQuery.deletePersistentAll(toRemove.getKey());
                        allTweets.remove(toRemove);
                    }
                }

            }
        } catch (Exception e) {
            // currentTransaction.rollback();
            log.info("Error removing duplicates:" + e.getMessage());
        } finally {
            // currentTransaction.commit();
        }

    }

    private List<PoetryTweet> getAllTweets(String byName) {
        Query query = persistenceManager.newQuery(PoetryTweet.class);
        query.setFilter("author == nameParam");
        query.declareParameters("String nameParam");
        List<PoetryTweet> tweets = (List<PoetryTweet>) query.execute(byName);
        log.info("tweets found for user: " + byName + ", count: " + tweets.size());

        return tweets;
    }

}
