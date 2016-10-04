package de.sandritter.version_analysis_of_build_dependencies.Persistence.Database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Interface.DataLoader;
import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Module.PersistenceModule;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.BuildSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.ComponentSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.DependentComponent;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.DependencyType;
import de.sandritter.version_analysis_of_build_dependencies.Persistence.Database.Factory.DataLoaderFactory;

public class DBLoaderTest {

	private DataLoader dataLoader;

	@Before
	public void setUp() throws IOException, InterruptedException
	{

		// get path of database placed in src/test/resources
		ClassLoader loader = getClass().getClassLoader();
		String dbPath = loader.getResource("jevi.db").getPath();

		Injector injector = Guice.createInjector(new PersistenceModule());
		DataLoaderFactory dataLoaderFactory = injector.getInstance(DataLoaderFactory.class);
		this.dataLoader = dataLoaderFactory.create(dbPath);
	}

	@Test
	public void shouldLoadBuild() throws Exception
	{
		Transferable t = dataLoader.loadBuild("ref-k3-01");
		BuildSummary build = (BuildSummary) t.getObject(BuildSummary.class);
		assertNotNull(build);
		assertEquals("b-k3-01", build.getBuildId());
		assertEquals((long) 144936, build.getTime_stamp());
		assertEquals(1, build.getBuildNumber());
		assertEquals("Job3", build.getJobName());
		assertEquals("https://jenkins-url/job-url/job-name/buildnumber3", build.getJobUrl());
	}
	
	@Test (expected=SQLException.class)
	public void shouldThrowExceptionWhileLoadingBuild() throws Exception
	{
		Injector injector = Guice.createInjector(new PersistenceModule());
		DataLoaderFactory dataLoaderFactory = injector.getInstance(DataLoaderFactory.class);
		this.dataLoader = dataLoaderFactory.create("invalidDBPath");
		@SuppressWarnings("unused")
		Transferable t = dataLoader.loadBuild("invalid");
	}
	
	@Test (expected=SQLException.class)
	public void shouldThrowExceptionWhileLoadingMainComponent() throws Exception
	{
		Injector injector = Guice.createInjector(new PersistenceModule());
		DataLoaderFactory dataLoaderFactory = injector.getInstance(DataLoaderFactory.class);
		this.dataLoader = dataLoaderFactory.create("invalidDBPath");
		@SuppressWarnings("unused")
		Transferable t = dataLoader.loadMainComponent("invalid");
	}
	
	@Test (expected=SQLException.class)
	public void shouldThrowExceptionWhileLoadingDependencies() throws Exception
	{
		Injector injector = Guice.createInjector(new PersistenceModule());
		DataLoaderFactory dataLoaderFactory = injector.getInstance(DataLoaderFactory.class);
		this.dataLoader = dataLoaderFactory.create("invalidDBPath");
		@SuppressWarnings("unused")
		Transferable t = dataLoader.loadDependencies("invalid", null);
	}
	
	@Test (expected=SQLException.class)
	public void shouldThrowExceptionWhileLoadingDependentComponents() throws Exception
	{
		Injector injector = Guice.createInjector(new PersistenceModule());
		DataLoaderFactory dataLoaderFactory = injector.getInstance(DataLoaderFactory.class);
		this.dataLoader = dataLoaderFactory.create("invalidDBPath");
		@SuppressWarnings("unused")
		Transferable t = dataLoader.loadDependentComponents("invalid");
	}
	
	@Test (expected=Exception.class)
	public void shouldThrowExceptionWhileEstablishingConnection() throws Exception
	{
		Injector injector = Guice.createInjector(new PersistenceModule());
		DataLoaderFactory dataLoaderFactory = injector.getInstance(DataLoaderFactory.class);
		this.dataLoader = dataLoaderFactory.create("invalidDBPath");
		@SuppressWarnings("unused")
		Transferable t = dataLoader.loadDependentComponents("invalid");
	}

	@Test
	public void shouldLoadMainComponent() throws Exception
	{
		Transferable t = dataLoader.loadMainComponent("b-k3-01");
		ComponentSummary component = (ComponentSummary) t.getObject(ComponentSummary.class);
		assertNotNull(component);
		assertEquals("b-k3-01", component.getBuildId());
		assertEquals(-1, component.getTimestamp());
		assertEquals("git", component.getSourceType());
		assertEquals("https://github.com/Sandritter/k3.git", component.getSourceUrl());
		assertEquals("Warenkorb", component.getComponentName());
		assertEquals("main", component.getDependencyType());
		assertEquals("ref-k3-01", component.getReference());
		assertEquals("3.0", component.getVersion());
	}

	@Test
	public void shouldLoadAllDependencies() throws Exception
	{
		Transferable t = dataLoader.loadDependencies("b-k3-01", DependencyType.ALL);
		@SuppressWarnings("unchecked")
		Map<String, ComponentSummary> totalSet = (Map<String, ComponentSummary>) t.getMap(ComponentSummary.class);
		assertEquals(3, totalSet.size());
	}

	@Test
	public void shouldLoadDependentComponents() throws Exception
	{
		Transferable t = dataLoader.loadDependentComponents("Kunde");
		@SuppressWarnings("unchecked")
		Map<String, DependentComponent> totalSet = (Map<String, DependentComponent>) t.getMap(DependentComponent.class);
		assertEquals(4, totalSet.size());
	}
	
	@Test
	public void shouldHaveSpecificPropertiesOfDependentComponent() throws Exception
	{
		Transferable t = dataLoader.loadDependentComponents("Kunde");
		@SuppressWarnings("unchecked")
		Map<String, DependentComponent> totalSet = (Map<String, DependentComponent>) t.getMap(DependentComponent.class);
		DependentComponent component = totalSet.get("Warenkorb");
		assertNotNull(component.getBuildId());
		assertNotNull(component.getBuildNumber());
		assertNotNull(component.getReference());
		assertNotNull(component.getVersion());
		assertEquals("Warenkorb", component.getComponentName());
		assertEquals("Job3", component.getJobName());
		assertEquals("https://jenkins-url/job-url/job-name/buildnumber3", component.getLink());
	}

	@Test
	public void shouldLoadHighLevelDependencies() throws Exception
	{
		Transferable t = dataLoader.loadDependencies("b-k3-01", DependencyType.HIGH_LEVEL);
		@SuppressWarnings("unchecked")
		Map<String, ComponentSummary> subSet = (Map<String, ComponentSummary>) t.getMap(ComponentSummary.class);
		assertEquals(2, subSet.size());
	}
}
