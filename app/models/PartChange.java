package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@MappedSuperclass

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"partVersion", "change"})
})
public class PartChange extends AuthoredTemporalModel {
	
	@ManyToOne(optional=false)
	public PartVersion partVersion;
}
