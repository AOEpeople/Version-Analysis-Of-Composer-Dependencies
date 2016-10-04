package de.sandritter.version_analysis_of_build_dependencies.Mapping.Facade;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Mapping.Module.MappingModule;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.DAO.Build;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.DAO.Component;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.DAO.Dependency;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.DAO.Stand;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildDataBuilder;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.FileType;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Exception.DataMappingFailedException;

public class MappingFacadeTest {
	
	private MappingFacade mappingFacade;

	@SuppressWarnings("unchecked")
	@Test
	public void shouldMapRowData() throws DataMappingFailedException
	{
		BuildDataBuilder dataBuilder = new BuildDataBuilder();
		BuildData buildData = dataBuilder.getMock(1);
		
		ClassLoader classLoader = getClass().getClassLoader();
		File composerJson = new File(classLoader.getResource("composer.json").getFile());
		File composerLock = new File(classLoader.getResource("composer.lock").getFile());
		
		Map<FileType, File> files = new HashMap<FileType, File>();
		files.put(FileType.COMPOSER_JSON, composerJson);
		files.put(FileType.COMPOSER_LOCK, composerLock);
		
		Injector injector = Guice.createInjector(new MappingModule());
		this.mappingFacade = injector.getInstance(MappingFacade.class);
	
		// execute
		Transferable transport = mappingFacade.mapRowData(buildData, files);
		
		Build build = (Build) transport.getObject(Build.class);
		assertEquals("buildId1", build.getBuildId());
		
		List<Component> componentList = (List<Component>) transport.getList(Component.class);
		assertEquals(23, componentList.size());
		
		List<Dependency> dependencyList = (List<Dependency>) transport.getList(Dependency.class);
		assertEquals(23, dependencyList.size());
		
		List<Stand> standList = (List<Stand>) transport.getList(Stand.class);
		assertEquals(23, standList.size());
	}
}
