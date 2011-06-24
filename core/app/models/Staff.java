package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Staff extends TemporalModel{

	@Required
	@MaxSize(100) @MinSize(5)
	public String name;
	
	@Lob
	@MaxSize(1000)
	public String description;

	
	@Override
	public String toString() {
		return name;
	}
	public List<User> getUsers() {
		List<StaffUser> staffusers = StaffUser.find("byStaff", this).fetch();	
		List<User> users = new ArrayList<User>();
		for(StaffUser staffuser: staffusers){
			users.add(staffuser.user);
		}
		return users;
	}
	
	public List<Project> getProjects(){
		List<StaffProject> staffprojects = StaffProject.find("byStaff", this).fetch();	
		List<Project> projects = new ArrayList<Project>();
		for(StaffProject staffproject: staffprojects){
			projects.add(staffproject.project);
		}
		return projects;
	}
	
	public void addUser(User user){
		new StaffUser(user, this).save();
	}
}
