

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.vdurmont.emoji.EmojiParser;

import utils.EmojiUtils;
import utils.HashtagUtils;
import utils.OpenCalaisClient;
import utils.SentimentAnalyzer;

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
                
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		        
		    out.print(json.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
