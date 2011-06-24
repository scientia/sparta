package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;

@Entity
@Table(uniqueConstraints={
@UniqueConstraint(columnNames={"name"})
})
public class Customer extends TemporalModel {
	
	@Required
    public String name;
	
    @ManyToOne
	public User contactPerson;
	
	@Embedded
	public Address officeAddress;
	
	public Customer(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
	public List<PartVersion> getParts(){
		List<PartCustomer> partcustomers = PartCustomer.find("byCustomer", this).fetch();
		List<PartVersion> parts = new ArrayList<PartVersion>();
		for(PartCustomer partcust: partcustomers){
			parts.add(partcust.partVersion);
		}	
		return parts;
	}
	
	public List<ECO> getECOs(){
		List<ECO> ecos = ECO.find("byCustomer", this).fetch();
		return ecos;
	}

	public List<ECR> getECRs(){
		List<ECR> ecrs =  ECR.find("byCustomer", this).fetch();
		return ecrs;
	}
}
