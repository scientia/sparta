package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import play.data.validation.Required;

import models.AuthoredTemporalModel;
import models.File;

/**
 * would like to have an abstract class insted of populating 
 * relation for every other class.
 * 
 *
 */
@Entity
public class ItemFile extends AuthoredTemporalModel{

	@Required
	@Column(updatable=false)
	public Long entityid;
	
	@Required
	@Column(updatable=false)
	public String entityClass;
	
	
	@ManyToOne(optional=false)
	public File file;
	
	public ItemFile(Long entityid, String entityClass, File file) {
		super();
		this.entityid = entityid;
		this.entityClass = entityClass;
		this.file = file;
	}

}
