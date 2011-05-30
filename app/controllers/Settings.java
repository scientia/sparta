package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.CoreController.ObjectType;
import models.User;
import models.ViewDefinitionContext;
import play.data.validation.Valid;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Messages;
import play.mvc.With;
import play.data.validation.Error;

@With(Secure.class)
public class Settings extends CoreController {

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
	
	public static void index(){
		String username = User.getConnected();
		User object = User.find("byUsername", username).first();
		ObjectType type = ObjectType.get(Users.class);
		render(type, object);
		
	}
	
	public static void accountupdate(@Valid User u){
		validation.valid(u);
		
		if (validation.hasErrors()) {
			List<Error> errors = validation.errors();
			for(Error error : errors) {
	             System.out.println(error.message());
	         }
			Map error = new HashMap();
			error.put("error", Messages.get("core.hasErrors"));
			renderJSON(error);
		}
		u.merge();
		u.save();
		renderText("User Updated");
	}
	public static void changepwd(String currentpassword, String newpassword, String verifypassword){
		
		Map value = params.all();
		Map error = new HashMap();
		error.put("error","This is error");		
		renderText(error);
	}
	
	public static void changecontext(){
		String context = params.get("context.id");
		session.remove("viewcontext");
		session.put("viewcontext", context);
	}
}
