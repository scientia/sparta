package models.number;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.Transient;

import play.Play;

@Entity
public class ECRNumber extends IdentifierNumber{

	@Transient
	public static String ECR_NUMBER_FORMAT = "sparta.ecr.format";
	
	@Transient
	public static String DEFAULT_ECR_NUMBER_FORMAT = "ECR000000";
	
	public ECRNumber(){
	}

	public static String getECRNumber(){
		String numberformat = (String) Play.configuration.get(ECR_NUMBER_FORMAT);
		if(numberformat == null){
			numberformat = DEFAULT_ECR_NUMBER_FORMAT;
		}
		ECRNumber p = new ECRNumber().save();
		DecimalFormat format = new DecimalFormat(numberformat);
		Long generated = p.getId();
		p.delete();
		return format.format(generated);
		
	}
}
