package com.fraise.contol;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.fraise.control.PoetryFeederManager;
import com.fraise.control.PoetrySorterImpl;
import com.fraise.domain.Language;
import com.fraise.domain.Paging;
import com.fraise.domain.PoetryTweet;
import com.fraise.domain.SortOrder;
import com.fraise.persistence.PoetryPersistenceManager;
import com.fraise.source.PoetryTweetSource;

public class GetMostRecentPoemTests extends TestCase {

    private PoetryFeederManager testObject;
    private PoetryPersistenceManager persistenceManager;
    private PoetryTweetSource source;
    private int pageSize = 6;

    public void setUp() {
        testObject = new PoetryFeederManager();
        persistenceManager = mock(PoetryPersistenceManager.class);
        source = mock(PoetryTweetSource.class);
        testObject.setPersistenceManager(persistenceManager);
        testObject.setSource(source);
        testObject.setPoetrySorter(new PoetrySorterImpl());
    }

    public void testSortedByAuthor() {
        Paging paging = new Paging(pageSize);
        when(persistenceManager.getMostRecent(paging, Language.EN)).thenReturn(recentTweets());

        List<PoetryTweet> result = testObject.getRecentSortedBy(SortOrder.AUTHOR, paging, Language.EN);
        assertNotNull(result);
        assertTrue(6 == result.size());

        assertEquals("cheryl", result.get(0).getAuthor());
        assertEquals("cheryl", result.get(1).getAuthor());
        assertEquals("david", result.get(2).getAuthor());
        assertEquals("david", result.get(3).getAuthor());
        assertEquals("john", result.get(4).getAuthor());
        assertEquals("leo", result.get(5).getAuthor());
    }

    public void testSortedByAlphabet() {
        Paging paging = new Paging(pageSize);
        when(persistenceManager.getMostRecent(paging, Language.EN)).thenReturn(recentTweets());

        List<PoetryTweet> result = testObject.getRecentSortedBy(SortOrder.ALPHABETICAL, paging, Language.EN);
        assertNotNull(result);
        assertTrue(pageSize == result.size());

        assertEquals("blueprint despise and demise", result.get(0).getText());
        assertEquals("dark was the night", result.get(1).getText());
        assertEquals("face aid through the cracks", result.get(2).getText());
        assertEquals("flush out all false notions", result.get(3).getText());
        assertEquals("riding on my big black rocket", result.get(4).getText());
        assertEquals("take my mind away from this", result.get(5).getText());

    }

    public void testSortedByChronological() {
        Paging paging = new Paging(pageSize);
        when(persistenceManager.getMostRecent(paging, Language.EN)).thenReturn(recentTweets());

        List<PoetryTweet> result = testObject.getRecentSortedBy(SortOrder.CHRONOLOGICAL, paging, Language.EN);
        assertNotNull(result);
        assertTrue(6 == result.size());

        assertEquals("face aid through the cracks", result.get(0).getText());
        assertEquals("flush out all false notions", result.get(1).getText());
        assertEquals("blueprint despise and demise", result.get(2).getText());
        assertEquals("riding on my big black rocket", result.get(3).getText());
        assertEquals("dark was the night", result.get(4).getText());
        assertEquals("take my mind away from this", result.get(5).getText());

    }

    private List<PoetryTweet> recentTweets() {
        List<PoetryTweet> allTweets = new ArrayList<PoetryTweet>();
        allTweets.add(new PoetryTweet(1, "dark was the night", "cheryl", new Date(2010, 0, 1, 10, 0)));
        allTweets.add(new PoetryTweet(2, "riding on my big black rocket", "david", new Date(2010, 0, 2, 10, 0)));
        allTweets.add(new PoetryTweet(3, "Take my mind away from this", "cheryl", new Date(2009, 0, 3, 10, 0)));
        allTweets.add(new PoetryTweet(4, "blueprint despise and demise", "john", new Date(2010, 0, 4, 10, 0)));
        allTweets.add(new PoetryTweet(5, "flush out all false notions", "david", new Date(2010, 0, 5, 10, 0)));
        allTweets.add(new PoetryTweet(6, "face aid through the cracks", "leo", new Date(2010, 0, 6, 10, 0)));
        return allTweets;

    }

}
