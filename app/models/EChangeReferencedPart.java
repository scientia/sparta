package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class EChangeReferencedPart extends EChangePart {

	public EChangeReferencedPart(EngineeringChange change, PartVersion partVersion) {
		super();
		this.partVersion = partVersion;
		this.change = change;
	}
}
