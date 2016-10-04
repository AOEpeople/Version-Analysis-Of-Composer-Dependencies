package de.sandritter.version_analysis_of_build_dependencies;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.sandritter.version_analysis_of_build_dependencies.IntegrationAnalyser;
import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Interface.DataLoader;
import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Module.PersistenceModule;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Analyse.AnalyseResult;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Analyse.DependencyResult;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Analyse.Disparity;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildDataBuilder;
import de.sandritter.version_analysis_of_build_dependencies.Persistence.Database.Factory.DataLoaderFactory;
import hudson.model.AbstractBuild;

public class IntegrationAnalyserTest {

	private BuildDataBuilder buildDataBuilder;
	private DataLoader dataLoader;
	private BuildData buildData;
	private IntegrationAnalyser analyzer;
	private AbstractBuild<?, ?> build;
	
	@Before
	public void setUp() throws Exception
	{
		buildDataBuilder = new BuildDataBuilder();
		this.buildData = buildDataBuilder.getLightMock();

		ClassLoader loader = getClass().getClassLoader();
		String dbPath = loader.getResource("jevi.db").getPath();
		
		this.build = mock(AbstractBuild.class);
		
		Injector injector = Guice.createInjector(new PersistenceModule());
		DataLoaderFactory dataLoaderFactory = injector.getInstance(DataLoaderFactory.class);
		this.dataLoader = dataLoaderFactory.create(dbPath);
		this.analyzer = new IntegrationAnalyser(build, "BuildDependency", buildData, dataLoader);
	}

	@Test
	public void testIntegrationAnalysis() throws Exception
	{
		AnalyseResult result = analyzer.getAnalyse();
		assertEquals(5, result.getDepResults().size());
		for (DependencyResult depResult : result.getDepResults()) {
			if (depResult.getAnalyseTarget().getComponentName().equals("Checkout")) {
				assertEquals(3, depResult.getDisparities().size());
				assertEquals("WARNING", depResult.getStatus());
			} else if (depResult.getAnalyseTarget().getComponentName().equals("Kunde")) {
				assertEquals("OK", depResult.getStatus());
				assertNull(depResult.getDisparities());
			} else if (depResult.getAnalyseTarget().getComponentName().equals("Produkt-Katalog")) {
				assertEquals("OK", depResult.getStatus());
				assertNull(depResult.getDisparities());
			} else if (depResult.getAnalyseTarget().getComponentName().equals("Billing")) {
				assertEquals("WARNING", depResult.getStatus());
				assertEquals(3, depResult.getDisparities().size());
			} else if (depResult.getAnalyseTarget().getComponentName().equals("Warenkorb")) {
				assertEquals("WARNING", depResult.getStatus());
				assertEquals(2, depResult.getDisparities().size());
				for (Disparity d : depResult.getDisparities()) {
					if (d.getTestedVersion().getComponentName().equals("Kunde")) {
						assertEquals("1.0", d.getTestedVersion().getVersion());
						assertEquals("1.1", d.getInstalledVersion().getVersion());
					}
				}
			} else {
				fail();
			}
		}
	}

	@Test
	public void testIntegrationAnalysisFailure() throws Exception
	{
		@SuppressWarnings("unused")
		IntegrationAnalyser analyzer = new IntegrationAnalyser(
				build, 
				"TestPlugin", 
				buildDataBuilder.getDefectMock(), 
				dataLoader
		);
	}
	
	@Test
	public void shouldGetIconFileName() throws Exception
	{
		assertEquals("plugin/BuildDependency/images/logo.png", analyzer.getIconFileName());
	}
	
	@Test
	public void shouldGetDisplayName()
	{
		assertEquals("Integration Analysis", analyzer.getDisplayName());
	}
	
	@Test
	public void shouldGetUrlName()
	{
		assertEquals("integration-analysis", analyzer.getUrlName());
	}
	
	@Test
	public void shouldGetTarget()
	{
		assertNull(analyzer.getTarget());
	}
}
