package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


/**
 * 
 * Tasks can have multiple changes attached.
 * This would give opportunity to engineer or designer to work on
 * multiple changes that need same work. (change bundling)
 * @author snehal
 *
 */
@Entity
public class EngChangeTask extends AuthoredTemporalModel {
	
	@ManyToOne
	public EngineeringChange change;
	
	@ManyToOne
	public Task task;

	public EngChangeTask(EngineeringChange change, Task task) {
		super();
		this.change = change;
		this.task = task;
	}

}
