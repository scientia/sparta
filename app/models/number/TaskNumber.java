package models.number;

import java.text.DecimalFormat;

import javax.persistence.Transient;

import play.Play;

public class TaskNumber extends IdentifierNumber{

	@Transient
	public static String TASK_NUMBER_FORMAT = "sparta.tasknumber.format";
	
	@Transient
	public static String DEFAULT_TASK_NUMBER_FORMAT = "T000000";
	
	public TaskNumber(){
	}

	public static String getTaskNumber(){
		String numberformat = (String) Play.configuration.get(TASK_NUMBER_FORMAT);
		if(numberformat == null){
			numberformat = DEFAULT_TASK_NUMBER_FORMAT;
		}
		PartNumber p = new PartNumber().save();
		DecimalFormat format = new DecimalFormat(numberformat);
		Long generated = p.getId();
		p.delete();
		return format.format(generated);
		
	}
}
