package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import controllers.Security;

import play.db.jpa.Model;


@MappedSuperclass
public abstract class TemporalModel extends Model {

	@Transient
	public String displayName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", nullable = false)
	public Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastupdated", nullable = false)
	public Date lastUpdated;

	@PrePersist
	protected void onDateCreate() {
		lastUpdated = dateCreated = new Date();
	}

	@PreUpdate
	protected void onLastUpdate() {
		lastUpdated = new Date();					
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	public String getDisplayName(){
		return this.toString();
	}
}