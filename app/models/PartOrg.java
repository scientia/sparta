package models;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(uniqueConstraints={
		@UniqueConstraint(name="partcustomeruq", columnNames={"partVersion", "customer"}),
		@UniqueConstraint(name="partplantuq", columnNames={"partVersion", "plant"}),
		@UniqueConstraint(name="partsupplieruq", columnNames={"partVersion", "supplier"})
})
public class PartOrg extends AuthoredTemporalModel {

	/** Attach external organization to a version of the part so as to
	 *  manage the organization specific information on this version.
	 *  This information may remain same or change depending on the 
	 *  external organization's revision model.
	 * 
	 *  We copy this information on the revisions of the PartVersion
	 *  than iteration. We do not keep any information on the iteration
	 */
	
	@ManyToOne
	public PartVersion partVersion;
	
	@Column(unique=true)
	public String orgPartnumber;
	
	public String orgPartName;
	
	public String orgPartRevision;
	
	
	public boolean endItem;
}
