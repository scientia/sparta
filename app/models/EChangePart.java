package models;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
public class EChangePart extends PartChange{

	@ManyToOne(optional=false)
	public EngineeringChange change;
}