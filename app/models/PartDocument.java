package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class PartDocument extends AuthoredTemporalModel{

	@ManyToOne
	public DocumentVersion documentVersion;
	
	@ManyToOne
	public PartVersion partVersion;

	public PartDocument(DocumentVersion documentVersion, PartVersion partVersion) {
		super();
		this.documentVersion = documentVersion;
		this.partVersion = partVersion;
	}
	
	
}
