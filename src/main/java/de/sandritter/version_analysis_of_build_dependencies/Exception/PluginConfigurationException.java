package de.sandritter.version_analysis_of_build_dependencies.Exception;

public class PluginConfigurationException extends Exception {
	
	/**
	 * @param message
	 * @param e {@link Exception}
	 */
	public PluginConfigurationException(String message, Exception e)
	{
		super(message, e);
	}

	private static final long serialVersionUID = 2269216344245777941L;

}
