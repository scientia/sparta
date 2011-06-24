package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class EChangeResultedPart extends EChangePart{

	public EChangeResultedPart(EngineeringChange change, PartVersion partVersion) {
		super();
		this.partVersion = partVersion;
		this.change = change;
	}
	
}
