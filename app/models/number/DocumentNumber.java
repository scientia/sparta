package models.number;

import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.Transient;

import play.Play;

@Entity
public class DocumentNumber extends IdentifierNumber{

	@Transient
	public static String DOCUMENT_NUMBER_FORMAT = "sparta.docnumber.format";
	
	@Transient
	public static String DEFAULT_DOC_NUMBER_FORMAT = "D00000000";
	
	public DocumentNumber(){
		super();
	}

	public static String getDocumentNumber(){
		String numberformat = (String) Play.configuration.get(DOCUMENT_NUMBER_FORMAT);
		if(numberformat == null){
			numberformat = DEFAULT_DOC_NUMBER_FORMAT;
		}
		DocumentNumber p = new DocumentNumber().save();
		DecimalFormat format = new DecimalFormat(numberformat);
		Long generated = p.getId();
		p.delete();
		return format.format(generated);
		
	}
}
