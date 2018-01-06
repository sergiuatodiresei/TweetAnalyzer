/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sergiu
 */
public class EmojiUtils {
    
    public static int NEGATIV = 0;
	public static int NEUTRU = 2;
	public static int POZITIV = 4;
        
  
	public static int getEmojisSentiment(String filename, List<String> emojiList){
		
		if (emojiList.isEmpty()) {
			return -1;
		}
		
		String csvFile = filename;
		String line = "";
		String cvsSplitBy = ",";
		int sum = 0;
		int emojiNumber = emojiList.size();
		boolean isFirstLine = true;
		Map<String, Integer> emojis = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			
				while ((line = br.readLine()) != null) {
					if (!isFirstLine) {
						String[] emoji = line.split(cvsSplitBy);
						
						emojis.put(emoji[0], getSentiment(Double.parseDouble(emoji[4]), Double.parseDouble(emoji[5]), Double.parseDouble(emoji[6])));
					}
					isFirstLine = false;
			}
			

		} catch (IOException e) {
                    e.printStackTrace();
		}
		
		for (String em : emojiList) {
			int sentiment = emojis.get(em);
			sum += sentiment;
			
		}
               
		
		float mean = (float)sum/emojiNumber;
		if (mean >= 2.5) {
			return POZITIV;
		} else {
			if (mean >= 1) {
				return NEUTRU;
			} 
			return NEGATIV;
		}
			
		

	}
	
	public static int getSentiment(Double neg, Double neu, Double pos ) {
		if (neg > neu && neg > pos) {
			return NEGATIV;
		}
		if (neu > neg && neu > pos) {
			return NEUTRU;
		}
		return POZITIV;
		
	}
    
}
