package pl.stalkon.ad.core.model;

public enum UserRoleDef {
	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_AD_ADMIN("ROLE_AD_ADMIN"),
	ROLE_COMPANY("ROLE_COMPANY"),
	ROLE_USER("ROLE_USER"),
	ROLE_CONTEST("ROLE_CONTEST"),
	ROLE_ANONYMOUS("ROLE_ANONYMOUS");
	
	private final String value;
	
	UserRoleDef(String value){
		this.value = value;
	}

	
	public String value(){
		return value;
	}
	
}
