package de.bathesis2015.msand.postBuildAction.jevidatacollector.Domain.Model.DAO;

/**
 * 
 * Dependency.java
 * the dependency class is data-access-object
 * that holds dependency specific information
 *
 * @author Michael Sandritter
 *
 */
public class Dependency {
	
	/**
	 * reference of a unique version of component
	 */
	private String reference;
	
	/**
	 * build identifier
	 */
	private String buildId;
	
	/**
	 * dependency type - main component / dependency
	 */
	private String type;
	
	/**
	 * constructor
	 * @param reference - reference of a unique stand of a component
	 * @param buildId - build identifier
	 * @param type - dependency type 
	 */
	public Dependency(String reference, String buildId, String type){
		this.reference = reference;
		this.buildId = buildId;
		this.setType(type);
	}
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String getBuildId() {
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
