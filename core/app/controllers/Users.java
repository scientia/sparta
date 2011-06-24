package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controllers.CoreController.ObjectType;

import models.PartVersion;
import models.Project;
import models.Role;
import models.Staff;
import models.User;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.binding.Binder;
import play.data.validation.Validation;
import play.data.validation.Valid;
import play.db.Model;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;


import play.mvc.With;


@With(Secure.class)
public class Users extends CoreController {
	/**
	 * get roles
	 */
	public static void listusers(){
		String queryIn = params.get("q");
		String queryLike = LIKE_CHAR+queryIn+LIKE_CHAR;
		List<User> users;
		/*if query is null find the latest documents that user
		 *  has either recently created or updated 
		 */
		if(queryLike == null){				
			users = User.findAll();
		}else{
			users = User.find("byUsernameLike", queryLike).fetch(DRPDOWN_MAX_NO_OBJS);
		}
		List userlist = new ArrayList();
		for (User usr : users) {
			Map<String, String> umap = new HashMap<String, String>();
			umap.put("id", usr.id.toString());
			umap.put("name", usr.username);
			userlist.add(umap);
		}
		renderJSON(userlist);
	}
	
	public static void settings(){
		render();
	}
	
	public static void bulkactivate(Long[] keys){		
		for(int i=0; i< keys.length; i++){
			Long id = keys[i];
			User u = User.findById(id);
			u.active = true;
			validation.valid(u);
			if (validation.hasErrors()) {
				JPA.setRollbackOnly();
				renderText("Falied to activate the user "+ u.username);
			}
			u.save();				
		}
		renderText("Users activated sucessfully."); 
	}
	
	public static void bulkdeactivate(Long[] keys){		
		for(int i=0; i< keys.length; i++){
			Long id = keys[i];
			User u = User.findById(id);
			u.active = false;
			validation.valid(u);
			if (validation.hasErrors()) {
				List<play.data.validation.Error> ers = validation.errors();
				for(int j = 0; j < ers.size(); j++){
					play.data.validation.Error er = ers.get(j);
				}
				JPA.setRollbackOnly();
				renderText("Falied to deactivate the user "+ u.username);
			}
			u.save();	
		}
		renderText("Users deactivated sucessfully."); 
	}
	
	public static void xactivate(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		params.put("object.active", "true");
		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderText("Could not activate the user.");
		}
		object._save();
		renderText("User activated successfully");
	}
	
	public static void xdeactivate(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		params.put("object.active", "false");
		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderText("Could not deactivate the user.");
		}
		object._save();
		renderText("User deactivated successfully");
	}
	/**
	 * Add child parts to the parent
	 * @param id
	 */
	public static void addroles(String id) {
		ObjectType type = ObjectType.get(Users.class);
		notFoundIfNull(type);
		User object = (User) type.findById(id);
		notFoundIfNull(object);
		String roleids = params.get("userroles");
		List<String> updates = new ArrayList<String>();
		
		List<Role> roles = relIdsToRole(roleids);
		for(Role role: roles){
			object.addRole(role);
			updates.add(role.name);
		}		
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}
	
	private static List<Role> relIdsToRole(String roleids) {
		List<String> ids = stringToArrayList(roleids);
		List<Role> models = new ArrayList<Role>();
		for(String id : ids){
			Role pv = Role.findById(Long.parseLong(id));
			models.add(pv);
		}
		return models;
	}

	public static void userroles(String id){
		ObjectType type = ObjectType.get(Users.class);
		notFoundIfNull(type);
		User object = (User) type.findById(id);
		notFoundIfNull(object);
		List<Role> objects = object.getRoles();
		type = ObjectType.get(Roles.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void userstaffs(String id){
		ObjectType type = ObjectType.get(Users.class);
		notFoundIfNull(type);
		User object = (User) type.findById(id);
		notFoundIfNull(object);
		List<Staff> objects = object.getStaffs();
		type = ObjectType.get(Staffs.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void userparticipatedprojects(String id){
		ObjectType type = ObjectType.get(Users.class);
		notFoundIfNull(type);
		User object = (User) type.findById(id);
		notFoundIfNull(object);
		List<Project> objects = object.getParticipatingProjects();
		type = ObjectType.get(Projects.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void usermanagedprojects(String id){
		ObjectType type = ObjectType.get(Users.class);
		notFoundIfNull(type);
		User object = (User) type.findById(id);
		notFoundIfNull(object);
		List<Project> objects = object.getManagedProjects();
		type = ObjectType.get(Projects.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
}
