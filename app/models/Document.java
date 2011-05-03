package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"number","tab"})
})
public class Document extends TemporalModel{

    public String number;
    
	public String tab;
	
	public Document(String number, String tab){
		this.number = number;
		this.tab = tab;
	}
}