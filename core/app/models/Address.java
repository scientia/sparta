package models;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import play.db.jpa.Model;

@Embeddable
public class Address implements java.io.Serializable{

	public String location;
	
	public String address1;
	
	public String address2;
	
	private String state;
	
	private String country;
	
	private String postalCode;
	
	private String fax;
	
	private String phone;
	
	private String email;

}
