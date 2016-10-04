package de.sandritter.version_analysis_of_build_dependencies;

import java.util.Collection;
import java.util.Map;

import org.kohsuke.stapler.StaplerProxy;

import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Interface.DataLoader;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.ComponentSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.DependentComponent;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Util.PropertyReader;
import hudson.model.Action;

/**
 * BuildDependencyPublisher.java
 * 
 * This object is implementing an {@link Action} so it can be one part of build step.
 * This action performs a search for all highlevel components which include
 * a specific lowlevel dependency. So whenever a lowlevel dependency changed,
 * all highlevel components can be informed or rebuild with the new version
 * of the lowlevel dependency
 * 
 * implements {@link Action} and {@link StaplerProxy}
 * 
 * @author Michael Sandritter
 */
public class DependentComponentResolver implements Action, StaplerProxy {

	private DataLoader dataLoader;
	private BuildData buildData;
	private Map<String, DependentComponent> map;
	public final static String SUB_URL = "dependency-resolver";
	private PropertyReader propertyReader;
	private String pluginName;

	public DependentComponentResolver(
			String pluginName, 
			DataLoader dataLoader, 
			BuildData buildData
	) throws Exception {
		this.dataLoader = dataLoader;
		this.buildData = buildData;
		this.propertyReader = PropertyReader.getInstance();
		this.pluginName = pluginName;
		
		resolveDependentComponents(buildData);
	}

	@SuppressWarnings("unchecked")
	private void resolveDependentComponents(BuildData buildData) throws Exception
	{
		String componentName = loadComponentNameOfMainComponent(buildData.getBuildId());
		Transferable t = dataLoader.loadDependentComponents(componentName);
		map = (Map<String, DependentComponent>) t.getMap(DependentComponent.class);
		String apiToken = propertyReader.getConfig("/config.properties").getProperty("api-token");
		for (DependentComponent c : map.values()) {
			String url =  c.getLink() + "build?token=" + apiToken;
			c.setLink(url);
		}
	}

	/**
	 * 
	 * @return url
	 */
	public String getAnalysisUrl()
	{
		String url = buildData.getJobUrl() + "/"
				+ Integer.toString(buildData.getNumber()) 
				+ "/" + IntegrationAnalyser.SUB_URL;
		return url;
	}

	/**
	 * 
	 * @param buildId
	 * @return 
	 * @throws Exception
	 */
	private String loadComponentNameOfMainComponent(String buildId) throws Exception
	{
		Transferable t = dataLoader.loadMainComponent(buildId);
		ComponentSummary c = (ComponentSummary) t.getObject(ComponentSummary.class);
		return c.getComponentName();
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
		return "Dependency-Resolver";
	}

	@Override
	public String getUrlName()
	{
		return SUB_URL;
	}

	public Collection<DependentComponent> getDependentComponents()
	{
		return map.values();
	}

}
