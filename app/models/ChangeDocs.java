package models;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class ChangeDocs extends AuthoredTemporalModel{
	
	@ManyToOne(optional=false)
	public DocumentVersion documentVersion;

}
