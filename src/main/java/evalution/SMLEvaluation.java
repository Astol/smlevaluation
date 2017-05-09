package evalution;

import net.dean.jraw.http.oauth.OAuthException;

public class SMLEvaluation {

    public static void main(String[] args) throws OAuthException {
        RedditMiner miner = new RedditMiner();
        System.out.println(miner.getClient().me());
    }
}
