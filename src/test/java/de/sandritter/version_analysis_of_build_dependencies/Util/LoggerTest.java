package de.sandritter.version_analysis_of_build_dependencies.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

public class LoggerTest {

	private Logger logger;
	private ClassLoader classLoader;
	
	@Before
	public void setUp() throws FileNotFoundException
	{
		classLoader = getClass().getClassLoader();
		String filePath = classLoader.getResource("test.log").getPath();
		this.logger = Logger.getInstance(new PrintStream(new File(filePath)));
	}
	
	@Test
	public void shouldLogPluginStart()
	{
		logger.logPluginStart();
	}
	
	@Test
	public void shouldLogFailure()
	{
		logger.logFailure(new Exception("test-message"), "test-failure");
	}
	
	@Test
	public void shouldFinalStatus()
	{
		logger.logFinalProcessStatus();
	}
	
	@Test
	public void shouldFinalStatusWithError()
	{
		logger.logFailure(new Exception("test-message"), "test-failure");
		logger.logFinalProcessStatus();
	}
	
	@Test
	public void shouldLogMilliseconds()
	{
		logger.logPluginStart();
		logger.logFinalProcessStatus();
	}

}
