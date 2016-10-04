package de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Analyse;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.sandritter.version_analysis_of_build_dependencies.DependentComponentResolver;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;

public class AnalyseResultTest {

	private AnalyseResult analyseResult;
	
	@Before
	public void setUp() throws IOException, InterruptedException
	{
		BuildData buildData = new BuildData();
		buildData.setJobUrl("http://job.url");
		buildData.setNumber(1);
		this.analyseResult = new AnalyseResult(buildData);
	}
	
	@Test
	public void shouldAddExternalDependency()
	{
		DependencyResult depResult = new DependencyResult();
		depResult.setExternal(true);
		this.analyseResult.add(depResult);
		assertEquals(1, this.analyseResult.getExternalDependencies().size());
	}

	@Test
	public void shouldAddInternalDependency()
	{
		DependencyResult depResult = new DependencyResult();
		depResult.setExternal(false);
		this.analyseResult.add(depResult);
		assertEquals(1, this.analyseResult.getInternalDependencies().size());
	}
	
	@Test
	public void shouldGetDependencyResults()
	{
		DependencyResult extDepResult = new DependencyResult();
		extDepResult.setExternal(true);
		DependencyResult intDepResult = new DependencyResult();
		intDepResult.setExternal(false);
		
		this.analyseResult.add(extDepResult);
		this.analyseResult.add(intDepResult);
		
		assertEquals(2, this.analyseResult.getDepResults().size());
	}
	
	@Test
	public void shouldGetResoverUrl() 
	{
		assertEquals(
			"http://job.url/1/" + DependentComponentResolver.SUB_URL, 
			this.analyseResult.getResolverUrl()
		);
	}
	
	@Test
	public void shouldGetPercentageOk()
	{
		DependencyResult extDepResult = new DependencyResult();
		extDepResult.setExternal(true);
		DependencyResult intDepResult = new DependencyResult();
		intDepResult.setExternal(false);
		
		this.analyseResult.add(extDepResult);
		this.analyseResult.add(intDepResult);
		this.analyseResult.increaseWarningCount();
		
		assertEquals("50.0", this.analyseResult.getPercentageOk());
	}
	
	@Test
	public void shouldGetPercentageWarnings()
	{
		DependencyResult extDepResult = new DependencyResult();
		extDepResult.setExternal(true);
		DependencyResult intDepResult = new DependencyResult();
		intDepResult.setExternal(false);
		
		this.analyseResult.add(extDepResult);
		this.analyseResult.add(intDepResult);
		this.analyseResult.increaseWarningCount();
		
		assertEquals("50.0", this.analyseResult.getPercentageWarnings());
	}
	
	@Test
	public void shouldHaveDependencies()
	{
		DependencyResult depResult = new DependencyResult();
		this.analyseResult.add(depResult);
		assertTrue(this.analyseResult.hasDependencies());
	}
	
	@Test
	public void shouldNotHaveDependencies()
	{
		assertFalse(this.analyseResult.hasDependencies());
	}
}
