package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class StaffUser extends TemporalModel {

	@ManyToOne
	public User user;
	
	@ManyToOne
	public Staff staff;

	public StaffUser(User user, Staff staff) {
		super();
		this.user = user;
		this.staff = staff;
	}
}
