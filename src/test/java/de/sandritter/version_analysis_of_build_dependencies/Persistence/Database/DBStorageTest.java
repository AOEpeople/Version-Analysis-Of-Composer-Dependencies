package de.sandritter.version_analysis_of_build_dependencies.Persistence.Database;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Interface.DataStorage;
import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Module.PersistenceModule;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.DependencyReflection.DependencyReflectionCollection;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.DependencyReflection.JsonDataImage;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildDataBuilder;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Transport;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Mapper.DaoMapper;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Mapper.DependencyReflectionMapper;
import de.sandritter.version_analysis_of_build_dependencies.Persistence.Database.Factory.DataStorageFactory;
import de.sandritter.version_analysis_of_build_dependencies.Util.TestDataProvider;

public class DBStorageTest {

	private DataStorage dataStorage;
	private TestDataProvider dataProvider;

	@Before
	public void setUp() {

		// get path of database
		ClassLoader loader = getClass().getClassLoader();
		String dbPath = loader.getResource("jevi.db").getPath();

		Injector injector = Guice.createInjector(new PersistenceModule());
		DataStorageFactory dataStorageFactory = injector.getInstance(DataStorageFactory.class);
		this.dataStorage = dataStorageFactory.create(dbPath);
		
		this.dataProvider = new TestDataProvider();
	}
	
	@Test (expected=Exception.class) 
	public void shouldThrowExceptionIfConnectionCanNotBeEstablished() throws Exception
	{
		String dbPath = "/var/test/invalid";

		Injector injector = Guice.createInjector(new PersistenceModule());
		DataStorageFactory dataStorageFactory = injector.getInstance(DataStorageFactory.class);
		this.dataStorage = dataStorageFactory.create(dbPath);
		
		Transferable t = dataProvider.getStorageData("b01", 1, 4);
		dataStorage.storeData(t);
	}

	@Test
	public void testStorage() throws Exception
	{
		Transferable t = dataProvider.getStorageData("b01", 1, 4);
		dataStorage.storeData(t);
	}
	
	@Test
	public void shouldStoreData() throws Exception
	{
		Transferable transport = new Transport();
		DaoMapper mapper = new DaoMapper(transport);
		ClassLoader classLoader = getClass().getClassLoader();
		File composerJson = new File(classLoader.getResource("composer.json").getFile());
		File composerLockExtended = new File(classLoader.getResource("composer/composer.lock").getFile());
		
		DependencyReflectionMapper reflectMapper = new DependencyReflectionMapper();
		
		DependencyReflectionCollection col = (DependencyReflectionCollection) reflectMapper.mapData(composerLockExtended, DependencyReflectionCollection.class);
		JsonDataImage json = (JsonDataImage) reflectMapper.mapData(composerJson, JsonDataImage.class);
		
		Transferable transport2 = new Transport();
		transport2.setObject(DependencyReflectionCollection.class, col);
		transport2.setObject(JsonDataImage.class, json);
		
		BuildDataBuilder buildDataBuilder = new BuildDataBuilder();
		BuildData buildData = buildDataBuilder.getMock(1);
		Transferable t = mapper.mapData(buildData, transport2);
		
		dataStorage.storeData(t);
	}
	
	@Test (expected=Exception.class) 
	public void testStorageThrowsException() throws Exception
	{
		Transferable t = dataProvider.getStorageData(null, 1, 4);
		dataStorage.storeData(t);
	}
}
