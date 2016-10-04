package de.sandritter.version_analysis_of_build_dependencies;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.kohsuke.stapler.StaplerProxy;

import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Interface.DataLoader;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Analyse.AnalyseResult;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Analyse.DependencyResult;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Analyse.Disparity;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.BuildSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.ComponentSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.DependencyType;
import hudson.PluginWrapper;
import hudson.model.Action;
import jenkins.model.Jenkins;

/**
 * IntegrationAnalyser.java
 * 
 * This class is analyzing the installed components of a build. The installed
 * components (total set) are compared with the subset of component which are
 * tested against each other. The {@link IntegrationAnalyser} is looking for
 * version stand disparities between the subsets and the total set.
 * 
 * implements {@link Action} and {@link StaplerProxy}
 *
 * @author Michael Sandritter
 */
public class IntegrationAnalyser implements Action, StaplerProxy {

	private DataLoader dataLoader;
	private AnalyseResult analyseResult;
	public static final String SUB_URL = "intregration-analysis";
	private String pluginName;

	/** 
	 * @param buildData {@link BuildData}
	 * @param dataLoader {@link DataLoader}
	 * @throws Exception in case the integration analysis failed
	 */
	public IntegrationAnalyser(String pluginName, BuildData buildData, DataLoader dataLoader) throws Exception
	{
		this.dataLoader = dataLoader;
		this.analyseResult = analyse(buildData);
		this.pluginName = pluginName;
	}

	/**
	 * is loading all installed dependencies (totalSet) of this build from
	 * database and analysis all of them
	 * 
	 * @param buildData
	 * @return {@link AnalyseResult}
	 * @throws SQLException
	 */
	private AnalyseResult analyse(BuildData buildData) throws Exception
	{
		AnalyseResult result = new AnalyseResult(buildData);
		Map<String, ComponentSummary> totalSet = loadDependencies(
			buildData.getBuildId(), 
			DependencyType.HIGH_LEVEL
		);

		// add information about this build
		ComponentSummary mainComponent = loadMainComponent(buildData.getBuildId());
		result.setMainComponent(mainComponent);
		result.setBuildData(buildData);

		// add analysis results about dependencies of this build
		for (ComponentSummary c : totalSet.values()) {
			BuildSummary build = loadBuildByReference(c.getReference());
			DependencyResult depResult = analyseDependency(build, c, totalSet, result);
			if (depResult.isExternal()) {
				depResult.setAnalyseTarget(c);
			}
			result.add(depResult);
		}
		return result;
	}

	/**
	 * loads all dependencies of installed dependencies (subSets).
	 * 
	 * @param build {@link BuildSummary}
	 * @param c {@link ComponentSummary}
	 * @param totalSet  Map<String, {@link ComponentSummary}>
	 * @param result {@link AnalyseResult}
	 * @return {@link DependencyResult}
	 * @throws Exception
	 * @throws LoadingFailedException
	 */
	private DependencyResult analyseDependency(
			BuildSummary build, 
			ComponentSummary c,
			Map<String, ComponentSummary> totalSet, 
			AnalyseResult result
	) throws Exception 
	{
		DependencyResult depResult = new DependencyResult();
		if (build != null) {
			c.setTimestamp(build.getTime_stamp());
			depResult.setRelatedBuild(build);
			depResult.setLinked(true);
			
			String url = build.getJobUrl() + "/"
					+ Integer.toString(build.getBuildNumber()) + "/" + SUB_URL;
			depResult.setLink(url);
			depResult.setAnalyseTarget(c);
			Map<String, ComponentSummary> subSet = loadDependencies(build.getBuildId(), DependencyType.HIGH_LEVEL);
			if (subSet != null) {
				compareReferences(totalSet, subSet, depResult, result);
			}
		} else {
			depResult.setExternal(true);
		}
		return depResult;
	}

	/**
	 * compares two references of the same component. If references are
	 * different: a Disparity is added to the {@link DependencyResult} and the
	 * amount of warnings of the {@link AnalyseResult} is increased by 1 by
	 * every {@link DependencyResult} that has Disparities
	 * 
	 * @param totalSet Map<String, {@link ComponentSummary}>
	 * @param subSet  Map<String, {@link ComponentSummary}>
	 * @param depResult {@link DependencyResult}
	 * @param result {@link AnalyseResult}
	 */
	private void compareReferences(
		Map<String, ComponentSummary> totalSet, 
		Map<String, ComponentSummary> subSet,
		DependencyResult depResult, 
		AnalyseResult result
	) {
		for (ComponentSummary latestVersionTested : subSet.values()) {
			if (!totalSet.containsKey(latestVersionTested.getReference())) {
				ComponentSummary versionInstalled = getComponentSummaryByName(totalSet.values(),
						latestVersionTested.getComponentName());
				Disparity disparity = new Disparity(latestVersionTested, versionInstalled);
				depResult.addDisparity(disparity);
			}
		}
		if (depResult.getDisparities() != null) result.increaseWarningCount();
	};

	/**
	 * is loading a main component of a build
	 * 
	 * @param buildId
	 * @return {@link ComponentSummary}
	 * @throws SQLException
	 * @throws LoadingFailedException
	 */
	private ComponentSummary loadMainComponent(String buildId) throws Exception
	{
		Transferable t = dataLoader.loadMainComponent(buildId);
		ComponentSummary c = (ComponentSummary) t.getObject(ComponentSummary.class);
		return c;
	}

	/**
	 * is loading a build by reference
	 * 
	 * @param reference
	 * @return {@link BuildSummary}
	 * @throws LoadingFailedException
	 */
	private BuildSummary loadBuildByReference(String reference) throws Exception
	{
		Transferable t = dataLoader.loadBuild(reference);
		return (BuildSummary) t.getObject(BuildSummary.class);
		
	}

	/**
	 * is searching for a component by its name
	 * 
	 * @param lst {@link Collection}<{@link ComponentSummary}>
	 * @param name component name
	 * @return {@link ComponentSummary}
	 */
	private ComponentSummary getComponentSummaryByName(Collection<ComponentSummary> lst, String name)
	{
		for (ComponentSummary c : lst) {
			if (c.getComponentName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * is loading direct oder all dependencies of a build according to its
	 * buildId
	 * 
	 * @param buildId  build identifier
	 * @param type {@link DependencyType}
	 * @return Map<String, {@link ComponentSummary}
	 * @throws LoadingFailedException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, ComponentSummary> loadDependencies(String buildId, DependencyType type) throws Exception
	{
		Transferable t =  dataLoader.loadDependencies(buildId, type);
		Map<String, ComponentSummary> map = (Map<String, ComponentSummary>) t.getMap(ComponentSummary.class);
		return map;
	}

	@Override
	public Object getTarget()
	{
		return null;
	}

	@Override
	public String getIconFileName()
	{
		return "plugin/" + pluginName + "/images/logo.png";
	}

	@Override
	public String getDisplayName()
	{
		return "Integration Analysis";
	}

	@Override
	public String getUrlName()
	{
		return "integration-analysis";
	}

	public AnalyseResult getAnalyse()
	{
		return analyseResult;
	}
}
