package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import play.data.validation.MaxSize;
import play.data.validation.Required;

//@Entity
//@javax.persistence.Inheritance(strategy=InheritanceType.JOINED)
@MappedSuperclass
public abstract class Change extends ItemModel{
	public String number;
	
	@Required
	public String name;
	
	@Required
	@ManyToOne
	public Project project;
	
	public String urgency;
	
	@Lob
    @Required
    @MaxSize(10000)
    public String description;
	
	@Lob
    @MaxSize(10000)
    public String solution;
	
	/**
	 * The person who requests the change
	 */
	@ManyToOne
	public User originator;
	
	/**
	 * The person who analses/modifies the change.
	 */
	@ManyToOne
	public User analyst;
	
	/**
	 * CCB might ask another person to evaluate the change
	 * Evaluator
	 */	
	@ManyToOne
	public User evaluator;
	
	/**
	 * Manager has to be there, everwhere.
	 */
	@ManyToOne
	public User manager;
	
	/**
	 * The person who verifies if the change has been done properly.
	 * He might just observe the ECR while work on ECO once it is
	 * complete.
	 */
	
	@ManyToOne
	public User verifier;
	/**
	 * Accepted, Rejected, Next Product by Cognizant engineer
	 */
	public String problemStatus;
	
	/**
	 * Reason for acceptance/rejection
	 */
	
	@Lob
    @MaxSize(10000)
    public String reason;
	
	@Lob
	@MaxSize(500)
	public String acceptConditions;
	
	public Date enggSignoff;
	
	/**
	 * Approved
	 * Cancelled
	 * Changed
	 * Closed
	 * Evaluated
	 * Rejected
	 * Submitted
	 * Verified
	 */
	public String workflowstatus;
	/**
	 * Customer for which the change is requested
	 * 
	 */

	@ManyToOne(cascade=CascadeType.PERSIST)
	public Customer customer;
	
	public String toString(){
		return name;
	}
	
	@PrePersist
    protected void onOriginator() {
		if(this.originator == null || this.originator.username == null){
			User currentUser = User.find("byUsername",User.getConnected()).first();
	    	if(currentUser != null ){
	    	originator = currentUser;
	    	}
		}
	}
	//@PrePersist
	/*protected void beforeCreate(){
		if(this.originator == null || this.originator.username == null){
			User currentUser = User.find("byUsername",User.getConnected()).first();
	    	if(currentUser != null ){
	    	originator = currentUser;
	    	}
		}
	}*/
	
	public abstract List<Task> getTasks();
	
	public void addCustomer(Customer customer){
		new ChangeCustomer(this.id, this.getClass().getName(), customer).save();
	}
	
	public void addPlant(Plant plant){
		new ChangePlant(this.id, this.getClass().getName(), plant).save();
	}
	
	public void addSupplier(Supplier supplier){
		new ChangeSupplier(this.id, this.getClass().getName(), supplier).save();
	}

	public List<Customer> getCustomers(String id, String entityClass){
		List<ChangeCustomer> changecustomers = ChangeCustomer.find("byChangeIdAndChangeClass", this._key(), entityClass).fetch();
		List<Customer> customers = new ArrayList<Customer>();
		for(ChangeCustomer chgcustomer: changecustomers){
			customers.add(chgcustomer.customer);
		}	
		return customers;
	}
	
	public List<Plant> getPlants(String id, String entityClass){
		List<ChangePlant> changeplants = ChangePlant.find("byChangeIdAndChangeClass", this._key(), entityClass).fetch();
		List<Plant> plants = new ArrayList<Plant>();
		for(ChangePlant chgplant: changeplants){
			plants.add(chgplant.plant);
		}	
		return plants;
	}
	
	public List<Supplier> getSuppliers(String id, String entityClass){
		List<ChangeSupplier> changesuppliers = ChangeSupplier.find("byChangeIdAndChangeClass", this._key(), entityClass).fetch();
		List<Supplier> suppliers = new ArrayList<Supplier>();
		for(ChangeSupplier chgsupplier: changesuppliers){
			suppliers.add(chgsupplier.supplier);
		}	
		return suppliers;
	}
}
