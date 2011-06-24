package controllers;

import java.util.List;
import models.VersionModel;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;


import play.mvc.With;

@With(Secure.class)

public class VersionModels extends Controller {
	public static void index() {
		List<VersionModel> entities = models.VersionModel.all().fetch();
		render(entities);
	}

	public static void create(VersionModel entity) {
		render(entity);
	}

	public static void show(java.lang.Long id) {
    VersionModel entity = VersionModel.findById(id);
		render(entity);
	}

	public static void edit(java.lang.Long id) {
    VersionModel entity = VersionModel.findById(id);
		render(entity);
	}

	public static void delete(java.lang.Long id) {
    VersionModel entity = VersionModel.findById(id);
    entity.delete();
		index();
	}
	
	public static void save(@Valid VersionModel entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
    entity.save();
		flash.success(Messages.get("scaffold.created", "VersionModel"));
		index();
	}

	public static void update(@Valid VersionModel entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}
		
      		entity = entity.merge();
		
		entity.save();
		flash.success(Messages.get("scaffold.updated", "VersionModel"));
		index();
	}

}
