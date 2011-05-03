package models;

import java.util.HashSet;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@Entity
public class Plant extends TemporalModel{

    public String name;
	
    @ManyToOne
	public User contactPerson;
	
	@Embedded
	public Address officeAddress;
	
	/*@javax.persistence.ManyToMany(mappedBy = "plants")
	public java.util.Set<Part> manufacturesParts = new HashSet<Part>();*/
	
	public Plant(String name){
		this.name = name;
	}
	
	/*public String toString(){
		return name;
	}*/
}
