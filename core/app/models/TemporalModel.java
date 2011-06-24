package models;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;

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
	
	public String[] getCopyProperties(){
		String[] array = {};
		return array;
	}
	protected Object _copy(Object to) {

		String[] cloneProps = getCopyProperties();
		
		for (int i = 0; i < cloneProps.length; i++) {
			Object value;
			try {
				value = PropertyUtils.getProperty(this, cloneProps[i]);
				PropertyUtils.setProperty(to, cloneProps[i], value);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return to;
	}
		
}