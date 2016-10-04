package de.sandritter.version_analysis_of_build_dependencies.Domain.Model.DAO;

import java.io.Serializable;

/**
 * Component.java
 * the component class is data-access-object
 * that holds component specific information
 *
 * @author Michael Sandritter
 */
public class Component implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8311181194341721309L;
	
	/**
	 * component name
	 */
	private String name;
	
	/**
	 * repository type - Git/SVN
	 */
	private String sourceType;
	
	/**
	 * repository url
	 */
	private String sourceUrl;
	
	/**
	 * constructor
	 * @param name - component name
	 * @param sourceType - version control source type
	 * @param sourceUrl - version control source url
	 */
	public Component(String name, String sourceType, String sourceUrl){
		this.setName(name);
		this.setSourceType(sourceType);
		this.setSourceUrl(sourceUrl);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSourceType() {
		return sourceType;
	}
	
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	public String getSourceUrl() {
		return sourceUrl;
	}
	
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
}
