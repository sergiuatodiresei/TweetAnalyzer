package tweetdata;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterManager {

	Twitter twitter;
	
	public TwitterManager() {
		this.twitter = TwitterConnection.getTwitterInstance(); 
	}
	
	public List<Status> searchTweets(String q){
		try {
		Query query = new Query(q);
		query.setLang("en");
		query.setCount(100);
		QueryResult result;
		List<Status> statusList = new ArrayList<Status>();
		do {
		
			result = twitter.search(query);
			statusList.addAll(result.getTweets());
			
		
		} while ((query = result.nextQuery()) != null);
		System.out.println("-----------------------" + Integer.toString(statusList.size()));
		return statusList;
		}
		
		catch (TwitterException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
		
	}
	
	public Status getTweetById(String id) {
		try {
			Status status = twitter.showStatus(Long.parseLong(id));
			if (status == null) return null;
			return status;
		}catch(TwitterException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
	
	public void parseTweet(Status s) {
		System.out.println("User:" + s.getUser().getScreenName());
		System.out.println("Tweet:" + s.getText());
		//System.out.println("Location. Latitude:" + String.valueOf(s.getGeoLocation().getLatitude()) + " | Longitude" + String.valueOf(s.getGeoLocation().getLongitude()));
		System.out.println("Lang:" + s.getLang());
		//s.getUser().isVerified();
	}
	
	
}
