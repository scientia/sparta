package models;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ProductOrgRelation extends AuthoredTemporalModel{
	
	public String orgItemName;
	
	public String orgItemnumber;
	
	public String orgItemrevision;

}
