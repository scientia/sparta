package controllers;

import java.util.List;
import models.DocumentVersion;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;


import play.mvc.With;

@With(Secure.class)

public class DocumentVersions extends Controller {
	public static void index() {
		List<DocumentVersion> entities = models.DocumentVersion.all().fetch();
		render(entities);
	}

	public static void create(DocumentVersion entity) {
		render(entity);
	}

	public static void show(java.lang.Long id) {
    DocumentVersion entity = DocumentVersion.findById(id);
		render(entity);
	}

	public static void edit(java.lang.Long id) {
    DocumentVersion entity = DocumentVersion.findById(id);
		render(entity);
	}

	public static void delete(java.lang.Long id) {
    DocumentVersion entity = DocumentVersion.findById(id);
    entity.delete();
		index();
	}
	
	public static void save(@Valid DocumentVersion entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
    entity.save();
		flash.success(Messages.get("scaffold.created", "DocumentVersion"));
		index();
	}

	public static void update(@Valid DocumentVersion entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}
		
      		entity = entity.merge();
		
		entity.save();
		flash.success(Messages.get("scaffold.updated", "DocumentVersion"));
		index();
	}

}
