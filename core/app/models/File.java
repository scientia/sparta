package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.MaxSize;
import play.data.validation.Required;

@Entity
public class File extends AuthoredTemporalModel{

	@Required
	@MaxSize(100)
	public String name;
	
	public String osName;
	
	@ManyToOne
	public Space space;
	
	public FileContent content;
	
	public FileContentType contentType;
	
	public File(String name, String osName, FileContent content, Space space){
		this.name = name;
		this.osName = osName;
		this.content = content;
		this.space = space;
	}
	
	public String toString(){
		return name;
	}
}
