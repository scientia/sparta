package models;

@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Specification")
public class SpecDocumentVersion extends DocumentVersion{

	public SpecDocumentVersion(String name) {
		super(name);
	}

}
