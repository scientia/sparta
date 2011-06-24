package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import models.Plant;
import models.RelationModel;

@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(name="changesupplieruq", columnNames={"changeId", "changeClass","plant_id" })
})
public class ChangePlant extends RelationModel {

	public Long changeId;
	
	public String changeClass;
	
	@ManyToOne
	public Plant plant;

	public ChangePlant(Long changeId, String changeClass, Plant plant) {
		super();
		this.changeId = changeId;
		this.changeClass = changeClass;
		this.plant = plant;
	}

}
