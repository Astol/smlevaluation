package evalution;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.SubredditPaginator;
import org.cfg4j.provider.ConfigurationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RedditMiner {
    public RedditClient getClient() {
        return client;
    }

    private RedditClient client;
    private Random randomGenerator;
    private List<String> previousPosts;
    private OAuthData oAuthData;

    public void refreshToken(){
        client.authenticate(oAuthData);
    }

    public RedditMiner() throws OAuthException {
        ConfigurationProvider config = Configuration.getInstance().getConfig();
        String username = config.getProperty("REDDIT.USERNAME", String.class);
        String password = config.getProperty("REDDIT.PASSWORD", String.class);
        String appId = config.getProperty("REDDIT.APPID", String.class);
        String appSecret = config.getProperty("REDDIT.APPSECRET", String.class);
        Credentials credentials = Credentials.script(username, password, appId, appSecret);

        UserAgent userAgent = UserAgent.of("desktop", appId, "v0.1", "SocialMediaLibrary");

        client = new RedditClient(userAgent);
        oAuthData = client.getOAuthHelper().easyAuth(credentials);
        this.refreshToken();

        randomGenerator = new Random();
        previousPosts = new ArrayList<>();
    }

    public String getRandomHotSubmission() {
        List<SubredditPaginator> pager = Arrays.asList(
                new SubredditPaginator(client,"funny"),
                new SubredditPaginator(client,"iamverysmart"),
                new SubredditPaginator(client,"jokes"),
                new SubredditPaginator(client,"programmerhumor"));

        int index = randomGenerator.nextInt(pager.size());
        Listing<Submission> currentPage = pager.get(index).next();


        List<Submission> submissionList = currentPage.stream()
                .filter(item -> !(item.isStickied() || previousPosts.contains(item.getId())))
                .limit(20L)
                .collect(Collectors.toList());
        index = randomGenerator.nextInt(submissionList.size());
        Submission submission = submissionList.get(index);
        this.previousPosts.add(submission.getId());

        List<String> postList = new ArrayList<>();
        postList.add(submission.getTitle());
        postList.add(submission.getSelftext());
        postList.add(submission.getUrl());

        if(!submission.getUrl().endsWith(submission.getPermalink())) {
            postList.add(submission.getPermalink());
        }

        List<String> resultList = postList.stream()
                .filter(item -> !isNullOrWhitespace(item))
                .collect(Collectors.toList());

        return String.join("\n\n", resultList);
    }

    private static boolean isNullOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }

}
