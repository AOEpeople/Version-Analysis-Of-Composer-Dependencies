package de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Interface;

import java.sql.SQLException;

import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.DependencyType;

/**
 * DataLoader.java
 * interface that defines the methods to load data from database
 * 
 * @author Michael Sandritter
 */
public interface DataLoader {
	
	/**
	 * is loading dependency information from database
	 * @param value table identifier
	 * @param type {@link DependencyType}
	 * @return {@link Transferable}
	 * @throws Exception in case the database fetch of dependencies failed
	 */
	public Transferable loadDependencies(String value, DependencyType type) throws Exception;

	/**
	 * is loading build-specific information from database
	 * @param reference - vcs reference
	 * @return {@link Transferable}
	 * @throws Exception in case the database fetch of build information failed
	 */
	public Transferable loadBuild(String reference) throws Exception;

	/**
	 * is loading information about the main component of a build from database
	 * @param value table identifier
	 * @return {@link Transferable}
	 * @throws Exception in case the database fetch of the main component failed
	 */
	public Transferable loadMainComponent(String value) throws Exception;
	
	/**
	 * is loading information about dependent components of a component
	 * @param dependencyName dependency name
	 * @return {@link Transferable}
	 * @throws Exception in case the database fetch of dependent components failed
	 */
	public Transferable loadDependentComponents(String dependencyName) throws Exception;

}
