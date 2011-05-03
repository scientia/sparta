package models;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;

@MappedSuperclass
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"name"})
		})
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class AdminModel extends TemporalModel {

	@Required
	@MaxSize(100)
	public String name;
}
