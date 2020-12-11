package twitterBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author arthu
 */
class GenericBotTask extends TimerTask {

    protected Twitter theTwitter;

    private int currentTweetIndex;

    public GenericBotTask(Twitter t) {
        theTwitter = t;
        loadTweetIndex();
    }

    @Override
    public void run() {

        Calendar cal = Calendar.getInstance(new Locale("FR", "FRANCE"));
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int monthNum = cal.get(Calendar.MONTH);
        String month = getMonth(monthNum);
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minuteOfHour = cal.get(Calendar.MINUTE);

        String newMessageToTweet = getMonth(monthNum) + " the " + getNumber(dayOfMonth)
                + " be with you.";

        StatusUpdate update = new StatusUpdate(newMessageToTweet);
        addImageIfNeeded(update, dayOfMonth, monthNum);

        try {
            // Send the tweet.
            theTwitter.updateStatus(update);
            System.out.println("Tweeted: \"" + newMessageToTweet + "\", "
                    + dayOfMonth + "-" + month + ", "
                    + (hourOfDay < 10 ? " " : "") + hourOfDay
                    + ":"
                    + (minuteOfHour < 10 ? " " : "") + minuteOfHour
                    + " (tweet n°" + currentTweetIndex + ")");
            saveTweetIndex();

            File bannerFile = new File("Resources/Banner.jpg");
            theTwitter.updateProfileBanner(bannerFile);

        } catch (TwitterException ex) {
            System.out.println("Could not tweet: " + newMessageToTweet);
            Logger.getLogger(GenericBotTask.class.getName()).log(Level.SEVERE, null, ex);
        }

        currentTweetIndex++;
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
        switch (day) {
            case 1:
            case 21:
            case 31:
                result = day + "st";
                break;
            case 2:
            case 22:
                result = day + "nd";
                break;
            case 3:
            case 23:
                result = day + "rd";
                break;
            default:
                result = day + "th";
                break;
        };

        return result;
    }

    private static String getMonth(int monthNum) {
        String tab[] = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        return tab[monthNum];
    }

    /**
     * Find the image for the day.
     *
     * @param update the status we add the image to.
     * @param dayOfMonth number of the day (1 to 31)
     * @param monthNum number of the month (0 to 11)
     */
    private void addImageIfNeeded(StatusUpdate update, int dayOfMonth, int monthNum) {

        String filename = "Resources/Images/image_" + dayOfMonth + "-" + (monthNum + 1) + ".png";
        File imageFile = new File(filename);
        if (imageFile.exists()) {
            update.setMedia(imageFile);
            System.out.println("Loading image " + filename);
        } else {
            System.out.println("Image " + filename + " not found.");
        }
    }

    /**
     * Read the file "lastTweetIndex.txt"
     *
     */
    private void saveTweetIndex() {
        try {
            File lastTweetFile = new File("Resources/lastTweetIndex.txt");
            if (lastTweetFile.exists()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(lastTweetFile));
                writer.write(currentTweetIndex + "\n");
                writer.close();
            } else {
                currentTweetIndex = 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(GenericBotTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTweetIndex() {
        System.out.println("Loading index");
        try {
            File lastTweetFile = new File("Resources/lastTweetIndex.txt");

            BufferedReader reader = new BufferedReader(new FileReader(lastTweetFile));

            String text = reader.readLine();
            currentTweetIndex = Integer.valueOf(text);
            System.out.println("Setting current tweet index to " + currentTweetIndex);

        } catch (FileNotFoundException e) {
            System.out.println("Index file not found.");
        } catch (IOException e) {
            System.out.println("Error when reading file.");
        }
    }
}
