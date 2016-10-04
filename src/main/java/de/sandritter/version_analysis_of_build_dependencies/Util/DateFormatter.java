package de.sandritter.version_analysis_of_build_dependencies.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * DateFormatter.java
 * util object that formats timestamp to readable time
 *
 * @author Michael Sandritter
 */
public class DateFormatter {
	
	/**
	 * is formating a timestamp to readble time  dd.MM.yyyy - HH:mm:ss
	 * @param timestamp
	 * @return formatted and readable time of the build-start
	 */
	public static String getFormattedTime(long timestamp){
		if(timestamp != -1){			
			Date date = new Date(timestamp); 
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY - HH:mm:ss"); 
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			String formattedDate = sdf.format(date);
			return formattedDate;
		} 
		return "no date available, timestamp value might be unset";
	}
}
