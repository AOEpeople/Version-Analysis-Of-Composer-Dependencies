package de.sandritter.version_analysis_of_build_dependencies.Persistence.IO;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.FileType;
import de.sandritter.version_analysis_of_build_dependencies.Persistence.IO.PathResolver;

public class PathResolverTest {
	
	private PathResolver pathResolver;
	private String testWorkspacePath;
	
	@Before
	public void setUp() throws Exception
	{
		String absoluteFilePath = getClass().getClassLoader().getResource("composer.json").getPath();
		File f = new File(absoluteFilePath);
		this.testWorkspacePath = f.getParent();
		this.pathResolver = new PathResolver();
	}
	
	@Test
	public void shouldGetAbsolutePathOfAComposerJsonFile()
	{
		String relativePath = "/test/composer.json";
		String expected = testWorkspacePath + relativePath;
		String path = pathResolver.resolveAbsolutePath(FileType.COMPOSER_JSON, testWorkspacePath, relativePath);
		assertEquals(expected, path);
	}
	
	@Test
	public void shouldGetAbsolutePathOfAFileByUsingAbsolutePath()
	{
		String absolutePath = getClass().getClassLoader().getResource("composer.json").getPath();
		String path = pathResolver.resolveAbsolutePath(FileType.COMPOSER_JSON, testWorkspacePath, absolutePath);
		assertEquals(absolutePath, path);
	}
	
	@Test
	public void shouldGetAbsolutePathOfAComposerLockFile()
	{
		String relativePath = "/test/composer.lock";
		String expected = testWorkspacePath + relativePath;
		String path = pathResolver.resolveAbsolutePath(FileType.COMPOSER_LOCK, testWorkspacePath, relativePath);
		assertEquals(expected, path);
	}
	
	@Test
	public void shouldGetAbsolutePathOfAFileWithDefaultConfiguration()
	{
		String relativePath = "default";
		String expected = testWorkspacePath + "/source/composer.lock";
		String path = pathResolver.resolveAbsolutePath(FileType.COMPOSER_LOCK, testWorkspacePath, relativePath);
		assertEquals(expected, path);
	}

}
