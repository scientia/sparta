package controllers;

import models.ProductVersion;
import controllers.CoreController.ObjectType;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Messages;
import play.mvc.With;


@With(Secure.class)
public abstract class Products extends CoreController {

	public static void clone(String id) throws Exception{
		
	}
	
    public static void revise(String id) throws Exception{
		
	}

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
	
}
