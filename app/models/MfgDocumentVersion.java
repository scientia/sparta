package models;

@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Manufacturing")
public class MfgDocumentVersion extends DocumentVersion {

	public MfgDocumentVersion(String number, String name) {
		super(number, name);
	}

	public String toString(){
		return name;
	}
}
