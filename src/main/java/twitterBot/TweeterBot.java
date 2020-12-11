package twitterBot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author arthu
 */
public class TweeterBot {

    public static void main(String[] args) throws ParseException {

        System.out.println("Main launched.");

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

        GenericBotTask task = new GenericBotTask(twitter);

        Timer timer = new Timer();

        String firstPublishDate = propReader.getPropertyValue("firstPublishDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date firstGo = format.parse(firstPublishDate);
        Date currentDate = new Date();

        long delayInMillisec = firstGo.getTime() - currentDate.getTime();
        String periodInSecString = propReader.getPropertyValue("publishPeriod");
        long timerPeriod = Integer.valueOf(periodInSecString.split(" ")[0]);

        System.out.println("First publication scheduled at " + firstGo);
        System.out.println("Current date is " + currentDate);
        System.out.println("waiting " + delayInMillisec / 1000 + " seconds.");

        timer.schedule(task, delayInMillisec, timerPeriod);
        System.out.println("Timer scheduled.");
    }
}
