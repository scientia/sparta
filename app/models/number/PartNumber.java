package models.number;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import play.Play;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
public class PartNumber extends IdentifierNumber{

	@Transient
	public static String PART_NUMBER_FORMAT = "sparta.partnumber.format";
	
	@Transient
	public static String DEFAULT_PART_NUMBER_FORMAT = "P00000000";
	
	public PartNumber(){
	}

	public static String getPartNumber(){
		String numberformat = (String) Play.configuration.get(PART_NUMBER_FORMAT);
		if(numberformat == null){
			numberformat = DEFAULT_PART_NUMBER_FORMAT;
		}
		PartNumber p = new PartNumber().save();
		DecimalFormat format = new DecimalFormat(numberformat);
		Long generated = p.getId();
		p.delete();
		return format.format(generated);
		
	}
}
