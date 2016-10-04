package de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum;

import hudson.model.AbstractBuild;

/**
 * BuildEnvVars.java
 * defines the keys of the environment variables of a build-job {@link AbstractBuild}
 *
 * @author Michael Sandritter
 */
public enum BuildEnvVars {

	JOB_NAME ("JOB_NAME"),
	GIT_URL ("GIT_URL"),
	GIT_COMMIT ("GIT_COMMIT"),
	GIT_TAG_NAME ("GIT_TAG_NAME"),
	WORKSPACE ("WORKSPACE"),
	SVN_REVISION ("SVN_REVISION"),
	SVN_URL ("SVN_URL"),
	JENKINS_URL ("JENKINS_URL"),
	JOB_URL ("JOB_URL");
	
    private final String name;       

    private BuildEnvVars(String s) 
    {
        name = s;
    }

    @Override
	public String toString() 
    {
       return this.name;
    }
}
