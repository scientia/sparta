package models;

import java.text.DecimalFormat;
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
public class Part extends Identifier{
	
	private static String[] cloneProps = {"number", "servicePart", "uom"};
	
	/*@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "SmartIdSequenceGenerator")
	@org.hibernate.annotations.GenericGenerator(name = "SmartIdSequenceGenerator",strategy = "util.SmartIdSequenceGenerator",
	         parameters = {
	             @org.hibernate.annotations.Parameter(name = "format", value = "PRT0000001")
	         })
    
    @SequenceGenerator(name="PartNumber_Gen", sequenceName="PartNumber-Seq")
    @GeneratedValue(generator="PartNumber_Gen")
    public Long partnumber;*/
	
	public Boolean servicePart = false;
	
	@ManyToOne
	public UnitOfMeasure uom;
	
	
	/*@javax.persistence.OneToMany(mappedBy = "identifier", cascade=CascadeType.PERSIST)
	public java.util.Set<PartVersion> revisionsOfPart = new java.util.HashSet<PartVersion>();
	
	@javax.persistence.ManyToMany
	public java.util.Set<Supplier> suppliers = new java.util.HashSet<Supplier>();
	
	@javax.persistence.ManyToMany
	public java.util.Set<Customer> customers = new HashSet<Customer>();
	
	@javax.persistence.ManyToMany
	public java.util.Set<Plant> plants = new HashSet<Plant>();*/
	
	public Part(String number, String tab, Boolean servicePart){
		super(number, tab);		
		this.servicePart = servicePart;		
	}
	
	public Part(String number){
		super(number);
	}
	
	public Part(){
		
	}
	public String toString(){
		return number+","+tab;
	}

	@Override
	public String getNextTab() {
		Long count = Part.count("number=?", this.number);
		//q.setParameter(1, this.number);		
		//int tabcount = (Integer)q.getSingleResult();
		DecimalFormat df = new DecimalFormat("00");
		return df.format(count);
	}

	public Part getTabbedPart() {
		Part tabbedId = new Part();
		this._copy(tabbedId);
		tabbedId.tab = getNextTab();
		tabbedId.save();
		return tabbedId;
	}
	
	@Override
    public String[] getCopyProperties() {
    	return cloneProps;
    }
	/*public List<PartVersion> getRevisionsOfPart(){
		return null;
	}*/
	
	/*public static String getNextNumber(){
		Query query = JPA.em().createQuery("select max(number) from Part");
		Integer a = (Integer)query.getSingleResult();
		return a.toString();
	}*/
}
