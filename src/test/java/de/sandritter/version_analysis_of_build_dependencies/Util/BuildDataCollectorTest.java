package de.sandritter.version_analysis_of_build_dependencies.Util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.BuildData;
import de.sandritter.version_analysis_of_build_dependencies.Util.BuildDataCollector;
import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

public class BuildDataCollectorTest {

	private BuildDataCollector buildDataCollector;
	
	@Before
	public void setUp() throws IOException, InterruptedException
	{
		BuildListener listener = mock(BuildListener.class);
		AbstractBuild<?, ?> build = mock(AbstractBuild.class);
		when(build.getId()).thenReturn("testJenkinsJob_build_1");
		when(build.getNumber()).thenReturn(1);
		//when(build.getTimeInMillis()).thenReturn(millis);
		
		EnvVars envVars = new EnvVars();
		envVars.addLine("JOB_NAME=TestJenkinsJob");
		envVars.addLine("JENKINS_URL=https://jenkins-url.de");
		envVars.addLine("JOB_URL=https://job-url.de");
		envVars.addLine("GIT_COMMIT=213748612368712a");
		envVars.addLine("GIT_URL=https://git-url.de");
		envVars.addLine("GIT_TAG_NAME=v3.1");
		
		when(build.getEnvironment(listener)).thenReturn(envVars);
		this.buildDataCollector = new BuildDataCollector(build, listener);
	}
	
	@Test
	public void shouldCollectBuildData()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertThat(buildData, instanceOf(BuildData.class));
	}
	
	@Test
	public void shouldHaveJobName()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertEquals("TestJenkinsJob", buildData.getJobName());
	}

	@Test
	public void shouldHaveJenkinsUrl()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertEquals("https://jenkins-url.de", buildData.getJenkinsUrl());
	}
	
	@Test
	public void shouldHaveJobUrl()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertEquals("https://job-url.de", buildData.getJobUrl());
	}
	
	@Test
	public void shouldHaveGitRevision()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertEquals("213748612368712a", buildData.getRevision());
	}
	
	@Test
	public void shouldHaveGitUrl()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertEquals("https://git-url.de", buildData.getSourceUrl());
	}
	
	@Test
	public void shouldHaveGitTagName()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertEquals("v3.1", buildData.getVersion());
	}
	
	@Test
	public void shouldHaveDbPath()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		buildData.setDbPath("dbPath");
		assertEquals("dbPath", buildData.getDbPath());
	}
	
	@Test
	public void shouldHaveTimestamp()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		buildData.setTimestamp(23179823);
		assertEquals(23179823, buildData.getTimestamp());
	}
	
	@Test
	public void shouldHaveFormatted()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		buildData.setTimestamp(-1);
		assertEquals("no date available, timestamp value might be unset", buildData.getFormattedTime());
	}
	
	@Test
	public void shouldHaveNumber()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertEquals(1, buildData.getNumber());
	}
	
	@Test
	public void shouldHaveBuildId()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertEquals("testJenkinsJob_build_1TestJenkinsJob", buildData.getBuildId());
	}
	
	@Test
	public void shouldHaveSourceType()
	{
		BuildData buildData = buildDataCollector.collectBuildData();
		assertEquals("git", buildData.getSourceType());
	}
	
	@Test
	public void shouldNotInjectGitInfoWhenGitRevisionIsNull() throws IOException, InterruptedException
	{
		
		BuildListener listener = mock(BuildListener.class);
		AbstractBuild<?, ?> build = mock(AbstractBuild.class);
		when(build.getId()).thenReturn("testJenkinsJob_build_1");
		when(build.getNumber()).thenReturn(1);
		
		EnvVars envVars = new EnvVars();
		envVars.addLine("JOB_NAME=TestJenkinsJob");
		envVars.addLine("JENKINS_URL=https://jenkins-url.de");
		envVars.addLine("JOB_URL=https://job-url.de");
		envVars.addLine("GIT_URL=https://git-url.de");
		envVars.addLine("GIT_TAG_NAME=v3.1");
		
		when(build.getEnvironment(listener)).thenReturn(envVars);
		this.buildDataCollector = new BuildDataCollector(build, listener);
		
		BuildData buildData = buildDataCollector.collectBuildData();
		
		assertNull(buildData.getVersion());
		assertNull(buildData.getRevision());
		assertNull(buildData.getSourceUrl());
	}
	
	@Test
	public void shouldInjectSvnInfo() throws IOException, InterruptedException
	{
		
		BuildListener listener = mock(BuildListener.class);
		AbstractBuild<?, ?> build = mock(AbstractBuild.class);
		when(build.getId()).thenReturn("testJenkinsJob_build_1");
		when(build.getNumber()).thenReturn(1);
		
		EnvVars envVars = new EnvVars();
		envVars.addLine("JOB_NAME=TestJenkinsJob");
		envVars.addLine("JENKINS_URL=https://jenkins-url.de");
		envVars.addLine("JOB_URL=https://job-url.de");
		envVars.addLine("SVN_REVISION=v3.1");
		envVars.addLine("SVN_URL=https://svn-url.de");
		
		when(build.getEnvironment(listener)).thenReturn(envVars);
		this.buildDataCollector = new BuildDataCollector(build, listener);
		
		BuildData buildData = buildDataCollector.collectBuildData();
		
		assertEquals("https://svn-url.de", buildData.getSourceUrl());
		assertEquals("v3.1", buildData.getRevision());
	}
	
	@Test
	public void shouldCatchIOException() throws IOException, InterruptedException
	{
		BuildListener listener = mock(BuildListener.class);
		AbstractBuild<?, ?> build = mock(AbstractBuild.class);
		when(build.getId()).thenReturn("testJenkinsJob_build_1");
		when(build.getNumber()).thenReturn(1);
		
		when(build.getEnvironment(listener)).thenThrow(new IOException());
		this.buildDataCollector = new BuildDataCollector(build, listener);
		
		BuildData buildData = buildDataCollector.collectBuildData();
		
		assertThat(buildData, instanceOf(BuildData.class));
	}
	
	@Test
	public void shouldCatchInterruptedException() throws IOException, InterruptedException
	{
		BuildListener listener = mock(BuildListener.class);
		AbstractBuild<?, ?> build = mock(AbstractBuild.class);
		when(build.getId()).thenReturn("testJenkinsJob_build_1");
		when(build.getNumber()).thenReturn(1);
		
		when(build.getEnvironment(listener)).thenThrow(new InterruptedException());
		this.buildDataCollector = new BuildDataCollector(build, listener);
		
		BuildData buildData = buildDataCollector.collectBuildData();
		
		assertThat(buildData, instanceOf(BuildData.class));
	}
	
	@Test
	public void shouldCatchException() throws IOException, InterruptedException
	{
		BuildListener listener = mock(BuildListener.class);
		AbstractBuild<?, ?> build = mock(AbstractBuild.class);
		when(build.getId()).thenReturn("testJenkinsJob_build_1");
		when(build.getNumber()).thenReturn(1);
		
		EnvVars envVars = mock(EnvVars.class);
		when(envVars.get("JOB_NAME")).thenThrow(new RuntimeException());
		
		when(build.getEnvironment(listener)).thenReturn(envVars);
		this.buildDataCollector = new BuildDataCollector(build, listener);
		
		BuildData buildData = buildDataCollector.collectBuildData();
		
		assertThat(buildData, instanceOf(BuildData.class));
	}
}
