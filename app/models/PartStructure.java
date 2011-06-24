package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PartStructure extends ProductStructure{
	
	public double quantity = 0.00;
	public int findNumber = 0;

	/**
	 * child nodes
	 */
	@ManyToOne
	public PartVersion uses;
	
	/**
	 * parent nodes
	 */
	@ManyToOne
	public PartVersion usedOn;
	
	public PartStructure(PartVersion parent, PartVersion child){
		super();
		this.uses = child;
		this.usedOn = parent;
		this.quantity= 0;
		this.findNumber = 0;
	}

	public PartStructure(PartVersion uses,
			PartVersion usedOn, double quantity, int findNumber) {
		super();
		this.quantity = quantity;
		this.findNumber = findNumber;
		this.uses = uses;
		this.usedOn = usedOn;
	}
}
