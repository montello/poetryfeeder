package com.fraise.source;

import java.util.Iterator;
import java.util.List;

import twitter4j.TwitterException;
import twitter4j.User;

import com.fraise.persistence.DataStorePoetryPersistenceManager;
import com.fraise.source.twitter.AccountAccessToken;
import com.fraise.source.twitter.ApplicationToken;
import com.fraise.source.twitter.TwitterWrapper;
import com.fraise.util.BaseTest;

public class UserFollowingAutomationTests extends BaseTest {

    private DataStorePoetryPersistenceManager pfPersistenceManager;

    private TwitterWrapper twitterForPF;
    private String consumerKey = "o7Mni3cZVXbkDupXDqK6ag";
    private String consumerSecret = "uUrbyPxOMPmIknY2ydrIrSShlvXWBnXB3iSXxUUqVk";

    public void setUp() throws Exception {
        super.setUp();
        pfPersistenceManager = new DataStorePoetryPersistenceManager();
        pfPersistenceManager.setPersistenceManager(newPersistenceManager());

        setUpPoetryFeederTwitter();
    }

    public void setUpPoetryFeederTwitter() {
        String tokenPF = "51423404-C24silbhobslmrFK1sgnWeQPSU395yOfuiI8soanz";
        String tokenSecretPF = "Bi9foBXG3O5vG8BEY7JyOLOEsd0kbkSHoEfw5i28";
        twitterForPF = new TwitterWrapper(new ApplicationToken("poetryfeeder", consumerKey, consumerSecret),
                new AccountAccessToken("poetryfeeder", tokenPF, tokenSecretPF));
    }

    public void testFollowAllFollowers() throws TwitterException {
        List<User> followed = twitterForPF.followUserFollowers();
        assertNotNull(followed);

        Iterator<User> followedIter = followed.iterator();
        while (followedIter.hasNext()) {
            User user = followedIter.next();
            // assertEquals(user.get)
        }
    }

}
