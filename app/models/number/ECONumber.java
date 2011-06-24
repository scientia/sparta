package models.number;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.Transient;

import play.Play;

@Entity
public class ECONumber extends IdentifierNumber{

	@Transient
	public static String ECO_NUMBER_FORMAT = "sparta.eco.format";
	
	@Transient
	public static String DEFAULT_ECO_NUMBER_FORMAT = "ECO000000";
	
	public ECONumber(){
	}

	public static String getECONumber(){
		String numberformat = (String) Play.configuration.get(ECO_NUMBER_FORMAT);
		if(numberformat == null){
			numberformat = DEFAULT_ECO_NUMBER_FORMAT;
		}
		ECONumber p = new ECONumber().save();
		DecimalFormat format = new DecimalFormat(numberformat);
		Long generated = p.getId();
		p.delete();
		return format.format(generated);
		
	}
}
