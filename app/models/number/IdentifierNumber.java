package models.number;

import java.text.DecimalFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import play.Play;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@MappedSuperclass
public abstract class IdentifierNumber extends Model {
	
	@Transient
	public String number;
	
	
   
}
