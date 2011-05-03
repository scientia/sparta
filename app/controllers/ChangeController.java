package controllers;

import java.util.ArrayList;
import java.util.List;

import models.DocumentVersion;
import models.ECO;
import models.EngineeringChange;
import models.PartVersion;
import models.Supplier;
import controllers.CoreController.ObjectType;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Messages;
import play.mvc.With;

@With(Secure.class)
public abstract class ChangeController extends CoreController{

	/** Add affected parts on change*/
	public static void addaffdparts(String id, String affparts){		
		EngineeringChange object = (EngineeringChange)getChangeObject(id);
		
		List<PartVersion> parts = relIdsToParts(affparts);
		List<String> updates = new ArrayList<String>();
		for(PartVersion part: parts){
			object.addAffectedPart(part);
			updates.add(part.name);
		}		
		object.save();		
		request.current().contentType = "application/json";
		renderJSON(updates);
	}
	
	/** Affected parts tab on change**/
	public static void affectedparts(String id){
		EngineeringChange object = (EngineeringChange)getChangeObject(id);
		List<PartVersion> objects = ((EngineeringChange)object).getAffectedParts();	
		ObjectType type = ObjectType.get(Parts.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}

	/**
	 * Add resulted part to a change
	 * @param id
	 * @param affparts
	 */
	public static void addrsltedparts(String id, String rsltparts){		
		EngineeringChange object = (EngineeringChange)getChangeObject(id);	
		List<PartVersion> parts = relIdsToParts(rsltparts);
		List<String> updates = new ArrayList<String>();
		for(PartVersion part: parts){
			object.addResultedPart(part);
			updates.add(part.name);
		}		
		object.save();		
		request.current().contentType = "application/json";
		renderJSON(updates);
	}
	
	/**
	 * resulted parts tab
	 * @param id
	 */
	public static void resultedparts(String id){
		EngineeringChange object = (EngineeringChange)getChangeObject(id);
		List<PartVersion> objects = ((EngineeringChange)object).getResultedParts();	
		ObjectType type = ObjectType.get(Parts.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	/**
	 * 
	 * @param id
	 * @param rfncdparts
	 */
	public static void addrfncdparts(String id, String rfncdparts){		
		EngineeringChange object = (EngineeringChange)getChangeObject(id);	
		List<PartVersion> parts = relIdsToParts(rfncdparts);
		List<String> updates = new ArrayList<String>();
		for(PartVersion part: parts){
			object.addReferencedPart(part);
			updates.add(part.name);
		}		
		object.save();		
		request.current().contentType = "application/json";
		renderJSON(updates);
	}
	
	/**
	 * Referenced Parts Tab
	 * @param id
	 */
	public static void rfrncedparts(String id){
		EngineeringChange object = (EngineeringChange)getChangeObject(id);
		List<PartVersion> objects = ((EngineeringChange)object).getReferencedParts();	
		ObjectType type = ObjectType.get(Parts.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	/**
	 * Add referenced documents
	 * @param id
	 * @param rfncdparts
	 */
	public static void addrfncddocs(String id, String rfncddocs){		
		EngineeringChange object = (EngineeringChange)getChangeObject(id);	
		List<DocumentVersion> docs = relIdsToDocs(rfncddocs);
		List<String> updates = new ArrayList<String>();
		for(DocumentVersion doc: docs){
			object.addReferencedDoc(doc);
			updates.add(doc.name);
		}		
		object.save();		
		request.current().contentType = "application/json";
		renderJSON(updates);
	}
	/**
	 * Referenced documents
	 * @param id
	 */
	public static void rfrnceddocs(String id){
		EngineeringChange object = (EngineeringChange)getChangeObject(id);
		List<DocumentVersion> objects = ((EngineeringChange)object).getReferencedDocs();	
		ObjectType type = ObjectType.get(Documents.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}
	
	public static void addcustomers(String id){
		
	}
	
	public static void customers(Long id){
		
	}
	
    public static void plants(Long id){
		
	}
    
    public static void suppliers(Long id){
		
	}
	
	public static void addsuppliers(String id){
		
	}
	
	public static void addplants(String id){
		
	}
	
	protected static Model getChangeObject(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Model object = type.findById(id);
		notFoundIfNull(object);
		
		return object;
	}
	
	private static List<PartVersion> relIdsToParts(String idlist) {
		List<String> ids = stringToArrayList(idlist);
		List<PartVersion> models = new ArrayList<PartVersion>();
		for(String id : ids){		
			PartVersion dv = PartVersion.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;	
	}
	
	private static List<DocumentVersion> relIdsToDocs(String idlist) {
		List<String> ids = stringToArrayList(idlist);
		List<DocumentVersion> models = new ArrayList<DocumentVersion>();
		for(String id : ids){		
			DocumentVersion dv = DocumentVersion.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;	
	}
	
}
