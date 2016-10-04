package de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum;

/**
 * DependencyType.java
 * defines the dependency types of an installed component of a build
 *
 * @author Michael Sandritter
 */
public enum DependencyType {
	MAIN ("main"), 
	HIGH_LEVEL ("direct"),
	LOW_LEVEL ("indirect"),
	ALL ("all");
	
	private final String name;       

    private DependencyType(String s) 
    {
        name = s;
    }

    @Override
	public String toString() 
    {
       return this.name;
    }
}
