package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;


import play.db.jpa.*;

@Entity
public class Role extends AdminModel implements Comparable<Role>{

	@Transient
	private static String BY_ROLE_RELATION = "byRole";
	
	public Role(String name){
		this.name = name;
	}
	public int compareTo(Role otherProf) {
		return name.compareTo(otherProf.name);
	}
	
	public static Role findByName(String rolename){
		return Role.find("byName", rolename).first();
	}
	
	public String toString(){
		return name;
	}
	
	public List<User> getUsers(){
		List<UserRole> userroles = UserRole.find(BY_ROLE_RELATION, this).fetch();
		List<User> users = new ArrayList<User>();
		for(UserRole userrole: userroles){
			users.add(userrole.user);
		}
		return users;
	}
	public void addUser(User usr) {
		new UserRole(usr, this).save();
	}
}
