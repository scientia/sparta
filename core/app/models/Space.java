package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.JPAPlugin;

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
	
	/**
	 * Users could either set a local filesystem to store files
	 * or can have a distributed file system such as HDFS.
	 * If mapped directory is used no need to specify uri vice versa.
	 */
	@MaxSize(150)
	@Column(unique=true)
	public String mappedDir;
	
	public boolean usingdfs = false;
	
	@MaxSize(150)
	@Column(unique=true)
	public String dfsUri;

	public List<Project> releasedSpaceForProjects(){
		return Project.find("byReleaseSpace", this).fetch();
	}
	
	public List<Project> wipSpaceForProjects(){
		return Project.find("byWipSpace", this).fetch();
	}
	
	public String toString(){
		return name;
	}

	public List<Project> getProjects() {
		return Project.find("wipSpace = ? or releaseSpace = ?", this, this).fetch();
	}
}
