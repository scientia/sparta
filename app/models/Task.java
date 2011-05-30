package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import models.number.ECRNumber;
import models.number.TaskNumber;

import play.data.validation.InFuture;
import play.data.validation.IsTrue;
import play.data.validation.MaxSize;
import play.data.validation.Required;

@Entity
public class Task extends AuthoredTemporalModel {

    public String number;
	
	@Required
	public String name;
	
	@Lob
    @Required
    @MaxSize(10000)
    public String description;
	
	@Lob    
    @MaxSize(10000)
    public String actionTaken;
	
	@Lob   
    @MaxSize(10000)
    public String remarks;
	
	
	@ManyToOne
	public Project project;
	
	@ManyToOne
	@Required
	public User assignee;
	
	@InFuture
    public Date planStartDate;
	
	@InFuture
	public Date planEndDate;
	
	@Required
	@InFuture
	public Date requestedEndDate;
	
	@InFuture
	public Date actualStartDate;
	
	@InFuture
	public Date actualEndDate;
	
	public boolean effortDriven = false;
	
	public String type;
	
	
	public Integer duration;
	
	public Integer percentcomplete;
	
	public Integer priority;
	/**
	 * Assigned
	 * Accepted
	 * Rejected
	 * Cancelled
	 * Complete
	 */
	
	public String workflowstatus;

	
	public  Task(){
		
	}
	
	@PrePersist
	public void beforeCreate(){
		String number = TaskNumber.getTaskNumber();		
		this.number = number;
	}
	
	public List<EngineeringChange> getEngneeringChanges(){
		List<EngChangeTask> engchangetasks =  EngChangeTask.find("byTask", this).fetch();
		List<EngineeringChange> changes = new ArrayList<EngineeringChange>();
		for(EngChangeTask engchangetask: engchangetasks){
			changes.add(engchangetask.change);
		}		
		return changes;
	}
}
