package models;

@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Support")
public class SupportDocumentVersion extends DocumentVersion {
	

	public SupportDocumentVersion(String name) {
		super(name);		
	}

	/**
	 * @generated
	 */
	public String toString() {
      return name;
	}
}