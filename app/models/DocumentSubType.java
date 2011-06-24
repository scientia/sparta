package models;

import javax.persistence.Entity;

import org.dom4j.rule.Mode;

import play.db.jpa.Model;

@Entity
public class DocumentSubType extends TemporalModel{

   public String name;
   
   public DocumentSubType(String name){
	   this.name = name;
   }
}
