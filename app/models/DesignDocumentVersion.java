package models;

@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Design")
public class DesignDocumentVersion extends DocumentVersion{

	public DesignDocumentVersion(String number, String name) {
		super(number, name);
	}

	public String toString(){
		return this.name;
	}
}
