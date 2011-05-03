package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Valid;

@Entity
public class ECO extends EngineeringChange{
	
	public boolean documentOnly;
	public boolean interchangeable;
	public boolean noninterchangeable;
	
	/**
	 * The point in the manufacturing process at which the change will be made effective
	 * Effective points
	 */
	
	public boolean nextOrder;
	public boolean nextPO;
	public boolean atVendor;
	public boolean whseIssue;
	public boolean inAsm;
	public boolean inTest;
	public boolean burnIn;
	public boolean systemTest;
	public boolean inFGS;
	public boolean inField;	
	
	/*Effectivit Dates*/
	public Date plannedDate;
	public Date replanDate;
	public Date actualDate;
	
	/**
	 * Justification for change
	 */
	@Lob
    @Required
    @MaxSize(10000)
    public String justification;
	
	@Lob
    @Required
    @MaxSize(10000)
    public String affectedApps;
	
	@Lob
    @Required
    @MaxSize(10000)
    public String backgroundFacts;
	
	public String approvalPlan;
	
	public boolean retrofit;
	
	public RevReason revReason; 
	
	/**
	 * Affected epartments
	 */
	public YesNoType regulators;

	/*SW/FM*/
	public YesNoType swfw;
	public YesNoType tools;
	public YesNoType testEquip;
	public YesNoType assmProcess;
	public YesNoType suppliers;
	public YesNoType packaging;
	public YesNoType publications;
	public YesNoType productSpec;
	
	public Date startDate;
	public Date designComplete;
	public Date mrpUpdated;
	public Date closedDate;

	public void addECR(ECR ecr) {
		new EcoEcr(this, ecr).save();	
	}
	
	public List<ECR> getECRs() {
		List<EcoEcr> changerelns = EcoEcr.find("byEco", this).fetch();
		List<ECR> ecrs = new ArrayList<ECR>();
		for(EcoEcr ecoecrrln: changerelns){					
			ecrs.add(ecoecrrln.ecr);			
		}	
		return ecrs;
	}
}
