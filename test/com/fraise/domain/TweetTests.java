package com.fraise.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

public class TweetTests {

    @Test
    public void createTweetWithParameterisedConstructor() {
        String text = "@poetryfeeder Riding on my    		big black   rocket   ";
        PoetryTweet tweet = new PoetryTweet(1, text, "david", new Date());
        assertNotNull(tweet);
        assertEquals("riding on my big black rocket", tweet.getText());
        assertTrue("Word count incorrect:" + tweet.getTextLength(), 6 == tweet.getTextLength());
        assertEquals("david", tweet.getAuthor());
        assertEquals(new Character('r'), tweet.getFirstLetter());
    }

    @Test
    public void testAssertionsOnInvalidCreate() {
        try {
            @SuppressWarnings("unused")
            PoetryTweet tweet = new PoetryTweet(1, null, "david", null);
            fail("should have thrown a RTE");
        } catch (Exception e) {
            // good
        }
    }

}
