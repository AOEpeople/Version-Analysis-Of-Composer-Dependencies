package de.sandritter.version_analysis_of_build_dependencies.Util;

import java.util.Properties;

import de.sandritter.version_analysis_of_build_dependencies.Exception.PluginConfigurationException;

/**
 * 
 * @author Michael Sandritter
 *
 */
public class PropertyReader {
	
	private static PropertyReader reader; 
	private static Logger logger;
    
    private PropertyReader() {} 
    
    /**
     * @return
     */
    public static PropertyReader getInstance() { 
    	if (reader == null) {
    		PropertyReader.reader = new PropertyReader();
    		logger = Logger.getInstance();
    	}
      return PropertyReader.reader; 
    } 
    
    /**
     * @param configPath
     * @return plugin config
     */
    public Properties getConfig(String configPath){
		Properties config = new Properties();
		try {
		    config.load(getClass().getResourceAsStream(configPath)); 
		} catch (Exception ex) {
			logger.logFailure(
				new PluginConfigurationException("could not load configuration from " + configPath, ex), 
				"Please check that your config file is right in place ;)"
			);
		}
		
    	return config;
    }
}
