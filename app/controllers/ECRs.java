package controllers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Change;
import models.ECO;
import models.ECR;
import models.EngChangeTask;
import models.EngineeringChange;
import models.Task;

import controllers.CoreController.ObjectType;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Messages;
import play.mvc.With;

@With(Secure.class)
public class ECRs extends ChangeController {

	
	public static void listecrs(String q){
		String queryLike = LIKE_CHAR + q + LIKE_CHAR;
		List<ECR> ecrs;

		if (queryLike == null) {
			ecrs = ECR.findAll();
		} else {
			ecrs = ECR.find("byNameLike", queryLike).fetch(
					DRPDOWN_MAX_NO_OBJS);
		}
		List ecrlist = new ArrayList();
		for (ECR ecr : ecrs) {
			Map<String, String> smap = new HashMap<String, String>();
			smap.put("id", ecr.id.toString());
			smap.put("name", ecr.name);
			ecrlist.add(smap);
		}
		renderJSON(ecrlist);
	}
	
	
	/**
	 * 
	 * @param id
	 */
	public static void ecos(String id){
		ECR object = (ECR)getChangeObject(id);
		List<ECO> objects = object.getECOs();	
		ObjectType type = ObjectType.get(ECOs.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
}
