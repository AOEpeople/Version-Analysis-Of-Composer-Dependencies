package de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Mapping.Interface;

import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Exception.DataMappingFailedException;

/**
 * RowDataMapper.java
 * This class is mapping all extracted build- and version-information 
 * to data access objects
 *
 * @author Michael Sandritter
 */
public interface RowDataMapper {
	
	/**
	 * maps row data to data access objects
	 * 
	 * @param buildData {@link BuildData}
	 * @param transport {@link Transferable}
	 * @return Transferable {@link Transferable}
	 * @throws DataMappingFailedException in case mapping failed
	 */
	public Transferable mapData(BuildData buildData, Transferable transport) throws DataMappingFailedException;
}
