package de.sandritter.version_analysis_of_build_dependencies.Domain.Model.DAO;

/**
 * Stand.java
 * the stand class is a data-access-object
 * that holds stand specific information
 *
 * @author Michael Sandritter
 */
public class Stand {
	
	/**
	 * component version
	 */
	private String version;
	
	/**
	 * reference of a unique version of component
	 */
	private String reference;
	
	/**
	 * component name
	 */
	private String componentName;
	
	/**
	 * 
	 * @param version  component version
	 * @param reference  reference of a unique stand of a component
	 * @param name  component name
	 */
	public Stand(String version, String reference, String name){
		this.setVersion(version);
		this.setReference(reference);
		this.setComponentName(name);
	}

	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
}
