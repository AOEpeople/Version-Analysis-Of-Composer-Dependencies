package de.sandritter.version_analysis_of_build_dependencies.Util;

import static org.junit.Assert.*;
import org.junit.Test;

import de.sandritter.version_analysis_of_build_dependencies.Util.DateFormatter;

public class DateFormatterTest {

	@Test
	public void shouldGetFormattedTime()
	{
		long timestamp = 1460920662154L;
		String date = DateFormatter.getFormattedTime(timestamp);
		assertEquals("17.04.2016 - 19:17:42", date);
	}
	
	@Test
	public void shouldGetNoDateAvailableMessage()
	{
		long timestamp = -1;
		String date = DateFormatter.getFormattedTime(timestamp);
		assertEquals("no date available, timestamp value might be unset", date);
	}
	
	@Test
	public void shouldCreateDateFormatter()
	{
		@SuppressWarnings({ "unused"})
		DateFormatter dateFormatter = new DateFormatter();
	}
}
