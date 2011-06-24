package models;

public enum YesNoType {
	None("None"),
	Yes("Yes"),
	No("No");
	
	
	YesNoType(String displayName) {
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
