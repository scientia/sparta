package models;

public enum RevReason {

	R("Redesign"),
	A("Administration"),
	C("Correction");
	
	RevReason(String displayName) {
		this.displayName = displayName;
	}
	
	private String displayName;
	
	public String getString(){
		return this.displayName;
	}
	
	public String getDisplayName(){
		return this.displayName;
	}
}
