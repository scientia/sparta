package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"ecr_id", "eco_id"})
})
@Entity
public class EcoEcr extends AuthoredTemporalModel{

	@ManyToOne(optional=false)
	ECO eco;
	
	@ManyToOne(optional=false)
	ECR ecr;

	public EcoEcr(ECO eco, ECR ecr) {
		super();
		this.eco = eco;
		this.ecr = ecr;
	}

}
