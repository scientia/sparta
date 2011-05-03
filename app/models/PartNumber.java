package models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
public class PartNumber extends Model{

	/*@SequenceGenerator(name="PartNumber_Gen", sequenceName="PartNumber-Seq")
    @GeneratedValue(generator="PartNumber_Gen")*/
	
	 
	 
	 @Column(name="number", precision = 32)
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SmartIdSequenceGenerator")
	 @GenericGenerator (name = "SmartIdSequenceGenerator",strategy = "models.SmartIdSequenceGenerator",
	         parameters = {
			 @org.hibernate.annotations.Parameter(name = "format", value = "LD0000000")
	         })
	
	public String number;
	
	public String justString;
	
	public PartNumber(String str){
		this.justString = str;
	}
	public PartNumber save() {
		em().persist(this);
		em().refresh(this);
		return this;
    }
	
	public static EntityManager em() {
        return JPA.em();
    }

    public boolean isPersistent() {
        return JPA.em().contains(this);
    }
}
