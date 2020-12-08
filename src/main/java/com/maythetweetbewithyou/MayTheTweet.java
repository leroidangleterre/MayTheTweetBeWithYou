package com.maythetweetbewithyou;

import java.util.Calendar;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author arthu
 */
public class MayTheTweet {

    public static void main(String[] args) {

        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();

            ConnectionPropertiesReader propReader = new ConnectionPropertiesReader();

            cb.setDebugEnabled(true);

            final String consumerKey = propReader.getPropertyValue("oauth.consumerKey");
            final String consumerSecret = propReader.getPropertyValue("oauth.consumerSecret");
            final String accessToken = propReader.getPropertyValue("oauth.accessToken");
            final String accessTokenSecret = propReader.getPropertyValue("oauth.accessTokenSecret");

            cb.setOAuthConsumerKey(consumerKey);
            cb.setOAuthConsumerSecret(consumerSecret);
            cb.setOAuthAccessToken(accessToken);
            cb.setOAuthAccessTokenSecret(accessTokenSecret);

            Twitter twitter = new TwitterFactory(cb.build()).getInstance();

            Calendar cal = Calendar.getInstance();
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int monthNum = cal.get(Calendar.MONTH);
            String month = getMonth(monthNum);

            String newMessageToTweet = getMonth(monthNum) + " the " + getNumber(dayOfMonth) + " be with you.";

            System.out.println("Tweeting: " + newMessageToTweet);

            // Send the tweet.
            twitter.updateStatus(newMessageToTweet);
        } catch (TwitterException te) {
            te.printStackTrace();
        }
    }

    /**
     * Return the String representing the day number, with "th" (or "st"...)
     * added at the end.
     *
     * @param day
     * @return the day and suffix (st, nd, rd, th)
     */
    private static String getNumber(int day) {

        String result;
        result = switch (day) {
            case 1,21,31 ->
                day + "st";
            case 2,22 ->
                day + "nd";
            case 3,23 ->
                day + "rd";
            default ->
                day + "th";
        };

        return result;
    }

    private static String getMonth(int monthNum) {
        String tab[] = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        return tab[monthNum];
    }
}
