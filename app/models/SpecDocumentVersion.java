package models;

@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Specification")
public class SpecDocumentVersion extends DocumentVersion{

	public SpecDocumentVersion(String number, String name) {
		super(number, name);
	}

}
