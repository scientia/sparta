package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Email;
import play.data.validation.Max;
import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"name"})
		})
public class Organization extends TemporalModel{

	@Required
	@MaxSize(100) @MinSize(5)
	public String name;

	@Lob
	@Required
	@MaxSize(1000)
	public String description;
    
	/**
	 * Participates in these projects. Primary executions responsibility 
	 * lies with the owning organization.
	 */
	@ManyToMany(cascade=CascadeType.PERSIST, mappedBy="organizations")
	public Set<Project> projects =  new HashSet<Project>();
	
    public Organization(String name, String description) {
    	this.name = name;
        this.description = description;
    }
    
   
    public String toString(){
    	return name;
    }
    
    public List<Project> getOwnedProjects(){
    	return Project.find("byOwnerOrganization", this).fetch();
    }
}
