package controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controllers.CoreController.ObjectType;

import models.Change;
import models.DocumentVersion;
import models.Organization;
import models.PartVersion;
import models.Project;
import models.Staff;
import models.Task;
import models.User;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;
import play.exceptions.TemplateNotFoundException;

import play.mvc.With;

@With(Secure.class)
public class Projects extends CoreController {
	
	public static void projectLogo(Long id){
		Project project = Project.findById(id);
		InputStream is = project.logo.get();
        renderBinary(is);
	}
	public static void listprojects() {
		String queryIn = params.get("q");
		String queryLike = LIKE_CHAR + queryIn + LIKE_CHAR;
		List<Project> projects;
		
		if (queryLike == null) {
			projects = Project.findAll();
		} else {
			projects = Project.find("byNameLike", queryLike).fetch(
					DRPDOWN_MAX_NO_OBJS);
		}
		List projectlist = new ArrayList();
		for (Project project : projects) {
			Map<String, String> pmap = new HashMap<String, String>();
			pmap.put("id", project.id.toString());
			pmap.put("name", project.name);
			projectlist.add(pmap);
		}
		renderJSON(projectlist);

	}
	
	
	/** Organization Relations Tab Start*/
	public static void addorgs(String id) {
		ObjectType type = ObjectType.get(Projects.class);
		notFoundIfNull(type);
		Project object = (Project) type.findById(id);
		notFoundIfNull(object);
		String orgids = params.get("projorgs");
		List<Organization> orgs = relIdsToOrganizations(orgids);
		List<String> updates = new ArrayList<String>();
		for(Organization org: orgs){
			object.addOrganization(org);
			updates.add(org.name);
		}		
		object.save();
		flash.success(Messages.get("sparta.relation.documents", type.modelName));
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	public static void organizations(String id) {
		ObjectType type = ObjectType.get(Projects.class);
		notFoundIfNull(type);
		Project object = (Project) type.findById(id);
		notFoundIfNull(object);
		Set<Organization> objects = object.organizations;
		// render(type, documents, documents.size(), documents.size(), 1,
		// orderBy, order);
		type = ObjectType.get(Organizations.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
		// render(objects);
	}

	/**
	 * Add Documents to part.
	 * @param idlist
	 * @return
	 */
	private static List<Organization> relIdsToOrganizations(String idlist){
		List<String> ids = stringToArrayList(idlist);
		List<Organization> models = new ArrayList<Organization>();
		for(String id : ids){
			Organization dv = Organization.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;
		
	}
	
	
	public static void projectstaffs(String id){
		ObjectType type = ObjectType.get(Projects.class);
		notFoundIfNull(type);
		Project object = (Project) type.findById(id);
		notFoundIfNull(object);
		List<Staff> objects = object.getStaffs();
		// render(type, documents, documents.size(), documents.size(), 1,
		// orderBy, order);
		type = ObjectType.get(Staffs.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void addstaffs(String id){
		ObjectType type = ObjectType.get(Projects.class);
		notFoundIfNull(type);
		Project object = (Project) type.findById(id);
		notFoundIfNull(object);
		String staffids = params.get("projectstaffs");
		List<Staff> staffs = relIdsToStaffs(staffids);
		List<String> updates = new ArrayList<String>();
		for(Staff staff: staffs){
			object.addStaff(staff);
			updates.add(staff.name);
		}		
		object.save();
		flash.success(Messages.get("sparta.relation.staffs", type.modelName));
		request.current().contentType = "application/json";
		renderJSON(updates);
	}


	private static List<Staff> relIdsToStaffs(String staffids) {
		List<String> ids = stringToArrayList(staffids);
		List<Staff> models = new ArrayList<Staff>();
		for(String id : ids){
			Staff dv = Staff.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;
	}
	
	public static void tasks(String id){
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Project object =  (Project)type.findById(id);
		notFoundIfNull(object);
		List<Task> objects = object.getTasks();		
		type = ObjectType.get(Tasks.class);
		try{
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
		}catch (TemplateNotFoundException e) {
			render("common/tasks.html", type, objects, objects.size(), objects.size(), 0, "number", "DESC");
		}
	}
}
