package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OpenCalaisClient {
	
	public JSONObject getPropertyNamesAndValues(String text) throws IOException {
		
		URL url = new URL(" https://api.thomsonreuters.com/permid/calais");
		URLConnection con = url.openConnection();
		HttpURLConnection http = (HttpURLConnection)con;
		
		byte[] out = (text).getBytes(StandardCharsets.UTF_8);
		int length = out.length;

		http.setRequestMethod("POST"); // PUT is another valid option
		http.setDoOutput(true);
		http.setFixedLengthStreamingMode(length);
		http.setRequestProperty("x-ag-access-token", "mActA4rXkROkw0IzYe25gROo50OEiFng");
		http.setRequestProperty("Content-Type", "text/raw");
		http.setRequestProperty("outputFormat", "application/json");
		http.setRequestProperty("x-calais-language", "english");
		http.setDoOutput(true);
		http.setDoInput(true);
		http.connect();
		
		
		JSONObject jsonFinal = new JSONObject();
		
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
	
		try {
			JSONObject jsonResponse = new JSONObject(result.toString());
			
			JSONArray jsonArrayTopics = new JSONArray();
			JSONArray jsonArraySocialTags = new JSONArray();
			JSONArray jsonArrayIndustryTags = new JSONArray();
			JSONArray jsonArrayEntities = new JSONArray();
			
			Iterator<String> keysJsonResponse = jsonResponse.keys();

		    while (keysJsonResponse.hasNext()) {
		        String keyJsonResponse = keysJsonResponse.next();
		        
		        JSONObject jsonInt = new JSONObject(jsonResponse.get(keyJsonResponse).toString());
		        
		        
		        Iterator<String> keysJsonInt = jsonInt.keys();
		       
		        while (keysJsonInt.hasNext()) {
		        	
			        String keyJsonInt = keysJsonInt.next();
			        
			        if (keyJsonInt.equals("_typeGroup")) {
			        	
			        	if (jsonInt.get(keyJsonInt).toString().equals("topics")) {
			        		
			        		JSONObject jsonObj = new JSONObject();
			        		if (jsonInt.has("name")) {
			        			jsonObj.put("name", jsonInt.get("name"));
			        		}
			        		if (jsonInt.has("score")) {
			        			jsonObj.put("confidence", jsonInt.get("score"));
			        		}
			        		jsonArrayTopics.put(jsonObj);
			        	}
			        	
			        	if (jsonInt.get(keyJsonInt).toString().equals("socialTag")) {
			        		
			        		JSONObject jsonObj = new JSONObject();
			        		if (jsonInt.has("name")) {
			        			jsonObj.put("name", jsonInt.get("name"));
			        		}
			        		if (jsonInt.has("importance")) {
			        			jsonObj.put("importance", jsonInt.get("importance"));
			        		}
			        		jsonArraySocialTags.put(jsonObj);
			        	}
			        	
			        	if (jsonInt.get(keyJsonInt).toString().equals("industry")) {
			        		
			        		JSONObject jsonObj = new JSONObject();
			        		if (jsonInt.has("name")) {
			        			jsonObj.put("name", jsonInt.get("name"));
			        		}
			        		if (jsonInt.has("relevance")) {
			        		jsonObj.put("relevance", jsonInt.get("relevance"));
			        		}
			        		jsonArrayIndustryTags.put(jsonObj);
			        	}
			        	
			        	if (jsonInt.get(keyJsonInt).toString().equals("entities")) {
			        		
			        		JSONObject jsonObj = new JSONObject();
			        		if (jsonInt.has("name")) {
			        			jsonObj.put("name", jsonInt.get("name"));
			        		}
			        		if (jsonInt.has("_type")) {
			        			jsonObj.put("type", jsonInt.get("_type"));
			        		}
			        		if (jsonInt.has("confidencelevel")) {
			        			jsonObj.put("confidence", jsonInt.get("confidencelevel"));
			        		}
			        		if (jsonInt.has("relevance")) {
			        			jsonObj.put("relevance", jsonInt.get("relevance"));
			        		}
			        		jsonArrayEntities.put(jsonObj);
			        	}
			        	
			        	
			        }
		        }
		        
		    }
		    
		    
		    if(jsonArrayTopics != null && jsonArrayTopics.length() > 0 ){
		    	JSONObject jsonObj = new JSONObject();
		    	jsonObj.put("topics", jsonArrayTopics);
		    	jsonFinal.put("topics", jsonArrayTopics);
		    }
		    
		    if(jsonArraySocialTags != null && jsonArraySocialTags.length() > 0 ){
		    	JSONObject jsonObj = new JSONObject();
		    	jsonObj.put("social_tags", jsonArraySocialTags);
		    	jsonFinal.put("social_tags", jsonArraySocialTags);
		    }
		    
		    if(jsonArrayIndustryTags != null && jsonArrayIndustryTags.length() > 0 ){
		    	JSONObject jsonObj = new JSONObject();
		    	jsonObj.put("industry_tags", jsonArrayIndustryTags);
		    	jsonFinal.put("industry_tags", jsonArrayIndustryTags);
		    }
		    
		    if(jsonArrayEntities != null && jsonArrayEntities.length() > 0 ){
		    	JSONObject jsonObj = new JSONObject();
		    	jsonObj.put("entities", jsonArrayEntities);
		    	jsonFinal.put("entities", jsonArrayEntities);
		    }
		    
		    return jsonFinal;
	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonFinal;
		
	}

}

