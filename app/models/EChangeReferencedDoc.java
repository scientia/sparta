package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class EChangeReferencedDoc extends ChangeDocs {

	@ManyToOne(optional=false)
	public EngineeringChange change;

	public EChangeReferencedDoc(EngineeringChange change, DocumentVersion documentVersion) {
		super();
		this.change = change;
		this.documentVersion = documentVersion;
	}
}
