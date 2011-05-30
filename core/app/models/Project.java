package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;


@Entity
public class Project extends AdminModel{
	
	
	public Date startDate;
	
	public Date endDate;
	
	@Lob
    @Required
    @MaxSize(10000)
    public String description;
	
	public Blob logo;
	
	/**
	 * Every project needs a leader.
	 */
	@ManyToOne
	public User manager;
	
	@ManyToOne
	public Staff staff;
	
	/**Participating organizations*/
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<Organization> organizations;
	
	@ManyToOne
	public Organization ownerOrganization;
	
	/**
	 * Every project has two defined spaces.
	 * Multiple projects can share a space.
	 * 
	 */
	@ManyToOne
	public Space releaseSpace;
	
	@ManyToOne
	public Space wipSpace;
	
	
	public Project(String name){
		this.name = name;
		organizations = new HashSet<Organization>();
	}
	
	public String toString(){
		return name;
	}

	public void addOrganization(Organization org) {
		this.organizations.add(org);
	}
	
	
	public List<Staff> getStaffs(){
		List<StaffProject> staffprojects = StaffProject.find("byProject", this).fetch();	
		List<Staff> staffs = new ArrayList<Staff>();
		for(StaffProject staffproject: staffprojects){
			staffs.add(staffproject.staff);
		}
		return staffs;
	}
	
	/** 
	 *TODO: This needs to be customized further. 
	 * Project could have many tasks that are not necessarily user is looking for
	 * @return
	 */
	public List<Task> getTasks(){
		List<Task> tasks = Task.find("byProject", this).fetch();		
		return tasks;
	}
	
	public void addStaff(Staff staff){
		new StaffProject(staff, this).save();
	}
}
