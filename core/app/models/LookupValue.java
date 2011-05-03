package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;

@Entity
//@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "lookup" }) })
public class LookupValue extends TemporalModel {

	@Required
	@ManyToOne
	public Lookup lookup;
	public String code;
	public String value;

	public LookupValue(Lookup lookup, String code, String value) {
		super();
		this.lookup = lookup;
		this.code = code;
		this.value = value;
	}
	
	
}
