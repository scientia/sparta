package models;

@javax.persistence.Entity
@javax.persistence.DiscriminatorValue(value="Design")
public class DesignDocumentVersion extends DocumentVersion{

	public DesignDocumentVersion(String name) {
		super(name);
	}

	public String toString(){
		return this.name;
	}
}
