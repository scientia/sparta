package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controllers.CoreController.ObjectType;
import models.Organization;
import models.Project;
import models.User;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;


import play.mvc.With;

@With(Secure.class)
public class Organizations extends CoreController {
	
	public static void listorgs(){
		String queryIn = params.get("q");
		String queryLike = LIKE_CHAR+queryIn+LIKE_CHAR;
		List<Organization> orgs;
		/*if query is null find the latest documents that user
		 *  has either recently created or updated 
		 */
		if(queryLike == null){				
			orgs = Organization.findAll();
		}else{
			orgs = Organization.find("byNameLike", queryLike).fetch(DRPDOWN_MAX_NO_OBJS);
		}
		List orglist = new ArrayList();
		for (Organization org : orgs) {
			Map<String, String> omap = new HashMap<String, String>();
			omap.put("id", org.id.toString());
			omap.put("name", org.name);
			orglist.add(omap);
		}
		renderJSON(orglist);
	}
	
	public static void edit(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);

		notFoundIfNull(object);
		try {
			render(type, object);
		} catch (TemplateNotFoundException e) {
			render("core/edit.html", type, object);
		}
	}
	
	
	public static void participatingprojects(String id) {
		ObjectType type = ObjectType.get(Organizations.class);
		notFoundIfNull(type);
		Organization object = (Organization) type.findById(id);
		notFoundIfNull(object);
		Set<Project> objects = object.projects;
		// render(type, documents, documents.size(), documents.size(), 1,
		// orderBy, order);
		type = ObjectType.get(Projects.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
		// render(objects);
	}
	
	public static void ownedprojects(String id){
		ObjectType type = ObjectType.get(Organizations.class);
		notFoundIfNull(type);
		Organization object = (Organization) type.findById(id);
		notFoundIfNull(object);
		List<Project> objects = object.getOwnedProjects();
		// render(type, documents, documents.size(), documents.size(), 1,
		// orderBy, order);
		type = ObjectType.get(Projects.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
		// render(objects);
	}
}
