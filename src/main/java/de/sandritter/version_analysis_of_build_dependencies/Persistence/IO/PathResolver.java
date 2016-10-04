package de.sandritter.version_analysis_of_build_dependencies.Persistence.IO;

import java.io.File;

import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.FileType;

/**
 * PathResolver.java
 * resolves the absolute paths of files by given file and build
 * 
 * @author Michael Sandritter
 */
public class PathResolver implements Resolver {
	
	/**
	 */
	public PathResolver(){}

	/**
	 * 
	 * @param fileType
	 * @param relativePath
	 * @return
	 */
	public String resolveAbsolutePath(FileType fileType, String absolutePath, String relativePath)
	{
		for (FileType type : FileType.values()) {
			if (type == fileType && relativePath.equals("default")) {
				return getAbsolute("source/" + fileType.toString(), absolutePath);
			}
		}
		return getAbsolute(relativePath, absolutePath);
	}

	/**
	 * concatenates a file name and directory and returns a absolute path of
	 * that file
	 * 
	 * @param relative relative path
	 * @param workspace workspace path
	 * @return absolute path
	 */
	private String getAbsolute(String relative, String workspace)
	{
		File f = new File(relative);
		File workspacePath = new File(workspace);
		File absolutePath = new File(workspacePath, relative);
		if (f.isFile() && f.isAbsolute()) {
			return f.getAbsolutePath();
		}
		return absolutePath.getAbsolutePath();
	}

}
