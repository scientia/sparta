package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.db.jpa.Model;


@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"realm"})
})
@Entity

public class Lookup extends TemporalModel{
    
	
	public String realm;
	/*String, int etc*/
	public String codeType;
	public String valueType;
	public String ordering;
	public boolean internationalize= false;
	
	
	public Lookup(String realm, String codeType, String valueType,
			String ordering, boolean internationalize) {
		super();
		this.realm = realm;
		this.codeType = codeType;
		this.valueType = valueType;
		this.ordering = ordering;
		this.internationalize = internationalize;
	}
	
}
