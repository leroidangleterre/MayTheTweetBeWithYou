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

//        String newMessageToTweet = "Encore un test de tweet automatique, on est le "
//                + getNumber(dayOfMonth) + " du mois de " + month;
            String newMessageToTweet = getMonth(monthNum) + " the " + getNumber(dayOfMonth) + " be with you.";

            System.out.println("Message: " + newMessageToTweet);

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
     * @return
     */
    private static String getNumber(int day) {
        return day + "th";
    }

    private static String getMonth(int monthNum) {
        String tab[] = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        return tab[monthNum];
    }
}
