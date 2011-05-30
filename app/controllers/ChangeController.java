package controllers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import models.Change;
import models.Customer;
import models.DocumentVersion;
import models.ECO;
import models.EngChangeTask;
import models.EngineeringChange;
import models.File;
import models.PartVersion;
import models.Plant;
import models.Supplier;
import models.Task;
import controllers.CoreController.ObjectType;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Messages;
import play.mvc.With;

@With(Secure.class)
public abstract class ChangeController extends CoreController {

	public static void addTask(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Change change = (Change) type.findById(id);
		notFoundIfNull(change);
		Task object = new Task();
		type = ObjectType.get(Tasks.class);
		try {
			render(type, object, change);
		} catch (TemplateNotFoundException e) {
			notFound();
		}
	}

	public static void createTask(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		EngineeringChange change = (EngineeringChange) type.findById(id);
		notFoundIfNull(change);
		Task object = new Task();
		object.project = change.project;
		type = ObjectType.get(Tasks.class);
		Binder.bind(object, "object", params.all());
		validation.valid(object);
		if (validation.hasErrors()) {
			renderArgs.put("error", Messages.get("core.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/addTask.html",
						type, object, change);
			} catch (TemplateNotFoundException e) {
				notFound();
			}
		}
		object._save();
		new EngChangeTask(change, object).save();
		flash.success(Messages.get("core.created", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".show", change._key());
		}
		if (params.get("_saveAndAddAnother") != null) {
			redirect(request.controller + ".addTask");
		}
		redirect(request.controller + ".show", change._key());
	}

	public static void files(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Change object = (Change) type.findById(id);
		notFoundIfNull(object);
		List<File> objects = object.getFiles(type.entityClass.getName());
		type = ObjectType.get(Files.class);
		try {
			render(type, objects, objects.size(), objects.size(), 0, "name",
					"DESC");
		} catch (TemplateNotFoundException e) {
			render("common/files.html", type, objects, objects.size(), objects
					.size(), 0, "name", "DESC");
		}
	}

	public static void tasks(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Change object = (Change) type.findById(id);
		notFoundIfNull(object);
		List<Task> objects = object.getTasks();
		type = ObjectType.get(Tasks.class);
		try {
			render(type, objects, objects.size(), objects.size(), 0, "number",
					"DESC");
		} catch (TemplateNotFoundException e) {
			render("common/tasks.html", type, objects, objects.size(), objects
					.size(), 0, "number", "DESC");
		}
	}

