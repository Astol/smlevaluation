package evalution;

import net.dean.jraw.http.oauth.OAuthException;

import static java.lang.Thread.sleep;

public class SMLEvaluation {

    public static void main(String[] args) throws OAuthException, InterruptedException {
        RedditMiner miner = new RedditMiner();
        SocialMediaPublisher publisher = new SocialMediaPublisher();

        for(int i = 0; i < Integer.MAX_VALUE; i++) {
            String message = miner.getRandomHotSubmission();
            System.out.println("Printing new message: " + message);
            publisher.publish(message);
            sleep(1200000L);
        }
    }
}
