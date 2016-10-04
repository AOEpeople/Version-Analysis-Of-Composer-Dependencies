package de.sandritter.version_analysis_of_build_dependencies.Domain.Model.DAO;

/**
 * Build.java the build class is a data-access-objet that holds build specific
 * informations
 *
 * @author Michael Sandritter
 */
public class Build {

	/**
	 * build identifier
	 */
	private String buildId;

	/**
	 * timestamp of the build start
	 */
	private long timestamp;

	/**
	 * build number
	 */
	private int number;

	/**
	 * name of the jenkins build job
	 */
	private String jobName;
	 
	/**
	 * url to the build-job page
	 */
	private String jobUrl;

	/**
	 * 
	 * @param buildId build identifier
	 * @param timestamp timestamp of the build start
	 * @param number build number
	 * @param jobName job name of the build job
	 */
	public Build(String buildId, long timestamp, int number, String jobName, String jobUrl)
	{
		this.setBuildId(buildId);
		this.setTimestamp(timestamp);
		this.setNumber(number);
		this.setJobName(jobName);
		this.setJobUrl(jobUrl);
	}

	public String getBuildId()
	{
		return buildId;
	}

	public void setBuildId(String buildId)
	{
		this.buildId = buildId;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public String getJobName()
	{
		return jobName;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
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
