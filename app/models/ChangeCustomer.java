package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import models.Customer;
import models.RelationModel;

@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(name="changesupplieruq", columnNames={"changeId", "changeClass","customer_id" })
})
public class ChangeCustomer extends RelationModel {

	public Long changeId;
	
	public String changeClass;
	
	@ManyToOne
	public Customer customer;

	public ChangeCustomer(Long changeId, String changeClass, Customer customer) {
		super();
		this.changeId = changeId;
		this.changeClass = changeClass;
		this.customer = customer;
	}

}
