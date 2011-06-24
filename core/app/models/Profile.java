package models;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Profile extends TemporalModel {

	@OneToOne
	public User user;
	
	@ManyToOne
	public ViewDefinitionContext defaultContext;
	
	@Transient
	public Set<ViewDefinitionContext> availableContexts;
	
	@ManyToMany
	public Set<User> following;
	
	@ManyToMany
	public Set<User> followers;
	
	public Profile(){
		this.following = new TreeSet<User>();
		this.followers = new TreeSet<User>();
	}
}
