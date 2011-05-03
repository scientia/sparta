package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controllers.CoreController.For;
import controllers.CoreController.ObjectType;
import models.DesignDocumentVersion;
import models.Document;
import models.DocumentVersion;
import models.PartVersion;
import models.User;
import play.mvc.Controller;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.data.validation.Valid;
import play.exceptions.TemplateNotFoundException;


import play.mvc.With;

@With(Secure.class)

@For(DocumentVersion.class)
public abstract class Documents extends Products {
	
	/**
	 * Document Dropdown
	 * 
	 */
	
	public static void listdocs(){
		String queryIn = params.get("q");
		String queryLike = LIKE_CHAR+queryIn+LIKE_CHAR;
		List<DocumentVersion> documents;
		/*if query is null find the latest documents that user
		 *  has either recently created or updated 
		 */
		if(queryLike == null){
			String currentUser = Security.connected();		
			documents = DocumentVersion.find("select d from DocumentVersion d where d.createdBy = ? or d.updatedBy = ? order by lastupdated", currentUser, currentUser).fetch(DRPDOWN_MAX_NO_OBJS);

		}else{
			documents = DocumentVersion.find("byNameLike", queryLike).fetch(DRPDOWN_MAX_NO_OBJS);
		}
		List doclist = new ArrayList();
		for (DocumentVersion doc : documents) {
			Map<String, String> umap = new HashMap<String, String>();
			umap.put("id", doc.id.toString());
			umap.put("name", doc.name);
			doclist.add(umap);
		}
		renderJSON(doclist);
	}
	
	public static void ecos(){
		
	}
	
	public static void ecns(){
		
	}

	public static void parts(String id){
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		DocumentVersion object = (DocumentVersion)type.findById(id);
		notFoundIfNull(object);
		List<PartVersion> objects = object.getParts();
		//render(type, documents, documents.size(), documents.size(), 1, orderBy, order);
		//type = ObjectType.get(Parts.class);
		try {
			render(type, objects, objects.size(), objects.size(), 0, "number", "DESC");
		} catch (TemplateNotFoundException e) {
			render("Documents/parts.html", type, objects, objects.size(), objects.size(), 0, "number", "DESC");
		}
		
		//render(objects);
	}
	
	public static void files(){
		
	}
	
	/** Parts Tab*/
	/**
	 * Add child parts to the parent
	 * @param id
	 */
	public static void addparts(String id, String partdocs) {
		notFoundIfNull(partdocs);
		ObjectType type = ObjectType.get(getControllerClass());
		String substr = type.controllerClass.getName();
		notFoundIfNull(type);
		DocumentVersion object = (DocumentVersion) type.findById(id);
		notFoundIfNull(object);
		List<String> updates = new ArrayList<String>();
		
		List<PartVersion> parts = relIdsToParts(partdocs);
		for(PartVersion part: parts){
			object.addPart(part);
			updates.add(part.name);
		}		
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	private static List<PartVersion> relIdsToParts(String partdocs) {
		List<String> ids = stringToArrayList(partdocs);
		List<PartVersion> models = new ArrayList<PartVersion>();
		for(String id : ids){
			PartVersion pv = PartVersion.findById(Long.parseLong(id));
			models.add(pv);
		}
		return models;
	}
	
	public static void addecos(String id, String documentecos){
		
	}
	
	public static void addecrs(String id, String documentecrs){
		
	}
}
