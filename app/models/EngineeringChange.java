package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

/**
 * Rule to remember for Joined classes.
 * The deeper the hierarchy the more joins it will take to assemble instances 
 * of the concrete entity at the bottom. The broader the hierarchy the more joins 
 * it will take to query across an entity superclass
 * 
 * Keep the hierarchy as flat as possible. avoid query calls at the super class.
 * @author snehal
 *
 */
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy=InheritanceType.JOINED)
//@MappedSuperclass
public abstract class EngineeringChange extends Change{

	
	public void addAffectedPart(PartVersion partVersion){
		new EChangeAffectedPart(this, partVersion).save();
	}
	
	public List<PartVersion> getAffectedParts(){
		List<EChangeAffectedPart> affectedchangeparts =  EChangeAffectedPart.find("byChange", this).fetch();
		List<PartVersion> affectedparts = new ArrayList<PartVersion>();
		for(EChangeAffectedPart affreln: affectedchangeparts){
			affectedparts.add(affreln.partVersion);
		}		
		return affectedparts;
	}
	
	public void addResultedPart(PartVersion partVersion){
		new EChangeResultedPart(this, partVersion).save();
	}
	
	public List<PartVersion> getResultedParts(){
		List<EChangeResultedPart> resultedchangeparts =  EChangeResultedPart.find("byChange", this).fetch();
		List<PartVersion> resultedparts = new ArrayList<PartVersion>();
		for(EChangeResultedPart reslreln: resultedchangeparts){
			resultedparts.add(reslreln.partVersion);
		}		
		return resultedparts;
	}
	
	public void addReferencedPart(PartVersion partVersion){
		new EChangeReferencedPart(this, partVersion).save();
	}
	
	public List<PartVersion> getReferencedParts(){
		List<EChangeReferencedPart> rfencedchangeparts =  EChangeReferencedPart.find("byChange", this).fetch();
		List<PartVersion> rfrnceddparts = new ArrayList<PartVersion>();
		for(EChangeReferencedPart rfrncreln: rfencedchangeparts){
			rfrnceddparts.add(rfrncreln.partVersion);
		}		
		return rfrnceddparts;
	}
	
	public void addReferencedDoc(DocumentVersion docVersion){
		new EChangeReferencedDoc(this, docVersion).save();
	}
	
	public List<DocumentVersion> getReferencedDocs(){
		List<EChangeReferencedDoc> rfencedchangedocs =  EChangeReferencedDoc.find("byChange", this).fetch();
		List<DocumentVersion> rfrncedddocs = new ArrayList<DocumentVersion>();
		for(EChangeReferencedDoc rfrncreln: rfencedchangedocs){
			rfrncedddocs.add(rfrncreln.documentVersion);
		}		
		return rfrncedddocs;
	}
	
	public List<Task> getTasks(){
		List<EngChangeTask> engchangetasks =  EngChangeTask.find("byChange", this).fetch();
		List<Task> tasks = new ArrayList<Task>();
		for(EngChangeTask engchangetask: engchangetasks){
			tasks.add(engchangetask.task);
		}		
		return tasks;
	}
}
