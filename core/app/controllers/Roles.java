package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controllers.CoreController.ObjectType;
import models.PartVersion;
import models.Role;
import models.User;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;


import play.mvc.With;

@With(Secure.class)
public class Roles extends CoreController{
	
	/**
	 * get roles
	 */
	public static void listroles(){
		String queryIn = params.get("q");
		String queryLike = LIKE_CHAR+queryIn+LIKE_CHAR;
		List<Role> roles;
		/*if query is null find the latest documents that user
		 *  has either recently created or updated 
		 */
		if(queryLike == null){
					
			roles = Role.findAll();

		}else{
			roles = Role.find("byNameLike", queryLike).fetch(DRPDOWN_MAX_NO_OBJS);
		}
		List rolelist = new ArrayList();
		for (Role role : roles) {
			Map<String, String> umap = new HashMap<String, String>();
			umap.put("id", role.id.toString());
			umap.put("name", role.name);
			rolelist.add(umap);
		}
		renderJSON(rolelist);
	}
	/**
	 * Add user to a role
	 * @param id
	 */
	public static void addusers(String id) {
		ObjectType type = ObjectType.get(Roles.class);
		notFoundIfNull(type);
		Role object = (Role) type.findById(id);
		notFoundIfNull(object);
		String userids = params.get("userroles");
		List<String> updates = new ArrayList<String>();
		
		List<User> users = relIdsToUsers(userids);
		for(User usr: users){
			object.addUser(usr);
			updates.add(usr.username);
		}		
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}
	
	
	private static List<User> relIdsToUsers(String partIds) {
		List<String> ids = stringToArrayList(partIds);
		List<User> models = new ArrayList<User>();
		for(String id : ids){
			User usr = User.findById(Long.parseLong(id));
			models.add(usr);
		}
		return models;
	}
	
	/**
	 * list all the 
	 * @param id
	 */
	public static void userroles(String id) {
		ObjectType type = ObjectType.get(Roles.class);
		notFoundIfNull(type);
		Role object = (Role) type.findById(id);
		notFoundIfNull(object);
		List<User> objects = object.getUsers();
		
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
}