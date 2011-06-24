package models;

import java.util.HashSet;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import controllers.Secure;


@Entity
public class Supplier extends TemporalModel {

	public String name;
	
	public User contactPerson;
	
	@Embedded
	public Address officeAddress;
	
	/*@javax.persistence.ManyToMany(mappedBy = "suppliers")
	private java.util.Set<Part> supplierForProducts = new HashSet<Part>();*/
	
	public Supplier(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
