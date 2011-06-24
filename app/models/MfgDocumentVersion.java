package models;

@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Manufacturing")
public class MfgDocumentVersion extends DocumentVersion {

	public MfgDocumentVersion(String name) {
		super( name);
	}

	public String toString(){
		return name;
	}
}
