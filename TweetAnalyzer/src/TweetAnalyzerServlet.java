

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.vdurmont.emoji.EmojiParser;

import tweetdata.MyTweet;
import tweetdata.TweetEntity;
import tweetdata.TwitterManager;
import twitter4j.Status;
import utils.EmojiUtils;
import utils.HashtagUtils;
import utils.OpenCalaisClient;
import utils.SentimentAnalyzer;
import utils.StringSimilarity;
import utils.TweetParser;

/**
 * Servlet implementation class TweetAnalyzerServlet
 */
@WebServlet("/TweetAnalyzerServlet")
public class TweetAnalyzerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TweetAnalyzerServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String tweetText = request.getParameter("tweet_text");
		String stats = request.getParameter("stats");
		
		if (tweetText == null)
			return;
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		  //create Json Object
		  JSONObject json = new JSONObject();

		    
		    try {
		    	
                json.put("text", tweetText);
                String filename = getServletContext().getRealPath("") + "/resources/Emoji_Sentiment_Data_v1.0.csv";
                json.put("emoji_sentiment", EmojiUtils.getEmojisSentiment(filename, EmojiParser.extractEmojis(tweetText)));
                
                SentimentAnalyzer sentiment = new SentimentAnalyzer();
                json.put("text_sentiment", sentiment.getSentimentCodeFromTweetText(tweetText));
                
                HashtagUtils hashtagUtils = new HashtagUtils();
                String hashtags = hashtagUtils.getHashtags(tweetText);
                if(hashtags.isEmpty()) {
                	json.put("hashtag_sentiment", "-1");
                }else {
                	json.put("hashtag_sentiment", sentiment.getSentimentCodeFromTweetText(hashtags));
                }
                
                OpenCalaisClient oc = new OpenCalaisClient();
                json.put("ner", oc.getPropertyNamesAndValues(tweetText));
                
                if(stats != null && (stats.equals("1") || stats.equals("true"))) {
                	
                	MyTweet mt = new MyTweet(json.toString());
                	float stats_code = getTweetStats(tweetText, mt);
                	String stats_response = "We can not say precisely if the tweet is false or not";
                	json.put("stats_code", stats_code);
                	
                	if (stats_code == -1000) {
                		stats_response = "Something went wrong. Please retry!";
                		json.put("stats", stats_response);
                	} else {
                	
	                    if (stats_code == -500) {
	                    	stats_response = "We can not say precisely if the tweet is false or not";
	                    } else if (stats_code >= -100 && stats_code < 0) {
	                    	
	                    	if (stats_code >= -100 && stats_code < -50) {
	                    		stats_response =  "Tweet is not fake. Confidence: " + Float.toString(Math.abs(stats_code)) +". Less than 10 sources found.";
	                    	} else  if (stats_code >= -50 && stats_code < -0) {
	                    		stats_response =  "Tweet is fake. Confidence: " + Float.toString(100 - Math.abs(stats_code)) +". Less than 10 sources found.";
	                    	}
	                    } else if (stats_code > 0) {
	                    	if (stats_code <=100 && stats_code >= 50) {
	                    		stats_response =  "Tweet is not fake. Confidence: " + Float.toString(stats_code);
	                    	} else  if (stats_code < 50) {
	                    		stats_response =  "Tweet is fake. Confidence: " + Float.toString(100 - stats_code);
	                    	}
	                    } else {
	                    	stats_response = "We can not say precisely if the tweet is false or not";
	                    }
	                    
	                    json.put("stats", stats_response);
	                    if(jsonSimilarTweets != null) {
	                    	json.put("similar_tweets", jsonSimilarTweets);
	                    }
                	}
                   
             
                }
                
                
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		        
		    out.print(json.toString());
		
		
	}
	
	JSONObject jsonSimilarTweets;
	
	public float getTweetStats(String tweet_text, MyTweet tweet) {
		
		jsonSimilarTweets = new JSONObject();
		
		float score = 0;
		boolean notSure = false;
		
		TweetParser tp = new TweetParser();
		
		StringSimilarity ss = new StringSimilarity();
		
		try {
			//MyTweet tweet = tp.getParsedTweet(removeUrl(tweet_text.replaceAll("(\\r|\\n)", "")));
			
//			System.out.println("Text:" + tweet.getText());
//			System.out.println("Hashtag sentiment:" + tweet.getHashtagSentiment());
//			System.out.println("Emoji sentiment:" + tweet.getEmojiSentiment());
//			System.out.println("Text sentiment:" + tweet.getTextSentiment());
//			for (TweetTopics topics : tweet.getTopics()) {
//				System.out.println("Topic:" + topics.getName() + ", confidence:" + topics.getConfidence());
//			}
//			for (TweetSocial social : tweet.getSocialTags()) {
//				System.out.println("Social:" + social.getName() + ", importance:" + social.getImportance());
//			}
//			for (TweetIndustry industry : tweet.getIndustryTags()) {
//				System.out.println("Industry:" + industry.getName() + ", relevance:" + industry.getRelevance());
//			}
//			for (TweetEntity entity : tweet.getEntities()) {
//				System.out.println("Entity:" + entity.getName() + ", relevance:" + entity.getRelevance() + ", type:" + entity.getType());
//			}
			
			int originalTweetTextSentiment = tweet.getTextSentiment();
			int originalTweetHashtagSentiment = tweet.getHashtagSentiment();
			int originalTweetEmojiSentiment = tweet.getEmojiSentiment();
			
			int originalTweetEntitiesSize = tweet.getEntities().size();
			
			if (originalTweetEntitiesSize == 0) {
				return -500;
			}
			
			
			Set<String> originalTweetEntities = new HashSet<String>();
			for (TweetEntity entity : tweet.getEntities()) {
				originalTweetEntities.add(entity.getName().replace("the ", "").toLowerCase());
			}
			
//			System.out.println("Entities prelucrate: ");
//			System.out.println(originalTweetEntities);
//			System.out.println("");
			
			TwitterManager tweetManager = new TwitterManager();
			List<Status> tweets = tweetManager.searchTweets(tweet_text);
			
			
			int nrTweets = 0;
			
			JSONObject jsonTweets = new JSONObject();
			
			for (Status s : tweets) {
				
				if (s.getUser().isVerified()) {
				
					Double similiratyTweets = ss.similarity(tweet_text, s.getText());
					
					System.out.println(removeUrl(s.getText().replaceAll("(\\r|\\n)", "")));
					System.out.println(similiratyTweets);
					
					
				
				
					if(similiratyTweets >= 0.25){
						
						score += 10;
						
						MyTweet similarTweet = tp.getParsedTweet(removeUrl(s.getText().replaceAll("(\\r|\\n)", "")));
	//					System.out.println("Text:" + similarTweet.getText());
	//					System.out.println("Hashtag sentiment:" + similarTweet.getHashtagSentiment());
	//					System.out.println("Emoji sentiment:" + similarTweet.getEmojiSentiment());
	//					System.out.println("Text sentiment:" + similarTweet.getTextSentiment());
	//					for (TweetTopics topics : similarTweet.getTopics()) {
	//						System.out.println("Topic:" + topics.getName() + ", confidence:" + topics.getConfidence());
	//					}
	//					for (TweetSocial social : similarTweet.getSocialTags()) {
	//						System.out.println("Social:" + social.getName() + ", importance:" + social.getImportance());
	//					}
	//					for (TweetIndustry industry : similarTweet.getIndustryTags()) {
	//						System.out.println("Industry:" + industry.getName() + ", relevance:" + industry.getRelevance());
	//					}
	//					for (TweetEntity entity : similarTweet.getEntities()) {
	//						System.out.println("Entity:" + entity.getName() + ", relevance:" + entity.getRelevance() + ", type:" + entity.getType());
	//					}
					
						
						if (Math.abs(originalTweetTextSentiment - similarTweet.getTextSentiment()) > 2) {
							score -= 10;
						} else {
							
							if (originalTweetHashtagSentiment != -1 && Math.abs(originalTweetHashtagSentiment - similarTweet.getHashtagSentiment()) > 2) {
								score -= 2.5;
							}
							
							if (originalTweetEmojiSentiment != -1 && Math.abs(originalTweetEmojiSentiment - similarTweet.getEmojiSentiment()) > 2) {
								score -= 2.5;
							}
						
							Set<String> tweetEntities = new HashSet<String>();
							
							for (TweetEntity entity : similarTweet.getEntities()) {
								tweetEntities.add(entity.getName().replace("the ", "").toLowerCase());
							}
		
							Set<String> intersection = new HashSet<String>(originalTweetEntities);
							intersection.retainAll(tweetEntities);
							
							System.out.println(originalTweetEntitiesSize);
							System.out.println(intersection.size());
							System.out.println(tweetEntities);
							
							float scoreEntities = ((float) intersection.size()) / originalTweetEntitiesSize;
							System.out.println("Score entities: ");
							System.out.println(scoreEntities);
							
							if (scoreEntities == 1) {
								score += 40;
							} else if (scoreEntities >= 0.5) {
								score += 10;
							} else if (scoreEntities >= 0.1) {
								score += 5;
							}
							
							JSONObject json = new JSONObject();
							
							//json.put("tweet_text", s.getText());
							json.put("similiraty_text", similiratyTweets);
							json.put("similiraty_entities", scoreEntities);
							json.put("user_name", s.getUser().getName());
							json.put("user_descripton", s.getUser().getDescription());
							
							jsonTweets.put(removeUrl(s.getText().replaceAll("(\\r|\\n)", "")), json);
							
							nrTweets +=1;
							
							if(score > 100) {
								score = 100;
								break;
							}
						}
						
						System.out.println("----------------------");
						System.out.println("");
						
					}
				}
			}

			if (nrTweets == 0) {
				return -500;
			}
			
			
			if (nrTweets <= 1) {
				notSure = true;
			}
			
			jsonSimilarTweets.put("tweets", jsonTweets);
			
			jsonSimilarTweets.put("nr_analyzed_tweets", tweets.size());
			jsonSimilarTweets.put("nr_verified_users", nrTweets);
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			score = -1000;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(score > 100) {
			score = 100;
		}
		
		if (notSure) {
			score = score * -1;
		}
		
		return score;
		
	}
	
	
	private String removeUrl(String commentstr)
    {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i),"").trim();
            i++;
        }
        return commentstr;
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
