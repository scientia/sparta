package models;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;


/**
 * This need to be revisited again.
 * Every version model needs an identifier to be specified.
 * The type of identifier sould
 * @author snehal
 *
 */
@MappedSuperclass
public abstract class VersionModel extends AuthoredTemporalModel{

	
	public Integer iteration = 1;
	
	public Integer revision = 0;
	
	public Boolean clone = false;
	
    @ManyToOne
	public User revisedBy;
	
    @ManyToOne
	public User clonedBy;
}
