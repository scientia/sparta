package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import play.data.validation.MaxSize;
import play.data.validation.Required;

@MappedSuperclass
public abstract class ProductVersionRelation extends AuthoredTemporalModel{
	
	/**
	 * the string that specifies the meaning of this relation
	 *  Derivation: the instance defines a deriving relationship where the related ProductVersion 
	 *      is based on the relating ProductVersion which is an earlier version of the same or of a 
	 *      different Product; 
     *  Hierarchy: The instance defines a hierarchical relationship where the related ProductVersion
     *      is a subordinate version of the relating ProductVersion; 
     *      EXAMPLE 1   'Rev. 1.1' and 'rev. 1.2' are subordinates of 'version 1'.
     *  Sequence: the instance defines a version sequence where the relating ProductVersion 
     *      is the preceding version of the related ProductVersion that is the following version.
     *      For a given ProductVersion, there shall be at most one ProductVersionRelationship 
     *      of this relationType referring to this ProductVersion as 'relating' and at most one 
     *      ProductVersionRelationship of this relationType referring as 'related'. 
	 */
	public String relationType;


	@Lob
    @MaxSize(500)
    public String description;
}
