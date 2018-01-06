package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class SentimentAnalyzer {
	
	public int getSentimentCodeFromTweetText(String text) throws IOException {
		
		URL url = new URL("http://www.sentiment140.com/api/bulkClassifyJson");
		URLConnection con = url.openConnection();
		HttpURLConnection http = (HttpURLConnection)con;
		
		byte[] out = ("{\"data\": [{\"text\": \" " + text + " \"}]}" ).getBytes(StandardCharsets.UTF_8);
		int length = out.length;

		http.setRequestMethod("POST"); // PUT is another valid option
		http.setDoOutput(true);
		http.setFixedLengthStreamingMode(length);
		http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		http.connect();
		try(OutputStream os = http.getOutputStream()) {
		    os.write(out);
		}
		
		
		
		InputStream in = http.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	
		StringBuilder result = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null) {
		    result.append(line);
		}
		//System.out.println(result.toString());
	
		try {
			JSONObject jsonObj = new JSONObject(result.toString());
			
			JSONArray jsonArray = new JSONArray(jsonObj.get("data").toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			    org.json.JSONObject json = jsonArray.getJSONObject(i);
			    Iterator<String> keys = json.keys();

			    while (keys.hasNext()) {
			        String key = keys.next();
			       
			        if (key.equals("polarity")) {
			        	return Integer.parseInt(json.get(key).toString());
			        }
			    }

			}
		} catch (JSONException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
		
	}

}
