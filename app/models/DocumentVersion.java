package models;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.beanutils.PropertyUtils;

import models.number.DocumentNumber;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.JPA;
//import play.modules.elasticsearch.annotations.ElasticSearchable;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"identifier_id", "revision", "iteration"})
})
@javax.persistence.Entity
@javax.persistence.Inheritance
@javax.persistence.DiscriminatorColumn(name = "type")
public class DocumentVersion extends ProductVersion {

	@Transient
	private static String[] cloneProps = {"name", "description", "project", "frozen", "type", "subtype",
		                                  "cloned", "identifier" };
	
	public String subtype;
	
	@ManyToOne
	@Valid public Document identifier;
	
	/*@ManyToMany(cascade= CascadeType.PERSIST,  mappedBy="documents")
    public Set<PartVersion> parts;*/
	
	public DocumentVersion(){
		
	}
	public DocumentVersion(String name){
		this.name = name;
	}

	public String getDisplayName(){
		StringBuffer sf = new StringBuffer();		
		sf.append(this.identifier.number);
		sf.append("-");
		sf.append(this.identifier.tab);
		sf.append(",");
		sf.append(this.revision);
		sf.append(",");
		sf.append(this.iteration);
		return sf.toString();		
	}
	
	@PrePersist
	public void beforeCreate(){
		String number = DocumentNumber.getDocumentNumber();
		Document document = new Document(number).save();
		this.identifier = document;
	}
	public void addPart(PartVersion partVersion) {
		new PartDocument(this, partVersion).save();		
	}
	
	public List<PartVersion> getParts() {
		List<PartDocument> partdocuments = PartDocument.find("byDocumentVersion", this).fetch();
		List<PartVersion> partversions = new ArrayList<PartVersion>();
		for(PartDocument partdoc: partdocuments){
			partversions.add(partdoc.partVersion);
		}	
		return partversions;
	}
	
	public String toString(){
		return this.name;
	}
	
	@Override
	public String[] getCopyProperties() {
		return cloneProps;
	}

	@Override
	public DocumentVersion clone() {
		DocumentVersion object = new DocumentVersion();
		object = (DocumentVersion)_clone(object);			
		return object;
	}

	@Override
	public DocumentVersion revise() {
		DocumentVersion object = new DocumentVersion();
		DocumentVersion to = (DocumentVersion)_revise(object);
		return to;
	}

	@Override
	public DocumentVersion tab() {
		Document tabIdentifier = this.identifier.getTabbedDocument();
		DocumentVersion  object = new DocumentVersion();
		object = (DocumentVersion)_tab(object);
		object.identifier = tabIdentifier;
		return object;
	}

	@Override
	public Integer getNextIteration() {
		Query q = JPA.em().createQuery("select max(iteration) from DocumentVersion where identifier=? and revision=?" );
		q.setParameter(1, this.identifier);
		q.setParameter(2, this.revision);
		return (Integer)q.getSingleResult() + 1;
	}

	@Override
	public Integer getNextRevision() {
		Query q = JPA.em().createQuery("select max(revision) from DocumentVersion where identifier=?" );
		q.setParameter(1, this.identifier);		
		return (Integer)q.getSingleResult() + 1;
	}
	
   
}
