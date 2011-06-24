package models;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.beanutils.PropertyUtils;

import models.number.PartNumber;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
//import play.modules.elasticsearch.annotations.ElasticSearchable;


//@ElasticSearchable
@Table(uniqueConstraints={
		@UniqueConstraint(name="productversionuq", columnNames={"identifier_id", "revision", "iteration"})		
})
@Entity
public class PartVersion extends ProductVersion{

	@Transient
	private static String BY_USEDON_RELATION = "byUsedOn";
	@Transient
	private static String BY_USES_RELATION = "byUses";
	
	@Transient
	private static String[] cloneProps = {"name", "description", "project", "frozen", 
		                                  "cloned", "unitValue", "material", "finish", 
		                                  "location", "releaseState", "makeOrBuy", "identifier" };
	public String unitValue;
	public String material;
	public String finish;	
	public String location;
	
	public String releaseState;
	
	public Boolean makeOrBuy = false;

	//TODO:
	/** If you add this annotation we get a JPA error.
	 * identifier not defined
	 */
	@ManyToOne(cascade=CascadeType.PERSIST, optional=false,targetEntity=Part.class)
	@Valid public Part identifier;
	
	
	public PartVersion(String name, Part identifier){
		this.name = name;
		this.identifier = identifier;
	}
	
	public PartVersion() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getDisplayName(){
		StringBuffer sf = new StringBuffer();		
		sf.append(this.identifier.number);
		sf.append("-");
		sf.append(this.identifier.tab);
		sf.append(",");
		sf.append(this.revision);
		sf.append(",");
		sf.append(this.iteration);
		return sf.toString();		
	}
	
	public String toString(){
		return name;
	}
	
	@PrePersist
	public void beforeCreate(){
		/**
		 * Do we need to do this in separate transaction
		 */
		if(this.identifier.number == null){
		    String number = PartNumber.getPartNumber();
		    Part part = new Part(number).save();
		    this.identifier = part;
		}
	}
	
	@Override
	public PartVersion clone() {
		PartVersion  object = new PartVersion();
		object = (PartVersion)_clone(object);	
		return object;
	}
	
	@Override
    public PartVersion revise(){
		PartVersion  object = new PartVersion();
		object = (PartVersion)_revise(object);	
		return object;
	}
    
    @Override
	public PartVersion tab() {
		Part tabIdentifier = this.identifier.getTabbedPart();
		PartVersion  object = new PartVersion();
		object = (PartVersion)_tab(object);
		object.identifier = tabIdentifier;
		return object;
	}

    @Override
    public String[] getCopyProperties() {
    	return cloneProps;
    }
	
	public Integer getNextIteration(){
		Query q = JPA.em().createQuery("select max(iteration) from PartVersion where identifier=? and revision=?" );
		q.setParameter(1, this.identifier);
		q.setParameter(2, this.revision);
		return (Integer)q.getSingleResult() + 1;	
	}
	
	public Integer getNextRevision(){
		Query q = JPA.em().createQuery("select max(revision) from PartVersion where identifier=?" );
		q.setParameter(1, this.identifier);		
		return (Integer)q.getSingleResult() + 1;	
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PartStructure> getUsesParts(){
		List<PartStructure> structures = PartStructure.find("byUsedOn", this).fetch();
		List<PartVersion> parts = new ArrayList<PartVersion>();
		for(PartStructure struct: structures){
			parts.add(struct.uses);
		}	
		return structures;
	}
	
	public List<PartStructure> getUsedOnPartsStructures(){
		List<PartStructure> structures = PartStructure.find(BY_USES_RELATION, this).fetch();				
		return structures;
	}
	public List<PartVersion> getUsedOnParts(){
		List<PartStructure> structures = PartStructure.find(BY_USES_RELATION, this).fetch();
		List<PartVersion> parts = new ArrayList<PartVersion>();
		for(PartStructure struct: structures){
			parts.add(struct.usedOn);
		}		
		return parts;
	}
	
	
	public void addDocument(DocumentVersion document){
		new PartDocument(document, this);
	}
	
	public void addPart(PartVersion child) {
		new PartStructure(this, child).save();		
	}
	
	public List<Customer> getCustomers(){
		List<PartCustomer> partcustomers = PartCustomer.find("byPartVersion", this).fetch();
		List<Customer> customers = new ArrayList<Customer>();
		for(PartCustomer partcust: partcustomers){
			customers.add(partcust.customer);
		}	
		return customers;
	}
	
	public List<Plant> getPlants(){
		List<PartPlant> partplants = PartPlant.find("byPartVersion", this).fetch();
		List<Plant> plants = new ArrayList<Plant>();
		for(PartPlant partplant: partplants){
			plants.add(partplant.plant);
		}	
		return plants;
	}
	
	public List<Supplier> getSuppliers(){
		List<PartSupplier> partsupplier = PartSupplier.find("byPartVersion", this).fetch();
		List<Supplier> suppliers = new ArrayList<Supplier>();
		for(PartSupplier partplant: partsupplier){
			suppliers.add(partplant.supplier);
		}	
		return suppliers;
	}
	
	public void addCustomer(Customer customer) {
		new PartCustomer(customer, this).save();
	}
	
	public void addPlant(Plant plant) {
		new PartPlant(plant, this).save();
	}
	
	public void addSupplier(Supplier supplier) {
		new PartSupplier(supplier, this).save();
	}
	
	public List<DocumentVersion> getDocuments() {
		List<PartDocument> partdocuments = PartDocument.find("byPartVersion", this).fetch();
		List<DocumentVersion> documents = new ArrayList<DocumentVersion>();
		for(PartDocument partdoc: partdocuments){
			documents.add(partdoc.documentVersion);
		}	
		return documents;
	}
	
	public void addECO(ECO eco) {
		new EChangeAffectedPart(eco, this).save();
	}
	
	public List<ECO> getECOs() {
		List<EChangeAffectedPart> affectedparts = EChangeAffectedPart.find("byPartVersion", this).fetch();
		List<ECO> ecos = new ArrayList<ECO>();
		for(EChangeAffectedPart affpartrln: affectedparts){
			Change change = affpartrln.change ;
			if( change instanceof ECO){
			    ecos.add((ECO)affpartrln.change);
			}
		}	
		return ecos;
	}
	
	public List<ECR> getECRs() {
		List<EChangeAffectedPart> affectedparts = EChangeAffectedPart.find("byPartVersion", this).fetch();
		List<ECR> ecos = new ArrayList<ECR>();
		for(EChangeAffectedPart affpartrln: affectedparts){
			Change change = affpartrln.change ;
			if( change instanceof ECR){
			    ecos.add((ECR)affpartrln.change);
			}
		}	
		return ecos;
	}
	
	public void addECR(ECR ecr) {
		new EChangeAffectedPart(ecr, this).save();
	}
}
