package de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database;

import de.sandritter.version_analysis_of_build_dependencies.Util.DateFormatter;

/**
 * ComponentSummary.java 
 * The ComponentSummary-Object is used to hold component-
 * and version-specific information, that has been loaded from the database
 *
 * @author Michael Sandritter
 *
 */
public class ComponentSummary implements Cloneable {

	/**
	 * component name
	 */
	private String componentName;

	/**
	 * reference of a unique version of component
	 */
	private String reference;

	/**
	 * version name
	 */
	private String version;

	/**
	 * build identifier
	 */
	private String buildId;

	/**
	 * dependency type - main component / dependency
	 */
	private String dependencyType;

	/**
	 * repository url
	 */
	private String sourceUrl;

	/**
	 * repository type - Git/SVN
	 */
	private String sourceType;

	/**
	 * timestamp of the related build-start
	 */
	private long timestamp = -1;

	public String getComponentName()
	{
		return componentName;
	}

	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getBuildId()
	{
		return buildId;
	}

	public void setBuildId(String buildId)
	{
		this.buildId = buildId;
	}

	public String getDependencyType()
	{
		return dependencyType;
	}

	public void setDependencyType(String dependencyType)
	{
		this.dependencyType = dependencyType;
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

	public long getTimestamp()
	{
		return timestamp;
	}

	public String getLightReference()
	{
		if (reference != null && reference.length() > 7) {
			return reference.substring(0, 7) + "***";
		}
		return reference;
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

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
}