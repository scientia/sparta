package models;

public enum ReleaseState {

	Default("DESIGN"),
    Prototype("PROTOTYPE"),
    Production("PRODUCTION"),
    Obsolete("OBSOLETE"),
    Recycling("RECYCLING");
    
	ReleaseState(String displayName) {
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
