package com.fraise.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fraise.domain.Cinquain;
import com.fraise.domain.CinquainLine;
import com.fraise.domain.CinquainLineBookmarks;
import com.fraise.domain.Language;
import com.fraise.domain.Paging;
import com.fraise.domain.PoetryTweet;
import com.fraise.util.BaseTest;

@SuppressWarnings("deprecation")
public class CinquainTests extends BaseTest {

    private DataStorePoetryPersistenceManager testObject;

    public void setUp() throws Exception {
        super.setUp();
        testObject = new DataStorePoetryPersistenceManager();
        testObject.setPersistenceManager(newPersistenceManager());
        testObject.persistPoetryTweets(getMockPoetryTweets());
        testObject.persistCinquainLineBookmarks(new CinquainLineBookmarks("twitterCinquain_" + Language.en.toString(),
                new Date(2010, 0, 1, 9, 0)));

    }

    public void testCollateThenRetrieveCinquain() {

        Language language = Language.en;

        Date bookmark = new Date(2010, 0, 1, 9, 0);

        Paging paging = new Paging(5, bookmark);

        testObject.collateAndPersistCinquain(language);
        testObject.collateAndPersistCinquain(language);

        List<Cinquain> cinquains = testObject.getCinquain(1, paging, language);

        assertTrue(1 == cinquains.size());

        Cinquain cinquain = cinquains.get(0);

        CinquainLine one = cinquain.getLines().get(0);
        CinquainLine two = cinquain.getLines().get(1);
        CinquainLine three = cinquain.getLines().get(2);
        CinquainLine four = cinquain.getLines().get(3);
        CinquainLine five = cinquain.getLines().get(4);

        assertEquals("dark", one.getText());
        assertEquals("shiver cold", two.getText());
        assertEquals("some call clouds", three.getText());
        assertEquals("you in my brain", four.getText());
        assertEquals("empty", five.getText());
    }

    public void testCollateThenRetrieveSecondCinquain() {

        Language language = Language.en;

        Date bookmark = new Date(2010, 0, 1, 9, 0);

        Paging paging = new Paging(5, bookmark);

        testObject.collateAndPersistCinquain(Language.en);
        testObject.collateAndPersistCinquain(Language.en);

        List<Cinquain> cinquains = testObject.getCinquain(2, paging, language);

        assertTrue(2 == cinquains.size());

        Cinquain cinquain = cinquains.get(1);

        CinquainLine one = cinquain.getLines().get(0);
        CinquainLine two = cinquain.getLines().get(1);
        CinquainLine three = cinquain.getLines().get(2);
        CinquainLine four = cinquain.getLines().get(3);
        CinquainLine five = cinquain.getLines().get(4);

        assertEquals("disguise", one.getText());
        assertEquals("face aid", two.getText());
        assertEquals("hidden glasses woe", three.getText());
        assertEquals("why do you deceive", four.getText());
        assertEquals("finished", five.getText());

    }

    public void testTime() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        assertTrue(cal.get(Calendar.YEAR) == 1970);
    }

    private List<PoetryTweet> getMockPoetryTweets() {
        List<PoetryTweet> allTweets = new ArrayList<PoetryTweet>();
        allTweets.add(new PoetryTweet(1, "dark", "cheryl", new Date(2010, 0, 1, 10, 0)));
        allTweets.add(new PoetryTweet(2, "shiver cold", "david", new Date(2010, 0, 2, 10, 0)));
        allTweets.add(new PoetryTweet(3, "some call clouds", "cheryl", new Date(2010, 0, 3, 10, 0)));
        allTweets.add(new PoetryTweet(4, "you in my brain", "david", new Date(2010, 0, 4, 10, 0)));
        allTweets.add(new PoetryTweet(5, "empty", "david", new Date(2010, 0, 5, 10, 0)));
        allTweets.add(new PoetryTweet(6, "face aid", "cheryl", new Date(2010, 0, 6, 10, 0)));
        allTweets.add(new PoetryTweet(7, "disguise", "cheryl", new Date(2010, 0, 6, 10, 0)));
        allTweets.add(new PoetryTweet(8, "hidden glasses woe", "cheryl", new Date(2010, 0, 6, 10, 0)));
        allTweets.add(new PoetryTweet(9, "why do you deceive", "david", new Date(2010, 0, 6, 10, 0)));
        allTweets.add(new PoetryTweet(10, "finished", "david", new Date(2010, 0, 7, 10, 0)));
        return allTweets;
    }

}
