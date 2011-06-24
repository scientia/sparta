package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class StaffProject extends TemporalModel {

	@ManyToOne
	public Staff staff;
	
	@ManyToOne
	public Project project;

	public StaffProject(Staff staff, Project project) {
		super();
		this.staff = staff;
		this.project = project;
	}
}
