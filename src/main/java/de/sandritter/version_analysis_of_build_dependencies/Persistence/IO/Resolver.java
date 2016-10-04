package de.sandritter.version_analysis_of_build_dependencies.Persistence.IO;

import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.FileType;

/**
 * interface that defines methods which create absolute paths of given files
 * 
 * @author Michael Sandritter
 */
public interface Resolver {

	/**
	 * takes an absolute and relative path and puts them together
	 * @param fileType {@link FileType}
	 * @param absolutePath 
	 * @param relativePath
	 * @return String absolute path
	 */
	public String resolveAbsolutePath(FileType fileType, String absolutePath, String relativePath);
}
