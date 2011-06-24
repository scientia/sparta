package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;

/**
 * Organization participates in projects
 * projects have staff from users belonging to an organization
 * users have roles in projects where their organization either participates or
 * owns and they are member of the staff on that project.
 * 
 */
@Entity
public class UserRole extends AuthoredTemporalModel{

	@Required
	@ManyToOne
	public User user;
	
	@Required
	@ManyToOne
	public Role role;
	
	@ManyToOne
	public Project project;

	public UserRole(User user, Role role) {
		super();
		this.user = user;
		this.role = role;		
	}
	
	public UserRole(User user, Role role, Project project) {
		super();
		this.user = user;
		this.role = role;
		this.project = project;
	}
	
	
	
}
