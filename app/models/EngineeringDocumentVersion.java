package models;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Engineering")
public class EngineeringDocumentVersion extends DocumentVersion {

	public EngineeringDocumentVersion(String name) {
		super( name);
	}
	
	public String toString(){
		return name;
	}
}