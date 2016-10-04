package de.sandritter.version_analysis_of_build_dependencies;

import org.junit.Test;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

import static org.mockito.Mockito.*;

import java.io.IOException;

public class BuildDependencyPublisherTest {


	
	@Test
	public void shouldPerform() throws IOException
	{
		AbstractBuild<?, ?> build = mock(AbstractBuild.class);
		Launcher launcher = mock(Launcher.class);
		BuildListener listener = mock(BuildListener.class);
		BuildDependencyPublisher publisher = mock(BuildDependencyPublisher.class);
		publisher.perform(build, launcher, listener);
	}

}
