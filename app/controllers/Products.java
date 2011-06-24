package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import models.Customer;
import models.File;
import models.PartVersion;
import models.ProductVersion;
import models.User;
import controllers.CoreController.ObjectType;
import play.data.binding.Binder;
import play.db.Model;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Messages;
import play.mvc.With;


@With(Secure.class)
public abstract class Products extends CoreController {

	public static String[] cloneprops = {"name", "finish", "material", "project", "description", "unitValue"};
	
	
	/** Bulk Actions */
	public static void bulkfreeze(Long[] keys) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		
		for(int i=0; i< keys.length; i++){
			Long id = keys[i];
			ProductVersion object = (ProductVersion) type.findById(id);
			object.frozen = true;
			validation.valid(object);
			if (validation.hasErrors()) {
				JPA.setRollbackOnly();
				renderText("Falied to freeze the product "+ object.getDisplayName());
			}
			object.save();				
		}
		renderText("Product frozen successfully");
	}
	
	public static void bulkunfreeze(Long[] keys) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		
		for(int i=0; i< keys.length; i++){
			Long id = keys[i];
			ProductVersion object = (ProductVersion) type.findById(id);
			object.frozen = false;
			validation.valid(object);
			if (validation.hasErrors()) {
				JPA.setRollbackOnly();
				renderText("Falied to unfreeze the product "+ object.getDisplayName());
			}
			object.save();				
		}
		renderText("Product unfrozen successfully");
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
	
	/** Ajax methods */
	public static void xfreeze(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		params.put("object.frozen", "true");
		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderText("Could not freeze the products.");
		}
		object._save();
		renderText("Product frozen successfully");
	}
	
	public static void xunfreeze(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		params.put("object.frozen", "false");
		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderText("Could not unfreeze the parts.");
		}
		object._save();
		renderText("Product unfrozen successfully");
	}
	
	
	/** Non ajax methods */
	/**
	 * N
	 * @param id
	 * @throws Exception
	 */
	public static void freeze(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		params.put("object.frozen", "false");
		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderArgs.put("error", Messages.get("core.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/edit.html",
						type, object);
			} catch (TemplateNotFoundException e) {
				render("core/edit.html", type, object);
			}
		}
		object._save();
		flash.success(Messages.get("sparta.frozen", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
		}
		redirect(request.controller + ".show", object._key());
	}
	
	public static void revise(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = _revise(id);
		if(object == null){
			flash.error(Messages.get("core.revisefailed", type.modelName));
			redirect(request.controller + ".show",id);
		}
		flash.success(Messages.get("core.revised", type.modelName));
		redirect(request.controller + ".show", object._key());
	}
	
	public static void tab(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model tabObject = _tab(id, type);
		if(tabObject == null){
			flash.error(Messages.get("core.tabfailed", type.modelName));
			redirect(request.controller + ".show",id);
		}
		flash.success(Messages.get("core.tabbed", type.modelName));
		redirect(request.controller + ".show", tabObject._key());
	}
	
	public static ProductVersion _tab(String id, ObjectType type){		
		ProductVersion object = (ProductVersion)type.findById(id);
		notFoundIfNull(object);		
		ProductVersion tabObject = object.tab();
		tabObject.save();
		return tabObject;
	}
	
	public static void clone(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model cloneObject = _clone(id, type);
		if(cloneObject == null){
			flash.error(Messages.get("core.clonefailed", type.modelName));
			redirect(request.controller + ".show",id);
		}
		flash.success(Messages.get("core.cloned", type.modelName));
		redirect(request.controller + ".show", cloneObject._key());
	}
	private static ProductVersion _revise(String id){
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		ProductVersion object = (ProductVersion)type.findById(id);
		notFoundIfNull(object);
		ProductVersion revisedObject = object.revise();
		revisedObject.save();
		return revisedObject;
	}
	
	private static ProductVersion _clone(String id, ObjectType type){
		ProductVersion object = (ProductVersion)type.findById(id);
		notFoundIfNull(object);
		ProductVersion cloneObject = object.clone();
		cloneObject.save();
		return cloneObject;
	}
	
	private static ProductVersion _tab(Long id){
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		ProductVersion object = (ProductVersion)type.findById(id);
		notFoundIfNull(object);
		ProductVersion revisedObject = object.tab();
		revisedObject.save();
		return revisedObject;
	}
}
