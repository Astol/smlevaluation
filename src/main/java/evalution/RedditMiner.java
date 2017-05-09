package evalution;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import org.cfg4j.provider.ConfigurationProvider;

public class RedditMiner {
    public RedditClient getClient() {
        return client;
    }

    private RedditClient client;

    public RedditMiner() throws OAuthException {
        ConfigurationProvider config = Configuration.getInstance().getConfig();
        String username = config.getProperty("REDDIT.USERNAME", String.class);
        String password = config.getProperty("REDDIT.PASSWORD", String.class);
        String appId = config.getProperty("REDDIT.APPID", String.class);
        String appSecret = config.getProperty("REDDIT.APPSECRET", String.class);
        Credentials credentials = Credentials.script(username, password, appId, appSecret);

        UserAgent userAgent = UserAgent.of("desktop", appId, "v0.1", "SocialMediaLibrary");

        client = new RedditClient(userAgent);
        OAuthData data = client.getOAuthHelper().easyAuth(credentials);
        client.authenticate(data);
    }


}
