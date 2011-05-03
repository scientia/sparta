package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PartPlant extends PartOrg{

	/**
	 * This entity would store the plant part information
	 * mapping to the current revision of the part.
	 * 
	 * the plant person will be responsible for updating the 
	 * manufacturing name number revision 
	 */
	
	@ManyToOne
	public Plant plant;
	
	/**
	 * parent nodes
	 */
	
	public PartPlant(Plant plant, PartVersion partVersion){		
		this.plant = plant;
		this.partVersion = partVersion;
	}
	
	public PartPlant(Plant plant, PartVersion partVersion, String number, String name, String revision){		
		this.plant = plant;
		this.partVersion = partVersion;
		this.orgPartnumber = number;
		this.orgPartName = name;
		this.orgPartRevision = revision;		
	}
	
	/** Get attached plants*/
	
	public List<PartVersion> getParts(){
		List<PartPlant> partplants = PartPlant.find("byPlant", this).fetch();
		List<PartVersion> parts = new ArrayList<PartVersion>();
		for(PartPlant partplant: partplants){
			parts.add(partplant.partVersion);
		}	
		return parts;
	}
	
}
