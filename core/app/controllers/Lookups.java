package controllers;

import java.util.List;
import java.util.Set;

import models.Lookup;
import models.LookupValue;
import models.Organization;
import models.Project;
import play.mvc.With;
import controllers.CoreController.For;
import controllers.CoreController.ObjectType;

@With(Secure.class)
@For(Lookup.class)
public class Lookups extends CoreController{
	
	public static void lookupvalues(String id) {
	ObjectType type = ObjectType.get(Lookups.class);
	notFoundIfNull(type);
	Lookup object = (Lookup) type.findById(id);
	notFoundIfNull(object);
	List<LookupValue> objects = object.getLookupValues();	
	type = ObjectType.get(LookupValues.class);
	render(type, objects, objects.size(), objects.size(), 0, "number",
			"DESC");
	}

}
