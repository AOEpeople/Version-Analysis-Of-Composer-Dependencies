package de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer;

import de.sandritter.version_analysis_of_build_dependencies.Util.DateFormatter;

/**
 * BuildData.java 
 * used as transfer object for transfering all 
 * build-specific information
 *
 * @author Michael Sandritter
 */
public class BuildData {

	/**
	 * build identifier
	 */
	private String buildId;

	/**
	 * jenkins job name
	 */
	private String jobName;

	/**
	 * path to database
	 */
	private String dbPath;

	/**
	 * continuous build number
	 */
	private int number;

	/**
	 * time stamp of the time when the build started
	 */
	private long timestamp = -1;

	/**
	 * checked out version of the source code checked out from the repository
	 */
	private String reference;

	/**
	 * repository URL
	 */
	private String sourceUrl;

	/**
	 * type of the repository (Git or SVN)
	 */
	private String sourceType;

	/**
	 * build category (integration or component)
	 */
	private String buildCategory;

	/**
	 * tag name of the reference
	 */
	private String version;

	private String jenkinsUrl;
	
	private String jobUrl;

	public String getBuildId()
	{
		return buildId;
	}

	public void setBuildId(String buildId)
	{
		this.buildId = buildId;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getRevision()
	{
		return reference;
	}

	public void setRevision(String revision)
	{
		this.reference = revision;
	}

	public String getSourceUrl()
	{
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl)
	{
		this.sourceUrl = sourceUrl;
	}

	public String getSourceType()
	{
		return sourceType;
	}

	public void setSourceType(String sourceType)
	{
		this.sourceType = sourceType;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getDbPath()
	{
		return dbPath;
	}

	public void setDbPath(String dbPath)
	{
		this.dbPath = dbPath;
	}

	public String getJobName()
	{
		return jobName;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	/**
	 * formatted time dd.MM.yyyy - HH:mm:ss
	 * 
	 * @return formatted time dd.MM.yyyy - HH:mm:ss
	 */
	public String getFormattedTime()
	{
		return DateFormatter.getFormattedTime(timestamp);
	}

	public String getJenkinsUrl()
	{
		return jenkinsUrl;
	}

	public void setJenkinsUrl(String jenkinsUrl)
	{
		this.jenkinsUrl = jenkinsUrl;
	}

	public String getJobUrl()
	{
		return jobUrl;
	}

	public void setJobUrl(String jobUrl)
	{
		this.jobUrl = jobUrl;
	}

}
