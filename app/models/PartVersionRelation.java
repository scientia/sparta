package models;

import javax.persistence.Entity;

import play.data.validation.Valid;

@Entity
public class PartVersionRelation extends ProductVersionRelation {

	@Valid public PartVersion related;
	@Valid public PartVersion relating;
}
