package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;

/**
 * Spaces need to be cached.
 * @author snehal
 *
 */
//TODO: add to memcached
@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"name"})
		})
public class Space extends AdminModel {
	
	@MaxSize(150)
	@Column(unique=true)
	public String mappedDir;
	
	public List<Project> releasedSpaceForProjects(){
		return Project.find("byReleaseSpace", this).fetch();
	}
	
	public List<Project> wipSpaceForProjects(){
		return Project.find("byWipSpace", this).fetch();
	}
	
	public String toString(){
		return name;
	}
}
