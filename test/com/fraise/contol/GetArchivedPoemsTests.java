package com.fraise.contol;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.fraise.control.PoetryFeederManager;
import com.fraise.control.PoetrySorterImpl;
import com.fraise.domain.Language;
import com.fraise.domain.Paging;
import com.fraise.domain.Poem;
import com.fraise.domain.PoetryTweet;
import com.fraise.domain.SortOrder;
import com.fraise.persistence.PoetryPersistenceManager;
import com.fraise.source.PoetryTweetSource;

@SuppressWarnings("deprecation")
public class GetArchivedPoemsTests extends TestCase {

    private PoetryFeederManager testObject;
    private PoetryPersistenceManager persistenceManager;
    private PoetryTweetSource source;

    public void setUp() {
        testObject = new PoetryFeederManager();
        persistenceManager = mock(PoetryPersistenceManager.class);
        source = mock(PoetryTweetSource.class);
        testObject.setPersistenceManager(persistenceManager);
        testObject.setSource(source);
        testObject.setPoetrySorter(new PoetrySorterImpl());
    }

    public void testGetSinglePageFromArchive() {
        int poemSize = 3;
        int numberOfPoems = 1;

        Date bookmark = new Date(2009, 0, 1, 10, 0);
        Paging paging = new Paging(poemSize, bookmark);
        when(persistenceManager.getArchivedPoems(numberOfPoems, paging, Language.en)).thenReturn(oneArchivedPoem());

        List<Poem> poems = testObject.getArchivedPoems(numberOfPoems, paging, SortOrder.ALPHABETICAL, Language.en);

        assertTrue(1 == poems.size());

        assertEquals("dark was the night", poems.get(0).getLines().get(0).getText());
        assertEquals("riding on my big black rocket", poems.get(0).getLines().get(1).getText());
        assertEquals("take my mind away from this", poems.get(0).getLines().get(2).getText());
    }

    public void testGetTwoPoemsFromArchive() {
        int poemSize = 3;
        int numberOfPoems = 2;

        Date bookmark = new Date(2009, 0, 1, 10, 0);
        Paging paging = new Paging(poemSize, bookmark);
        when(persistenceManager.getArchivedPoems(numberOfPoems, paging, Language.en)).thenReturn(twoArchivedPoems());

        List<Poem> poems = testObject.getArchivedPoems(numberOfPoems, paging, SortOrder.ALPHABETICAL, Language.en);

        assertTrue(2 == poems.size());

        assertEquals("dark was the night", poems.get(0).getLines().get(0).getText());
        assertEquals("riding on my big black rocket", poems.get(0).getLines().get(1).getText());
        assertEquals("take my mind away from this", poems.get(0).getLines().get(2).getText());

        assertEquals("blueprint despise and demise", poems.get(1).getLines().get(0).getText());
        assertEquals("face aid through the cracks", poems.get(1).getLines().get(1).getText());
        assertEquals("flush out all false notions", poems.get(1).getLines().get(2).getText());

    }

    private List<Poem> oneArchivedPoem() {
        List<Poem> poems = new ArrayList<Poem>();
        poems.add(poem1());
        return poems;
    }

    private List<Poem> twoArchivedPoems() {
        List<Poem> poems = new ArrayList<Poem>();
        poems.add(poem1());
        poems.add(poem2());
        return poems;
    }

    private Poem poem1() {
        List<PoetryTweet> poemTweets1 = new ArrayList<PoetryTweet>();
        poemTweets1.add(new PoetryTweet(1, "Take my mind away from this", "cheryl", new Date(2009, 0, 3, 10, 0)));
        poemTweets1.add(new PoetryTweet(2, "dark was the night", "cheryl", new Date(2010, 0, 1, 10, 0)));
        poemTweets1.add(new PoetryTweet(3, "riding on my big black rocket", "david", new Date(2010, 0, 2, 10, 0)));
        Poem poem1 = new Poem(poemTweets1);
        return poem1;
    }

    private Poem poem2() {
        List<PoetryTweet> poemTweets2 = new ArrayList<PoetryTweet>();
        poemTweets2.add(new PoetryTweet(4, "blueprint despise and demise", "john", new Date(2010, 0, 4, 10, 0)));
        poemTweets2.add(new PoetryTweet(5, "flush out all false notions", "david", new Date(2010, 0, 5, 10, 0)));
        poemTweets2.add(new PoetryTweet(6, "face aid through the cracks", "leo", new Date(2010, 0, 6, 10, 0)));
        Poem poem2 = new Poem(poemTweets2);
        return poem2;
    }
}
