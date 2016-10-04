package de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Analyse;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.BuildSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.ComponentSummary;

public class DependencyResultTest {
	
	private DependencyResult dependencyResult;

	@Before
	public void setUp()
	{
		ComponentSummary cs = new ComponentSummary();
		this.dependencyResult = new DependencyResult(cs);
		dependencyResult.setLink("https://link");
		dependencyResult.setStatus("WARNING");
	}
	
	@Test
	public void shouldNotBeLinked()
	{
		assertFalse(dependencyResult.isLinked());
	}
	
	@Test
	public void shouldGetLink()
	{
		assertEquals("https://link", dependencyResult.getLink());
	}

	@Test
	public void shouldSetStatus()
	{
		assertEquals("WARNING", dependencyResult.getStatus());
	}
	
	@Test
	public void shouldNotHaveWarnings()
	{
		assertFalse(dependencyResult.hasWarnings());
	}
	
	@Test
	public void shouldHaveWarnings()
	{
		Disparity d = new Disparity(null, null);
		dependencyResult.addDisparity(d);
		assertTrue(dependencyResult.hasWarnings());
	}
	
	@Test
	public void shouldGetRelatedBuild()
	{
		BuildSummary b = new BuildSummary();
		b.setBuildId("id");
		dependencyResult.setRelatedBuild(b);
		assertEquals("id", dependencyResult.getRelatedBuild().getBuildId());
	}
}
