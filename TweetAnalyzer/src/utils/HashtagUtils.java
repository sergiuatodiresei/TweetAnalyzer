package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashtagUtils {
	
	public String getHashtags(String text) {
		
		Pattern MY_PATTERN = Pattern.compile("#(\\S+)");
		Matcher mat = MY_PATTERN.matcher(text);
		StringBuilder tmp = new StringBuilder();
		
		while (mat.find()) {
		
		  tmp.append(mat.group(1));
		  tmp.append(" ");
		}
		
		return tmp.toString();
	}

}
