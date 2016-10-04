package de.sandritter.version_analysis_of_build_dependencies;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Interface.DataLoader;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.ComponentSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.DependentComponent;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Transport;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Persistence.Database.SQliteLoader;

public class DependentComponentResolverTest {
	
	private BuildData buildData;
	public final static String SUB_URL = "dependency-resolver";
	private DataLoader dataLoader;
	private DependentComponentResolver dependentComponentResolver;
	
	@Before
	public void setUp() throws Exception 
	{
		//mock data
		this.buildData = new BuildData();
		buildData.setNumber(1);
		buildData.setJobUrl("https://jenkins");
		buildData.setBuildId("id1");
		
		ComponentSummary componentSummary = new ComponentSummary();
		componentSummary.setComponentName("Warenkorb");
		
		DependentComponent dependentComponent = new DependentComponent();
		dependentComponent.setLink("");
		dependentComponent.setComponentName("Checkout");
		
		Map<String, DependentComponent> depMap = new HashMap<String, DependentComponent>();
		depMap.put("TEST", dependentComponent);
		
		Transferable t = new Transport();
		t.setObject(ComponentSummary.class, componentSummary);
		
		Transferable t2 = new Transport();
		t2.setMap(DependentComponent.class, depMap);
	
		this.dataLoader = mock(SQliteLoader.class);
		when(dataLoader.loadMainComponent(buildData.getBuildId())).thenReturn(t);
		when(dataLoader.loadDependentComponents("Warenkorb")).thenReturn(t2);
		
		this.dependentComponentResolver = new DependentComponentResolver("TestPlugin",dataLoader, buildData);
	}

	@Test
	public void shouldResolveDepedentComponents()
	{
		
	}
	
	@Test
	public void shouldGetAnalysisUrl()
	{
		assertEquals("https://jenkins/1/" + IntegrationAnalyser.SUB_URL, dependentComponentResolver.getAnalysisUrl());
	}
	
	@Test
	public void shouldGetDisplayName()
	{
		assertEquals("Dependency-Resolver", dependentComponentResolver.getDisplayName());
	}
	
	@Test
	public void shouldGetTarget()
	{
		assertNull(dependentComponentResolver.getTarget());
	}
	
	@Test
	public void shouldGetIconFileName()
	{
		assertEquals("plugin/TestPlugin/images/logo.png", dependentComponentResolver.getIconFileName());
		
	}
	
	@Test
	public void shouldGetSubUrl() throws Exception
	{
		assertEquals("dependency-resolver", dependentComponentResolver.getUrlName());
	}
	
	@Test
	public void shouldGetDependentComponents()
	{
		DependentComponent c = (DependentComponent) dependentComponentResolver.getDependentComponents().toArray()[0];
		assertEquals("Checkout", c.getComponentName());
	}
}
