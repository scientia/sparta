package models;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import controllers.Secure;
import controllers.Security;

import play.data.validation.Required;



@MappedSuperclass
public abstract class AuthoredTemporalModel extends TemporalModel{
	
	public String createdBy;
	
	public String updatedBy;
    
    @PrePersist
    protected void onCreatedBy() {
    	String currentUser = User.getConnected();
    	if(currentUser != null){
    	createdBy = updatedBy = currentUser;
    	}
	}

	@PreUpdate
	protected void onUpdatedBy() {
		String currentUser = User.getConnected();
    	if(currentUser != null){
    	createdBy = updatedBy = currentUser;
    	}					
	}

    
}
