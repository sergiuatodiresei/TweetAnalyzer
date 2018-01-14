package tweetdata;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConnection {
	
	private static final String ACCESS_TOKEN = "734029726235381760-pgwCEn2r08MIoMgKpMu3ZfTjJvhZqiG";
	private static final String ACCESS_TOKEN_SECRET = "I9jlC8GqOJNd12t1JIU0pXCWYiy0hcWjsQntM3u2okHYa";
	
	public static Twitter getTwitterInstance() {
		
		ConfigurationBuilder builder = new ConfigurationBuilder()
				.setOAuthConsumerKey("orEtfsb4RavNra0bAgfRUVSwF")
				.setOAuthConsumerSecret("6Kh0498MwgvDZW9qi1i0gRytpNaLvYY84I93ZLqzdAYdjCVIhy");
		Configuration config = builder.build();
		TwitterFactory twitterConn = new TwitterFactory(config);
		Twitter t = twitterConn.getInstance();
		AccessToken token = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
		t.setOAuthAccessToken(token);
		
		return t;
	}
	
	

}
