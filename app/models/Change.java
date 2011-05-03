package models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import play.data.validation.MaxSize;
import play.data.validation.Required;

@MappedSuperclass
public abstract class Change extends AuthoredTemporalModel{

	public String number;
	
	@Required
	public String name;
	
	@ManyToOne
	public Project project;
	
	public String urgency;
	
	@Lob
    @Required
    @MaxSize(10000)
    public String description;
	
	@Lob
    @Required
    @MaxSize(10000)
    public String solution;
	
	/**
	 * The person who requests the change
	 */
	@ManyToOne
	public User originator;
	
	/**
	 * The person who analses/modifies the change.
	 */
	@ManyToOne
	public User analyst;
	
	/**
	 * CCB might ask another person to evaluate the change
	 * Evaluator
	 */	
	@ManyToOne
	public User evaluator;
	
	/**
	 * Manager has to be there, everwhere.
	 */
	@ManyToOne
	public User manager;
	
	/**
	 * The person who verifies if the change has been done properly.
	 * He might just observe the ECR while work on ECO once it is
	 * complete.
	 */
	
	@ManyToOne
	public User verifier;
	/**
	 * Accepted, Rejected, Next Product by Cognizant engineer
	 */
	public String problemStatus;
	
	/**
	 * Reason for acceptance/rejection
	 */
	
	@Lob
    @MaxSize(10000)
    public String reason;
	
	@Lob
	@MaxSize(500)
	public String acceptConditions;
	
	public Date enggSignoff;
	
	/**
	 * Approved
	 * Cancelled
	 * Changed
	 * Closed
	 * Evaluated
	 * Rejected
	 * Submitted
	 * Verified
	 */
	public String workflowstatus;
	/**
	 * Customer for which the change is requested
	 * 
	 */

	@ManyToOne(cascade=CascadeType.PERSIST)
	public Customer customer;
	
	public String toString(){
		return name;
	}
	
}
