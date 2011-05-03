package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.CoreController.ObjectType;

import models.DocumentVersion;
import models.ECO;
import models.ECR;
import models.EngineeringChange;
import models.PartVersion;
import play.mvc.With;

@With(Secure.class)
public class ECOs extends ChangeController {

	public static void listecos(String q){
		String queryLike = LIKE_CHAR + q + LIKE_CHAR;
		List<ECO> ecos;

		if (queryLike == null) {
			ecos = ECO.findAll();
		} else {
			ecos = ECO.find("byNameLike", queryLike).fetch(
					DRPDOWN_MAX_NO_OBJS);
		}
		List ecolist = new ArrayList();
		for (ECO eco : ecos) {
			Map<String, String> smap = new HashMap<String, String>();
			smap.put("id", eco.id.toString());
			smap.put("name", eco.name);
			ecolist.add(smap);
		}
		renderJSON(ecolist);
	}
	
	/**
	 * 
	 * @param id
	 */
	public static void ecrs(String id){
		ECO object = (ECO)getChangeObject(id);
		List<ECR> objects = object.getECRs();	
		ObjectType type = ObjectType.get(ECRs.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
}
