package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;


import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.JPA;

@Table(uniqueConstraints={
		@UniqueConstraint(name="partuq", columnNames={"number", "tab"})
})
@Entity
public class Part extends AuthoredTemporalModel{
	
	
	/*@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "SmartIdSequenceGenerator")
	@org.hibernate.annotations.GenericGenerator(name = "SmartIdSequenceGenerator",strategy = "util.SmartIdSequenceGenerator",
	         parameters = {
	             @org.hibernate.annotations.Parameter(name = "format", value = "PRT0000001")
	         })
    
    @SequenceGenerator(name="PartNumber_Gen", sequenceName="PartNumber-Seq")
    @GeneratedValue(generator="PartNumber_Gen")
    public Long partnumber;*/
    
    @Required
	public String number;
    
    @Required
	public String tab = "00";
	
	public Boolean servicePart;
	
	/*@javax.persistence.OneToMany(mappedBy = "identifier", cascade=CascadeType.PERSIST)
	public java.util.Set<PartVersion> revisionsOfPart = new java.util.HashSet<PartVersion>();
	
	@javax.persistence.ManyToMany
	public java.util.Set<Supplier> suppliers = new java.util.HashSet<Supplier>();
	
	@javax.persistence.ManyToMany
	public java.util.Set<Customer> customers = new HashSet<Customer>();
	
	@javax.persistence.ManyToMany
	public java.util.Set<Plant> plants = new HashSet<Plant>();*/
	
	public Part(String number, String tab, Boolean servicePart){
		this.number = number;
		this.tab = tab;
		this.servicePart = servicePart;		
	}
	
	public Part(String number){
		this.number=number;
	}
	
	public Part(){
		
	}
	public String toString(){
		return number;
	}
	
	/*public List<PartVersion> getRevisionsOfPart(){
		return null;
	}*/
	
	/*public static String getNextNumber(){
		Query query = JPA.em().createQuery("select max(number) from Part");
	}*/
}
