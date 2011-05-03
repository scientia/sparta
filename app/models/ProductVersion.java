package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Valid;

@MappedSuperclass
public class ProductVersion extends VersionModel{

	@Required
	@MaxSize(100)
	public String name;
	
	@Transient
	public String displayName;
	
	@Lob
	// @Required
	@MaxSize(10000)
	public String description;
	
	public Boolean frozen = false;

	@ManyToOne
	@Valid public Project project;
	
	public int    workflowstate;
	
	public String getDisplayName(){
		return name;
	}
}
