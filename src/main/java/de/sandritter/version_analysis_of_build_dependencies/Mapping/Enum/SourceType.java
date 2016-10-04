package de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum;

/**
 * SourceType.java
 * enum that defines different version control source types
 *
 * @author Michael Sandritter
 */
public enum SourceType {
	GIT ("git"),
	SVN ("svn"),
	CVS ("cvs");
	
	private final String name;
	
	private SourceType (String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}