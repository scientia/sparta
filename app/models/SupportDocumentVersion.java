package models;

@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Support")
public class SupportDocumentVersion extends DocumentVersion {
	

	public SupportDocumentVersion(String number, String name) {
		super(number, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @generated
	 */
	public String toString() {
      return name;
	}
}