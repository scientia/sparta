package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import models.Plant;
import models.RelationModel;

@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(name="changesupplieruq", columnNames={"changeId", "changeClass","supplier_id" })
})
public class ChangeSupplier extends RelationModel {

    public Long changeId;
	
	public String changeClass;
	
	@ManyToOne
	public Supplier supplier;

	public ChangeSupplier(Long changeId, String changeClass, Supplier supplier) {
		super();
		this.changeId = changeId;
		this.changeClass = changeClass;
		this.supplier = supplier;
	}
}
