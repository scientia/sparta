package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;


import controllers.Security;
import controllers.ViewContexts;

import play.data.validation.Email;
import play.data.validation.Match;
import play.data.validation.Max;
import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Range;
import play.data.validation.Required;
import play.db.jpa.*;
import play.libs.Codec;
import play.modules.scaffold.NoScaffolding;
import play.mvc.Scope.Session;

@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"username"})
		})
public class User extends TemporalModel{
	
	@Transient
	private static String BY_USER_RELATION = "byUser";
	
	@Required
	@MinSize(4)
	@MaxSize(20)
	@Match("[a-zA-Z][a-zA-Z_0-9]*")
	public String username;
	
	@Email
	@Required
	public String email;
	
	@Required
	public boolean active = true;
	
	@Password
	public String password = "";
	
	@Required
	@MinSize(2)
	@MaxSize(20)
	public String firstName;
	
	@Required
	@MinSize(2)
	@MaxSize(20)
	public String lastName;
	
	public String passwordHash;
	
	public int loginCount;
	  
    public boolean isAdmin;
    
    @ManyToOne
    public ViewDefinitionContext viewContext;
    
    public User(String username, String email, String password) {
    	this.username = username;
        this.email = email;
        this.password = password;
        this.passwordHash = Codec.hexMD5(password);
        this.firstName = "PRashant";
        this.lastName = "Bhalesain";
        this.active=true;        
    }
    
    public User() {
		// TODO Auto-generated constructor stub
	}

    public String getDisplayName(){ 
    	if(firstName == null || lastName == null){
    	return username;
    	}
    	return this.firstName +" " + this.lastName;
    }
    
    public boolean checkPassword(String password) {
        return passwordHash.equals(Codec.hexMD5(password));
    }
    
	public static User connect(String username, String password) {
    	String encodedPwd = Codec.hexMD5(password);
        return find("byUsernameAndPassword", username, encodedPwd).first();
    }
    
   /* public String toString(){
    	return username;
    }*/
	/**
	 * Dont know why this is not handled in security module.
	 */
	
	@PrePersist
	private void onInsert(){
		if(viewContext == null || viewContext.name == null){
			ViewDefinitionContext context = ViewDefinitionContext.find("byName", "Default").first();
			if(context != null){
			this.viewContext = context;
			}
		}
	}
	
    public void addRole(String role){  
    	Role _role = Role.findByName(role);
    	System.out.println(_role.name);
    	//roles.add(_role);
    }
    
    public void addRole(Role role){      	
    		 new UserRole(this, role).save();
    }
    
    public static List<User> findUsersWithProfile(String profile) {
        return User.find(
            "select distinct u from User u join u.profiles as p where p.name = ?", profile
        ).fetch();
    }
    
    /** need more investigation with md5*/
    //TODO: this needs to work with mysql, if activated login failswith mysql
   /* public void setPassword(String password) {
        if (StringUtils.isBlank(password))
          return;
        this.passwordHash = Security.md5(password);
      }*/

    public void setPassword(String password){
    	if (StringUtils.isBlank(password))
            return;
          this.passwordHash =  Codec.hexMD5(password);
    }
    public boolean isCurrentUser(String currentUser) {
        String currentUserName = Session.current().get("username");
        if (StringUtils.isBlank(currentUserName))
          return false;
        return currentUserName.equalsIgnoreCase(currentUser);
    }
    
    public String toString(){
    	return username;
    }

	public static String getConnected() {
		if(Session.current() != null){
			return Session.current().get("username");
		}else{
			return null;
		}	
	}

	public List<Role> getRoles(){
		List<UserRole> userroles = UserRole.find(BY_USER_RELATION, this).fetch();	
		List<Role> roles = new ArrayList<Role>();
		for(UserRole userrole: userroles){
			roles.add(userrole.role);
		}
		return roles;
	}
	
	public List<Staff> getStaffs() {
		List<StaffUser> staffusers = StaffUser.find("byUser", this).fetch();	
		List<Staff> staffs = new ArrayList<Staff>();
		for(StaffUser staffuser: staffusers){
			staffs.add(staffuser.staff);
		}
		return staffs;
	}
	
	public List<Project> getManagedProjects(){
		return Project.find("byManager", this).fetch();
	}
	
	/** a user is member of staffs which participates in a a project*/
	public List<Project> getParticipatingProjects(){
		List<Project> projects = new ArrayList<Project>();
		List<Staff> staffs = getStaffs();
		for(Staff staff: staffs){
			List<Project> sProjects = staff.getProjects();
			projects.addAll(sProjects);
		}
		return projects;
	}

}
