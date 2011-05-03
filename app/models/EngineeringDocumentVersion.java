package models;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Engineering")
public class EngineeringDocumentVersion extends DocumentVersion {

	public EngineeringDocumentVersion(String number, String name) {
		super(number, name);
	}
	
	public String toString(){
		return name;
	}
}