package models;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@MappedSuperclass
public abstract class ProductStructure extends AuthoredTemporalModel {

	@ManyToOne
	public ViewDefinitionContext context;
}
