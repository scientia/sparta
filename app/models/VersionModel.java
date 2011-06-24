package models;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;
import play.db.jpa.JPA;


/**
 * This need to be revisited again.
 * Every version model needs an identifier to be specified.
 * The type of identifier sould
 * @author snehal
 *
 */
@MappedSuperclass
public abstract class VersionModel extends ItemModel{

	
	public Integer iteration = 1;
	
	public Integer revision = 0;
	
	public Boolean cloned = false;
	
    @ManyToOne
	public User revisedBy;
	
    @ManyToOne
	public User clonedBy;
    
    public abstract  Integer getNextIteration();
    
    public abstract Integer getNextRevision();
    
}
