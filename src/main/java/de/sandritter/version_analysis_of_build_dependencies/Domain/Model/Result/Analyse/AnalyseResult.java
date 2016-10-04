package de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Analyse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import de.sandritter.version_analysis_of_build_dependencies.DependentComponentResolver;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.ComponentSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;

/**
 * AnalyseResult.java 
 * This class summarizes the version analysis of integration
 * dependencies. The {@link AnalyseResult} object holds a list of
 * {@link DependencyResult} objects, the main component, which was analyzed and
 * a {@link BuildData} object, that holds build specific information
 *
 * @author Michael Sandritter
 */
public class AnalyseResult implements Serializable {

	private static final long serialVersionUID = -477899930704734201L;

	/**
	 * main component that has been analyzed {@link ComponentSummary}
	 */
	private ComponentSummary mainComponent = null;

	/**
	 * amount of {@link DependencyResult} with {@link Disparity}
	 */
	private double amountOfDependencyResultsWithDisparity = 0;
	
	private List<DependencyResult> depResults;

	private List<DependencyResult> externalDeps;
	
	private List<DependencyResult> internalDeps;

	/**
	 * build specific information {@link BuildData}
	 */
	private BuildData buildData;

	public AnalyseResult(BuildData buildData)
	{
		setBuildData(buildData);
		this.depResults = new ArrayList<DependencyResult>();
		this.externalDeps = new ArrayList<DependencyResult>();
		this.internalDeps = new ArrayList<DependencyResult>();
	}

	public ComponentSummary getMainComponent()
	{
		return mainComponent;
	}

	public void increaseWarningCount()
	{
		amountOfDependencyResultsWithDisparity++;
	}

	public void setMainComponent(ComponentSummary mainComponent)
	{
		this.mainComponent = mainComponent;
	}

	public String getResolverUrl()
	{
		String url = buildData.getJobUrl() + "/"
				+ Integer.toString(buildData.getNumber()) + "/" + DependentComponentResolver.SUB_URL;
		return url;
	}

	/**
	 * get percentage of installed components with latest tested version
	 * 
	 * @return percentage ok
	 */
	public String getPercentageOk()
	{
		if (depResults.size() == 0) {
			return "0";
		}
		double percentage = (depResults.size() - amountOfDependencyResultsWithDisparity) * 100 / depResults.size();
		return Double.toString(round(percentage, 2));
	}

	/**
	 * get percentage of installed components, which are not tested in latest
	 * version
	 * 
	 * @return percentage with warning
	 */
	public String getPercentageWarnings()
	{
		if (depResults.size() == 0) {
			return "0";
		}
		double percentage = amountOfDependencyResultsWithDisparity * 100 / depResults.size();
		return Double.toString(round(percentage, 2));
	}

	public boolean hasDependencies()
	{
		if (depResults.size() > 0)
			return true;
		return false;
	}

	/**
	 * round decimal numbers up to two places behind the comma
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	private double round(double value, int places)
	{
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public BuildData getBuildData()
	{
		return buildData;
	}

	public void setBuildData(BuildData buildData)
	{
		this.buildData = buildData;
	}
	
	/**
	 * adds DependencyResult to dependencyResultList
	 * DependencyResult will also be added to the following list:
	 * if DepedencyResult includes external dependency -> external dependency list
	 * if DepedencyResult includes internal dependency -> internal dependency list
	 * @param depResult
	 */
	public void add(DependencyResult depResult)
	{
		if (depResult.isExternal()){
			externalDeps.add(depResult);
		} else {
			internalDeps.add(depResult);
		}
		depResults.add(depResult);
	}
	
	public List<DependencyResult> getExternalDependencies()
	{
		return externalDeps;
	}
	
	public List<DependencyResult> getInternalDependencies()
	{
		return internalDeps;
	}
	
	public List<DependencyResult> getDepResults()
	{
		return depResults;
	}
}
