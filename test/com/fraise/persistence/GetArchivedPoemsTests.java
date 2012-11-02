package com.fraise.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fraise.domain.Language;
import com.fraise.domain.Paging;
import com.fraise.domain.Poem;
import com.fraise.domain.PoetryTweet;
import com.fraise.util.BaseTest;

@SuppressWarnings("deprecation")
public class GetArchivedPoemsTests extends BaseTest {

    private DataStorePoetryPersistenceManager testObject;

    public void setUp() throws Exception {
        super.setUp();
        testObject = new DataStorePoetryPersistenceManager();
        testObject.setPersistenceManager(newPersistenceManager());
        testObject.persistPoetryTweets(getMockPoetryTweets());
    }

    public void testGetFirstPoemSize3() {
        int poemSize = 3;
        int numberOfPoems = 1;

        List<Poem> poems = testObject.getArchivedPoems(numberOfPoems,
                new Paging(poemSize, new Date(2009, 0, 1, 10, 0)), Language.en);

        assertTrue(1 == poems.size());
        assertTrue(3 == poems.get(0).getLines().size());

        assertEquals("dark was the night", poems.get(0).getLines().get(0).getText());
    }

    public void testGetSecondPoemSize3() {
        int poemSize = 3;
        int numberOfPoems = 1;

        List<Poem> poems = testObject.getArchivedPoems(numberOfPoems,
                new Paging(poemSize, new Date(2010, 0, 3, 10, 0)), Language.en);

        assertTrue(1 == poems.size());
        assertTrue(3 == poems.get(0).getLines().size());

        assertEquals("blueprint despise and demise", poems.get(0).getLines().get(0).getText());

    }

    public void testGetTwoPoemsSize2() {
        int poemSize = 2;
        int numberOfPoems = 2;

        List<Poem> poems = testObject.getArchivedPoems(numberOfPoems,
                new Paging(poemSize, new Date(2009, 0, 3, 10, 0)), Language.en);

        assertTrue(2 == poems.size());
        assertTrue(2 == poems.get(0).getLines().size());

        assertEquals("dark was the night", poems.get(0).getLines().get(0).getText());
        assertEquals("riding on my big black rocket", poems.get(0).getLines().get(1).getText());

        assertEquals("take my mind away from this", poems.get(1).getLines().get(0).getText());
        assertEquals("blueprint despise and demise", poems.get(1).getLines().get(1).getText());
    }

    public void testGetTwoPoemsSize2OffsetOnePoem() {
        int poemSize = 2;
        int numberOfPoems = 2;

        List<Poem> poems = testObject.getArchivedPoems(numberOfPoems,
                new Paging(poemSize, new Date(2010, 0, 2, 10, 0)), Language.en);

        assertTrue("poems count returned incorrect: " + poems.size(), 2 == poems.size());
        assertTrue("poem size incorrect: " + poems.get(0).getLines().size(), 2 == poems.get(0).getLines().size());

        assertEquals("take my mind away from this", poems.get(0).getLines().get(0).getText());
        assertEquals("blueprint despise and demise", poems.get(0).getLines().get(1).getText());

        assertEquals("flush out all false notions", poems.get(1).getLines().get(0).getText());
        assertEquals("face aid through the cracks", poems.get(1).getLines().get(1).getText());
    }

    public void testGetPoemSize4DiscardPartialPoems() {
        int poemSize = 4;
        int numberOfPoems = 2;

        List<Poem> poems = testObject.getArchivedPoems(numberOfPoems,
                new Paging(poemSize, new Date(2009, 0, 2, 10, 0)), Language.en);

        assertTrue("poems count returned incorrect: " + poems.size(), 1 == poems.size());
        assertTrue("poem size incorrect: " + poems.get(0).getLines().size(), 4 == poems.get(0).getLines().size());

        assertEquals("dark was the night", poems.get(0).getLines().get(0).getText());
        assertEquals("riding on my big black rocket", poems.get(0).getLines().get(1).getText());
        assertEquals("take my mind away from this", poems.get(0).getLines().get(2).getText());
        assertEquals("blueprint despise and demise", poems.get(0).getLines().get(3).getText());

    }

    private List<PoetryTweet> getMockPoetryTweets() {
        List<PoetryTweet> allTweets = new ArrayList<PoetryTweet>();
        allTweets.add(new PoetryTweet(1, "dark was the night", "cheryl", new Date(2010, 0, 1, 10, 0)));
        allTweets.add(new PoetryTweet(2, "riding on my big black rocket", "david", new Date(2010, 0, 2, 10, 0)));
        allTweets.add(new PoetryTweet(3, "Take my mind away from this", "cheryl", new Date(2010, 0, 3, 10, 0)));
        allTweets.add(new PoetryTweet(1, "blueprint despise and demise", "david", new Date(2010, 0, 4, 10, 0)));
        allTweets.add(new PoetryTweet(2, "flush out all false notions", "david", new Date(2010, 0, 5, 10, 0)));
        allTweets.add(new PoetryTweet(3, "face aid through the cracks", "cheryl", new Date(2010, 0, 6, 10, 0)));
        return allTweets;
    }
}
