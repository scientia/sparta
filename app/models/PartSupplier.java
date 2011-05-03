package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PartSupplier extends PartOrg {

	/**
	 * This entity would store the supplier part information
	 * mapping to the current revision of the part.
	 */
	
	
	@ManyToOne
	public Supplier supplier;
	
	/**
	 * parent nodes
	 */
	
	public PartSupplier(Supplier supplier, PartVersion partVersion){		
		this.supplier = supplier;
		this.partVersion = partVersion;
	}
	
	public PartSupplier(Supplier supplier, PartVersion partVersion, String number, String name, String revision){		
		this.supplier = supplier;
		this.partVersion = partVersion;
		this.orgPartnumber = number;
		this.orgPartName = name;
		this.orgPartRevision = revision;		
	}
	
	/** Get attached plants*/
	
	public List<PartVersion> getParts(){
		List<PartSupplier> partsuppliers = PartSupplier.find("bySupplier", this).fetch();
		List<PartVersion> parts = new ArrayList<PartVersion>();
		for(PartSupplier partSupplier: partsuppliers){
			parts.add(partSupplier.partVersion);
		}	
		return parts;
	}
}
