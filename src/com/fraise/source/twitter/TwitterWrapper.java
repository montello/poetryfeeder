package com.fraise.source.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterResponse;
import twitter4j.User;
import twitter4j.http.AccessToken;

public class TwitterWrapper {

    public TwitterWrapper(ApplicationToken application, AccountAccessToken account) {
        AccessToken accessToken = new AccessToken(account.getToken(), account.getTokenSecret());
        twitterInterface = new TwitterFactory().getOAuthAuthorizedInstance(application.getConsumerKey(), application
                .getConsumerSecret(), accessToken);
    }

    private static final Logger log = Logger.getLogger(TwitterWrapper.class.getName());

    private Twitter twitterInterface;

    public void setTwitterInterface(Twitter twitterInterface) {
        this.twitterInterface = twitterInterface;
    }

    public ResponseList<? extends TwitterResponse> getDirectMessages(Paging paging) throws TwitterException {
        return twitterInterface.getDirectMessages(paging);
    }

    public ResponseList<? extends TwitterResponse> getUserTimeline(Paging paging) throws TwitterException {
        return twitterInterface.getUserTimeline(paging);
    }

    public ResponseList<? extends TwitterResponse> getMentions(Paging paging) throws TwitterException {
        return twitterInterface.getMentions(paging);
    }

    public List<User> followUserFollowers() {
        List<User> followedUsers = new ArrayList<User>();
        try {
            IDs ids = twitterInterface.getFollowersIDs();
            if (ids != null) {
                int[] idArray = ids.getIDs();
                for (int i = 0; i < idArray.length; i++) {
                    long follower = idArray[i];
                    boolean existsFriendship;
                    try {
                        existsFriendship = twitterInterface.existsFriendship("poetryfeeder", String.valueOf(follower));
                        if (!existsFriendship) {
                            User newFriend = twitterInterface.createFriendship(new Long(follower).intValue());
                            followedUsers.add(newFriend);
                            log.info("now following:" + newFriend.getScreenName());
                        }
                    } catch (Exception e) {
                        log.info("failed to follow ID:" + twitterInterface.showUser(Long.valueOf(follower).intValue()));
                    }
                }
            }
        } catch (TwitterException e) {
            log.info("Exception retrieving followers:" + e.getMessage());
        }
        return followedUsers;
    }

}
