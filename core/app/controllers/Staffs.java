package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.CoreController.ObjectType;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Messages;
import play.mvc.With;
import models.Organization;
import models.Project;
import models.Role;
import models.Staff;
import models.TemporalModel;
import models.User;

@With(Secure.class)
public class Staffs extends CoreController {


	public static void liststaffs() {
		String queryIn = params.get("q");
		String queryLike = LIKE_CHAR + queryIn + LIKE_CHAR;
		List<Staff> staffs;
		
		if (queryLike == null) {
			staffs = Staff.findAll();
		} else {
			staffs = Staff.find("byNameLike", queryLike).fetch(
					DRPDOWN_MAX_NO_OBJS);
		}
		List stafflist = new ArrayList();
		for (Staff staff : staffs) {
			Map<String, String> smap = new HashMap<String, String>();
			smap.put("id", staff.id.toString());
			smap.put("name", staff.name);
			stafflist.add(smap);
		}
		renderJSON(stafflist);

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
	
	/**
	 * Members tab
	 * @param id
	 */
	public static void members(String id) {
		ObjectType type = ObjectType.get(Staffs.class);
		notFoundIfNull(type);
		Staff object = (Staff) type.findById(id);
		notFoundIfNull(object);
		List<User> objects = object.getUsers();

		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	/**
	 * Projects tab
	 * @param id
	 */
	public static void projects(String id) {
		ObjectType type = ObjectType.get(Staffs.class);
		notFoundIfNull(type);
		Staff object = (Staff) type.findById(id);
		notFoundIfNull(object);
		List<Project> objects = object.getProjects();

		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	/**
	 * User editor
	 * @param id
	 */
	public static void addusers(String id){
		ObjectType type = ObjectType.get(Staffs.class);
		notFoundIfNull(type);
		Staff object = (Staff) type.findById(id);
		notFoundIfNull(object);
		String orgids = params.get("staffusers");
		List<User> users = relIdsToUsers(orgids);
		List<String> updates = new ArrayList<String>();
		for(User usr: users){
			object.addUser(usr);
			updates.add(usr.username);
		}		
		object.save();
		flash.success(Messages.get("sparta.relation.users", type.modelName));
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	public static List<User> relIdsToUsers(String userids) {
		List<String> ids = stringToArrayList(userids);
		List<User> models = new ArrayList<User>();
		for(String id : ids){
			User dv = User.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;
	}
	
}
