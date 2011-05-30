package models;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import controllers.Products;

import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.Required;

/** 
 * If named as "product" the compilation fails.
 * The way core controller gets the entity names.
 * Need a way to skip the abstract classes
 * @author snehal
 *
 */
@MappedSuperclass
public abstract class Identifier extends AuthoredTemporalModel {


	@Transient
    protected static String DEFAULT_TAB_FORMAT = "00";
	
	//@Required
	@MaxSize(20)
	//@Match("[A-Z]{20}")
	public String number;

	//@Required
	@MaxSize(4)
	public String tab = "00";

    public Identifier(){
		
	}
    
	public Identifier(String number, String tab) {
		this.number = number;
		this.tab = tab;
	}

	public Identifier(String number) {
		this.number = number;
	}
	
	public abstract String getNextTab();
	
}
