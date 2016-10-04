package de.sandritter.version_analysis_of_build_dependencies.Util;

import static org.junit.Assert.*;
import java.util.Properties;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.Test;

public class PropertyReaderTest {
	
	@Test
	public void shouldGetInstance()
	{
		PropertyReader reader = PropertyReader.getInstance();
		assertThat(reader, instanceOf(PropertyReader.class));
	}
	
	@Test
	public void shouldGetApiTokenFromConfig()
	{
		PropertyReader reader = PropertyReader.getInstance();
		Properties config = reader.getConfig("/config.properties");
		assertEquals("1463644124", config.getProperty("api-token"));
	}
	
	@Test
	public void shouldGetJenkinsLogFilePathFromConfig()
	{
		PropertyReader reader = PropertyReader.getInstance();
		Properties config = reader.getConfig("/config.properties");
		assertEquals("/var/log/jenkins/jenkins.log", config.getProperty("jenkins-log"));
	}
	
	@Test(expected=NullPointerException.class)
	public void shouldThrowNullPointerExceptionWithInvalidConfigPath()
	{
		PropertyReader reader = PropertyReader.getInstance();
		Properties config = reader.getConfig("/not_existing_config.properties");
		assertEquals("/var/log/jenkins/jenkins.log", config.getProperty("jenkins-log"));
	}
}
