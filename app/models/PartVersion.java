package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Valid;


@Table(uniqueConstraints={
		@UniqueConstraint(name="productversionuq", columnNames={"identifier_id", "revision", "iteration"})		
})
@Entity
public class PartVersion extends ProductVersion{

	@Transient
	private static String BY_USEDON_RELATION = "byUsedOn";
	@Transient
	private static String BY_USES_RELATION = "byUses";
	
	public String unitValue;
	public String material;
	public String finish;	
	public String location;
	
	public ReleaseState releaseState;
	
	
	@ManyToOne
	//@Required	
	@Valid public Space space;
	
	
	public Boolean makeOrBuy = false;

	//TODO:
	/** If you add this annotation we get a JPA error.
	 * identifier not defined
	 */
	@ManyToOne(cascade=CascadeType.PERSIST, optional=false,targetEntity=Part.class)
	public Part identifier;
	
	
	public PartVersion(String name, Part identifier){
		this.name = name;
		this.identifier = identifier;
	}
	public PartVersion() {
		// TODO Auto-generated constructor stub
	}
	public String getDisplayName(){
		StringBuffer sf = new StringBuffer();
		sf.append("S");
		sf.append(this.identifier.number);
		sf.append("-");
		sf.append(this.identifier.tab);
		sf.append(",");
		sf.append(this.revision);
		sf.append(",");
		sf.append(this.iteration);
		return sf.toString();
		//return name+"-"+this.identifier.tab+"-"+this.revision.toString()+"-"+this.iteration.toString();
	}
	
	public String toString(){
		return name;
	}

	/*public Set<Plant> getPlants(){
		return identifier.plants;
	}*/
	
	
	/**
	 * 
	 * @return
	 */
	public List<PartVersion> getUsesParts(){
		List<PartStructure> structures = PartStructure.find("byUsedOn", this).fetch();
		List<PartVersion> parts = new ArrayList<PartVersion>();
		for(PartStructure struct: structures){
			parts.add(struct.uses);
		}
			
		return parts;
	}
	
	public List<PartVersion> getUsedOnParts(){
		List<PartStructure> structures = PartStructure.find(BY_USES_RELATION, this).fetch();
		List<PartVersion> parts = new ArrayList<PartVersion>();
		for(PartStructure struct: structures){
			parts.add(struct.usedOn);
		}
		
		return parts;
	}
	
	public PartStructure addParts(PartVersion child){
		return new PartStructure(this, child).save();
	}
	
	public void addDocument(DocumentVersion document){
		new PartDocument(document, this);
	}
	public void addPart(PartVersion part) {
		new PartStructure(this, part).save();		
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
		List<PartDocument> partdocuments = PartDocument.find("byPartDocument", this).fetch();
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
			EngineeringChange change = affpartrln.change ;
			if( change instanceof ECO){
			    ecos.add((ECO)affpartrln.change);
			}
		}	
		return ecos;
	}
	
	public List<ECO> getECRs() {
		List<EChangeAffectedPart> affectedparts = EChangeAffectedPart.find("byPartVersion", this).fetch();
		List<ECO> ecos = new ArrayList<ECO>();
		for(EChangeAffectedPart affpartrln: affectedparts){
			EngineeringChange change = affpartrln.change ;
			if( change instanceof ECR){
			    ecos.add((ECO)affpartrln.change);
			}
		}	
		return ecos;
	}
	
	public void addECR(ECR ecr) {
		new EChangeAffectedPart(ecr, this).save();
		
	}
}
