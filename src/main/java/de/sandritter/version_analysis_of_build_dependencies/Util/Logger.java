package de.sandritter.version_analysis_of_build_dependencies.Util;

import java.io.PrintStream;

import hudson.model.BuildListener;

/**
 * Logger.java This class is a singleton helper to log all relevant messages
 * including warnings and error to console output
 *
 * @author Michael Sandritter
 */
public class Logger {

	public static final String LABEL = "\t[va]: ";
	public static final String SUCCESS = "-> SUCCESS: ";
	public static final String WARNING = "-> WARNING: ";
	private static PrintStream out;
	private static Logger logger = null;
	private long startTime = 0;
	private int errorCount = 0;

	private Logger(){}

	/**
	 * @param listener {@link BuildListener}
	 * @return
	 */
	public static Logger getInstance(PrintStream stream)
	{
		logger = new Logger();
		out = stream;
		return logger;
	}
	
	public static Logger getInstance()
	{
		return logger;
	}

	/**
	 * prints a String to console output
	 * 
	 * @param s
	 */
	public void log(String s)
	{
		out.println(s);
	}

	/**
	 * prints an empty line to console ouput
	 */
	public void println()
	{
		out.println();
	}

	public void logPluginStart()
	{
		startTime = System.currentTimeMillis();
		println();
		log("[VERSION ANALYSIS] : [va]");
	}

	/**
	 * calculates the time the plugin needed to process all his work and logs it
	 * in the console output of a build
	 * 
	 * @param start
	 *            start time
	 * @param end
	 *            end time
	 */
	private void logElapsedTime(long end)
	{
		long diff = end - startTime;
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		if (diff < 1000) {
			log(LABEL + "TOTEL TIME ELAPSED: " + diff + " milliseconds.");
		} else {
			log(LABEL + "TOTEL TIME ELAPSED: " + diffMinutes + " minutes " + diffSeconds + " seconds.");
		}
	}

	public void logFailure(Exception e, String title)
	{
		log(Logger.LABEL + Logger.WARNING + "-> " + title + ": ");
		log(Logger.LABEL + Logger.WARNING + "error message: " + e.getMessage());
		for (StackTraceElement s : e.getStackTrace()) {
			log(s.toString());
		}
		println();
		errorCount++;
	}

	/**
	 * logs the final process status of this plugin
	 * 
	 * @param start - timestamp of the time when the peform method was invoked
	 */
	public void logFinalProcessStatus()
	{
		long end = System.currentTimeMillis();
		if (errorCount == 0) {
			log(
				Logger.LABEL + Logger.SUCCESS + "Version Analysis Of Build Dependencies ended successfully"
			);
		} else {
			log(
				Logger.LABEL + Logger.WARNING
				+ "Integration Analysis Of Integration Dependencies DIDN'T end successful"
			);
			log(Logger.LABEL + "warnings occured: " + errorCount);
		}
		log(Logger.LABEL + "STARTED at: " + DateFormatter.getFormattedTime(startTime));
		log(Logger.LABEL + "ENDED at: " + DateFormatter.getFormattedTime(startTime));
		logElapsedTime(end);
		println();
	}
}
