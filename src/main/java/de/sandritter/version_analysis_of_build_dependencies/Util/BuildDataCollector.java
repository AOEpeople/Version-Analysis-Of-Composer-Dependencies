package de.sandritter.version_analysis_of_build_dependencies.Util;

import java.io.IOException;

import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.BuildEnvVars;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.SourceType;
import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

/**
 * 
 * @author Michael Sandritter
 *
 */
public class BuildDataCollector {
	
	private AbstractBuild<?, ?> build;
	private BuildListener listener;
	
	/**
	 * 
	 * @param build {@link AbstractBuild}
	 * @param listener {@link BuildListener}
	 */
	public BuildDataCollector(AbstractBuild<?, ?> build, BuildListener listener)
	{
		this.build = build;
		this.listener = listener;
	}
	
	/**
	 * collects build specific data
	 * 
	 * @return BuildData object that holds all relevant build-specific data
	 */
	public BuildData collectBuildData()
	{
		BuildData data = new BuildData();
		EnvVars env = null;
		try {
			env = build.getEnvironment(listener);
			data = readEnvironment(data, env);
		} catch (IOException e) {
			//logger.logFailure(e, "LOADING ENVIRONMENT OF BUILD FAILED");
		} catch (InterruptedException e) {
			//logger.logFailure(e, "LOADING ENVIRONMENT OF BUILD STOPPED");
		} 
		return data;
	}
	
	private BuildData readEnvironment(BuildData data, EnvVars env)
	{
		data.setJobName(getEnvVarValueByType(env, BuildEnvVars.JOB_NAME));
		data.setJenkinsUrl(getEnvVarValueByType(env, BuildEnvVars.JENKINS_URL));
		data.setJobUrl(getEnvVarValueByType(env, BuildEnvVars.JOB_URL));
		data.setBuildId(build.getId() + getEnvVarValueByType(env, BuildEnvVars.JOB_NAME));
		data.setNumber(build.getNumber());
		data.setTimestamp(build.getTimeInMillis());
		injectVersionControlInfo(data, env);
		return data;
	}
	
	/**
	 * checks if main component is stored in svn or git and adds the relevant
	 * data to {@link BuildData}
	 * 
	 * @param data {@link BuildData}
	 * @param env environment variables of {@link AbstractBuild}
	 */
	private void injectVersionControlInfo(BuildData data, EnvVars env)
	{
		String svnRevision = getEnvVarValueByType(env, BuildEnvVars.SVN_REVISION);
		String gitRevision = getEnvVarValueByType(env, BuildEnvVars.GIT_COMMIT);
		if (gitRevision != null) {
			data.setSourceType(SourceType.GIT.toString());
			data.setSourceUrl(getEnvVarValueByType(env, BuildEnvVars.GIT_URL));
			data.setRevision(gitRevision);
			data.setVersion(getEnvVarValueByType(env, BuildEnvVars.GIT_TAG_NAME));
		} else if (svnRevision != null) {
			data.setSourceType(SourceType.SVN.toString());
			data.setSourceUrl(getEnvVarValueByType(env, BuildEnvVars.SVN_URL));
			data.setRevision(svnRevision);
			data.setVersion(svnRevision);
		}
	}
	
	private String getEnvVarValueByType(EnvVars env, BuildEnvVars varType){
		String var = "";
		try {
			var = env.get(varType.toString());
		} catch (Exception e) {
			//logger.logFailure(e, "couldn't get build environment variable with following key: "+ varType.toString());
			return "";
		}
		return var;
	}
}
