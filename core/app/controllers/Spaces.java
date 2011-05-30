package controllers;

import java.util.List;

import models.Project;
import models.Space;
import models.Staff;
import controllers.CoreController.ObjectType;
import play.mvc.With;

@With(Secure.class)
public class Spaces extends CoreController {

	
	
	public static void projects(String id) {
		ObjectType type = ObjectType.get(Spaces.class);
		notFoundIfNull(type);
		Space object = (Space) type.findById(id);
		notFoundIfNull(object);
		List<Project> objects = object.getProjects();

		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
}
