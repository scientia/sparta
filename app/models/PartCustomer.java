package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity

public class PartCustomer extends PartOrg{

	/**
	 * This entity would store the customer part information
	 * mapping to the current revision of the part.
	 */
	
	@ManyToOne
	public Customer customer;
	
	/**
	 * parent nodes
	 */
	
	public PartCustomer(Customer customer, PartVersion partVersion){		
		this.customer = customer;
		this.partVersion = partVersion;
	}
	
	public PartCustomer(Customer customer, PartVersion partVersion, String number, String name, String revision){		
		this.customer = customer;
		this.partVersion = partVersion;
		this.orgPartnumber = number;
		this.orgPartName = name;
		this.orgPartRevision = revision;		
	}
}
