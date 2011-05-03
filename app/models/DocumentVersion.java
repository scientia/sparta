package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Valid;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"identifier_id", "revision", "iteration"})
})
@javax.persistence.Entity
@javax.persistence.Inheritance
@javax.persistence.DiscriminatorColumn(name = "type")
public abstract class DocumentVersion extends ProductVersion {
	
	@Required
	@MaxSize(100)
	public String number;

	
	//@Embedded
	//@Valid DocumentSubType type;
	
	@ManyToOne
	@Valid public Document identifier;
	
	/*@ManyToMany(cascade= CascadeType.PERSIST,  mappedBy="documents")
    public Set<PartVersion> parts;*/
	
	public DocumentVersion(String number, String name){
		this.number = number;
		this.name = name;
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
}
