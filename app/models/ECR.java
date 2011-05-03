package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.MaxSize;
import play.data.validation.Required;

@Entity
public class ECR extends EngineeringChange {

	
	public void addECO(ECO ecr) {
		new EcoEcr(ecr, this).save();	
	}
	
	public List<ECO> getECOs() {
		List<EcoEcr> changerelns = EcoEcr.find("byEcr", this).fetch();
		List<ECO> ecos = new ArrayList<ECO>();
		for(EcoEcr ecoecrrln: changerelns){					
			ecos.add(ecoecrrln.eco);			
		}	
		return ecos;
	}
}