	/** Add affected parts on change */
	public static void addaffdparts(String id, String affparts) {
		EngineeringChange object = (EngineeringChange) getChangeObject(id);

		List<PartVersion> parts = relIdsToParts(affparts);
		List<String> updates = new ArrayList<String>();
		for (PartVersion part : parts) {
			object.addAffectedPart(part);
			updates.add(part.name);
		}
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	/** Affected parts tab on change **/
	public static void affectedparts(String id) {
		EngineeringChange object = (EngineeringChange) getChangeObject(id);
		List<PartVersion> objects = ((EngineeringChange) object)
				.getAffectedParts();
		ObjectType type = ObjectType.get(Parts.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}

	/**
	 * Add resulted part to a change
	 * 
	 * @param id
	 * @param affparts
	 */
	public static void addrsltedparts(String id, String rsltparts) {
		EngineeringChange object = (EngineeringChange) getChangeObject(id);
		List<PartVersion> parts = relIdsToParts(rsltparts);
		List<String> updates = new ArrayList<String>();
		for (PartVersion part : parts) {
			object.addResultedPart(part);
			updates.add(part.name);
		}
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	/**
	 * resulted parts tab
	 * 
	 * @param id
	 */
	public static void resultedparts(String id) {
		EngineeringChange object = (EngineeringChange) getChangeObject(id);
		List<PartVersion> objects = ((EngineeringChange) object)
				.getResultedParts();
		ObjectType type = ObjectType.get(Parts.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}

	/**
	 * 
	 * @param id
	 * @param rfncdparts
	 */
	public static void addrfncdparts(String id, String rfncdparts) {
		EngineeringChange object = (EngineeringChange) getChangeObject(id);
		List<PartVersion> parts = relIdsToParts(rfncdparts);
		List<String> updates = new ArrayList<String>();
		for (PartVersion part : parts) {
			object.addReferencedPart(part);
			updates.add(part.name);
		}
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	/**
	 * Referenced Parts Tab
	 * 
	 * @param id
	 */
	public static void rfrncedparts(String id) {
		EngineeringChange object = (EngineeringChange) getChangeObject(id);
		List<PartVersion> objects = ((EngineeringChange) object)
				.getReferencedParts();
		ObjectType type = ObjectType.get(Parts.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}

	/**
	 * Add referenced documents
	 * 
	 * @param id
	 * @param rfncdparts
	 */
	public static void addrfncddocs(String id, String rfncddocs) {
		EngineeringChange object = (EngineeringChange) getChangeObject(id);
		List<DocumentVersion> docs = relIdsToDocs(rfncddocs);
		List<String> updates = new ArrayList<String>();
		for (DocumentVersion doc : docs) {
			object.addReferencedDoc(doc);
			updates.add(doc.name);
		}
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	/**
	 * Referenced documents
	 * 
	 * @param id
	 */
	public static void rfrnceddocs(String id) {
		EngineeringChange object = (EngineeringChange) getChangeObject(id);
		List<DocumentVersion> objects = ((EngineeringChange) object)
				.getReferencedDocs();
		ObjectType type = ObjectType.get(Documents.class);
		render(type, objects, objects.size(), objects.size(), 0, "number",
				"DESC");
	}

	public static void customers(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Change object = (Change) type.findById(id);
		notFoundIfNull(object);
		List<Customer> objects = object.getCustomers(id, type.entityClass
				.getName());
		type = ObjectType.get(Customers.class);
		try {
			render(type, objects, objects.size(), objects.size(), 0, "name",
					"DESC");
		} catch (TemplateNotFoundException e) {
			render("common/customers.html", type, objects, objects.size(), objects
					.size(), 0, "name", "DESC");
		}
	}

	public static void plants(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Change object = (Change) type.findById(id);
		notFoundIfNull(object);
		List<Plant> objects = object.getPlants(id, type.entityClass.getName());
		type = ObjectType.get(Customers.class);
		try{
		render(type, objects, objects.size(), objects.size(), 0, "name", "DESC");
		}catch (TemplateNotFoundException e) {
			render("common/plants.html", type, objects, objects.size(), objects.size(), 0, "name", "DESC");
		}
	}

	public static void suppliers(String id) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Change object = (Change) type.findById(id);
		notFoundIfNull(object);
		List<Plant> objects = object.getPlants(id, type.entityClass.getName());
		type = ObjectType.get(Customers.class);
        try{
		    render(type, objects, objects.size(), objects.size(), 0, "name", "DESC");
        }catch (TemplateNotFoundException e) {
			render("common/suppliers.html", type, objects, objects.size(), objects.size(), 0, "name", "DESC");
		}
	}

	public static void addsuppliers(String id, String changesuppliers) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Change object = (Change) type.findById(id);
		notFoundIfNull(object);
		List<Supplier> suppliers = ControllerRelationUtil
				.relIdsToSuppliers(changesuppliers);
		List<String> updates = new ArrayList<String>();
		for (Supplier supplier : suppliers) {
			object.addSupplier(supplier);
			updates.add(supplier.name);
		}
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	public static void addplants(String id, String changeplants) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Change object = (Change) type.findById(id);
		notFoundIfNull(object);
		List<Plant> plants = ControllerRelationUtil
				.relIdsToPlants(changeplants);
		List<String> updates = new ArrayList<String>();
		for (Plant plant : plants) {
			object.addPlant(plant);
			updates.add(plant.name);
		}
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
	}

	public static void addcustomers(String id, String changecustomers) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Change object = (Change) type.findById(id);
		notFoundIfNull(object);
		List<Customer> customers = ControllerRelationUtil
				.relIdsToCustomers(changecustomers);
		List<String> updates = new ArrayList<String>();
		for (Customer customer : customers) {
			object.addCustomer(customer);
			updates.add(customer.name);
		}
		object.save();
		request.current().contentType = "application/json";
		renderJSON(updates);
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
		for (String id : ids) {
			PartVersion dv = PartVersion.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;
	}

	private static List<DocumentVersion> relIdsToDocs(String idlist) {
		List<String> ids = stringToArrayList(idlist);
		List<DocumentVersion> models = new ArrayList<DocumentVersion>();
		for (String id : ids) {
			DocumentVersion dv = DocumentVersion.findById(Long.parseLong(id));
			models.add(dv);
		}
		return models;
	}

}
