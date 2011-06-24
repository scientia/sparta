package models;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.beanutils.PropertyUtils;

import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Valid;

@MappedSuperclass
public abstract class ProductVersion extends VersionModel {

	@Required
	@MaxSize(100)
	@Match("[a-zA-Z][a-zA-Z_0-9() \\.]*")
	public String name;

	@Transient
	public String displayName;

	@Lob
	// @Required
	@MaxSize(10000)
	public String description;

	public Boolean frozen = false;

	@Required
	@ManyToOne
	@Valid
	public Project project;

	public int workflowstate;

	protected ProductVersion _clone(ProductVersion to){		
		to = (ProductVersion)_copy(to);	
		to.revision = this.revision;
		to.revisedBy = this.revisedBy;
		to.clonedBy = User.find("byUsername", User.getConnected()).first();
		to.cloned = true;
		to.iteration = getNextIteration();		
		return to;
	}

	protected  ProductVersion _revise(ProductVersion to){		
		to = (ProductVersion)_copy(to);
		to.revisedBy = User.find("byUsername", User.getConnected()).first();
		to.revision = getNextRevision();
		to.cloned = false;
		to.clonedBy = null;
		to.iteration = 1;
		return to;
	}

	protected  ProductVersion _tab(ProductVersion to){		
		to = (ProductVersion)_copy(to);
		to.revisedBy =null;
		to.revision = 0;
		to.cloned = false;
		to.clonedBy = null;
		to.iteration = 1;
		return to;
	}
	public abstract ProductVersion clone();
	
	public abstract ProductVersion revise();
	
	public abstract ProductVersion tab();

	
}
