package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;


@Entity
public class EChangeAffectedPart extends EChangePart {
	
	/**
	 * Use As Is, Scrap, Return to Vendor, Reworkable, Manufacturing Rework
	 */
	@MaxSize(20)
	public String partDisposition;
	
	/**
	 * Interchangable
	 * NonInterchangable 
	 */
	@MaxSize(20)
	public String changeClass;
	
	public EChangeAffectedPart(EngineeringChange change, PartVersion partVersion) {
		super();
		this.partVersion = partVersion;
		this.change = change;
	}
}
