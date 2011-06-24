package controllers;

import java.util.List;
import models.DocumentSubType;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;


import play.mvc.With;

@With(Secure.class)

public class DocumentSubTypes extends Controller {
	public static void index() {
		List<DocumentSubType> entities = models.DocumentSubType.all().fetch();
		render(entities);
	}

	public static void create(DocumentSubType entity) {
		render(entity);
	}

	public static void show(java.lang.Long id) {
    DocumentSubType entity = DocumentSubType.findById(id);
		render(entity);
	}

	public static void edit(java.lang.Long id) {
    DocumentSubType entity = DocumentSubType.findById(id);
		render(entity);
	}

	public static void delete(java.lang.Long id) {
    DocumentSubType entity = DocumentSubType.findById(id);
    entity.delete();
		index();
	}
	
	public static void save(@Valid DocumentSubType entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
    entity.save();
		flash.success(Messages.get("scaffold.created", "DocumentSubType"));
		index();
	}

	public static void update(@Valid DocumentSubType entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}
		
      		entity = entity.merge();
		
		entity.save();
		flash.success(Messages.get("scaffold.updated", "DocumentSubType"));
		index();
	}

}
