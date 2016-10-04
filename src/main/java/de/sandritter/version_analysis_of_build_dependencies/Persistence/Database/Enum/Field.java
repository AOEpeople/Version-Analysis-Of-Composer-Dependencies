package de.sandritter.version_analysis_of_build_dependencies.Persistence.Database.Enum;

/**
 * StorageFields.java
 * StorageFields defines the column names of the database tables
 *
 * @author Michael Sandritter
 */
public enum Field {
	BUILD_ID ("build_id"),
	BUILD_NUMBER ("build_number"),
    TIMESTAMP ("time_stamp"),
    JOB_NAME ("job_name"),
    CATEGORY ("category"),
    ANALYSE ("analyse"),
    JOB_URL ("job_url"),

    REFERENCE ("reference"),
    VERSION ("version"),
    DEP_TYPE ("type"),

    COMPONENT ("component"),
    COMPONENT_NAME ("component_name"),
	NAME ("name"),
	VC_URL ("source_url"),
	VC_TYPE ("source_type");
	
	
    private final String name;       

    private Field(String s) 
    {
        name = s;
    }

    @Override
	public String toString() 
    {
       return this.name;
    }
}
