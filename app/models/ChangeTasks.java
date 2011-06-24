package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ChangeTasks extends AuthoredTemporalModel {

	@OneToOne
	public Task task;
}
