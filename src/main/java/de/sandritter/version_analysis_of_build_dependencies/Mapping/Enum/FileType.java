package de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum;

/**
 * JsonFileType.java
 * enum describing defining differnt file types
 *
 * @author Michael Sandritter
 */
public enum FileType {
	COMPOSER_LOCK ("composer.lock"), 
	COMPOSER_JSON ("composer.json");
	
	private final String name;       

    private FileType(String s) 
    {
        name = s;
    }

    @Override
	public String toString() 
    {
       return this.name;
    }
}
