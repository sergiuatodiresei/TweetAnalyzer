package tweetdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyTweet {

    private static final String JSON_KEY_HASHTAG_SENTIMENT = "hashtag_sentiment";
    private static final String JSON_KEY_TEXT_SENTIMENT = "text_sentiment";
    private static final String JSON_KEY_EMOJI_SENTIMENT = "emoji_sentiment";
    private static final String JSON_KEY_NER = "ner";
    private static final String JSON_KEY_TOPICS = "topics";
    private static final String JSON_KEY_SOCIAL_TAGS = "social_tags";
    private static final String JSON_KEY_INDUSTRY_TAGS = "industry_tags";
    private static final String JSON_KEY_ENTITIES = "entities";
    private static final String JSON_KEY_IMPORTANCE = "importance";
    private static final String JSON_KEY_CONFIDENCE = "confidence";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_RELEVANCE = "relevance";
    private static final String JSON_KEY_TYPE = "type";
    private static final String JSON_KEY_TEXT = "text";


    private List<TweetEntity> entities = new ArrayList<>();
    private List<TweetTopics> topics = new ArrayList<>();
    private List<TweetSocial> socialTags = new ArrayList<>();
    private List<TweetIndustry> industryTags = new ArrayList<>();
    private int emojiSentiment;
    private int hashtagSentiment;
    private int textSentiment;
    private String text;

    public MyTweet(String rawTweet) {
        parseJsonTweet(rawTweet);
    }

    private void parseJsonTweet(String rawTweet) {
        try {
            JSONObject object = new JSONObject(rawTweet);
            text = object.optString(JSON_KEY_TEXT);
            hashtagSentiment = object.optInt(JSON_KEY_HASHTAG_SENTIMENT);
            textSentiment = object.optInt(JSON_KEY_TEXT_SENTIMENT);
            emojiSentiment = object.optInt(JSON_KEY_EMOJI_SENTIMENT);
            JSONObject nerObject = object.optJSONObject(JSON_KEY_NER);
            if (nerObject != null) {
            	
                JSONArray topicsArray = nerObject.optJSONArray(JSON_KEY_TOPICS);
                if (topicsArray != null) {
                	
	                for (int i = 0; i < topicsArray.length(); i++) {
	                    JSONObject obj = topicsArray.optJSONObject(i);
	                    if (obj != null) {
	                        TweetTopics topic = new TweetTopics(
	                                obj.optInt(JSON_KEY_CONFIDENCE),
	                                obj.optString(JSON_KEY_NAME)
	                        );
	                        topics.add(topic);
	                    }
	                }
                }
                
                JSONArray socialArray = nerObject.optJSONArray(JSON_KEY_SOCIAL_TAGS);
                if (socialArray != null) {
                	
	                for (int i = 0; i < socialArray.length(); i++) {
	                    JSONObject obj = socialArray.optJSONObject(i);
	                    if (obj != null) {
	                        TweetSocial s = new TweetSocial(
	                                obj.optString(JSON_KEY_IMPORTANCE),
	                                obj.optString(JSON_KEY_NAME)
	                        );
	                        socialTags.add(s);
	                    }
	                }
                }
                
                JSONArray industryArray = nerObject.optJSONArray(JSON_KEY_INDUSTRY_TAGS);
                if (industryArray != null) {
	                
	                for (int i = 0; i < industryArray.length(); i++) {
	                    JSONObject obj = industryArray.optJSONObject(i);
	                    if (obj != null) {
	                        TweetIndustry industry = new TweetIndustry(
	                                obj.optString(JSON_KEY_NAME),
	                                obj.optDouble(JSON_KEY_RELEVANCE)
	                        );
	                        industryTags.add(industry);
	                    }
	                }
                }
                
                JSONArray entitiesArray = nerObject.optJSONArray(JSON_KEY_ENTITIES);
                if (entitiesArray != null) {
                	
	                for (int i = 0; i < entitiesArray.length(); i++) {
	                    JSONObject obj = entitiesArray.optJSONObject(i);
	                    if (obj != null) {
	                        TweetEntity e = new TweetEntity(
	                                obj.optString(JSON_KEY_NAME),
	                                obj.optString(JSON_KEY_TYPE),
	                                obj.optDouble(JSON_KEY_RELEVANCE)
	                        );
	                        entities.add(e);
	                    }
	                }
                }
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public List<TweetEntity> getEntities() {
        return entities;
    }

    public List<TweetTopics> getTopics() {
        return topics;
    }

    public List<TweetSocial> getSocialTags() {
        return socialTags;
    }

    public List<TweetIndustry> getIndustryTags() {
        return industryTags;
    }

    public int getEmojiSentiment() {
        return emojiSentiment;
    }

    public int getHashtagSentiment() {
        return hashtagSentiment;
    }

    public int getTextSentiment() {
        return textSentiment;
    }

    public String getText() {
        return text;
    }
}
